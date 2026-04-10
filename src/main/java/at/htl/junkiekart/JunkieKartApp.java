package at.htl.junkiekart;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

import java.util.ArrayList;

import static com.almasb.fxgl.dsl.FXGLForKtKt.entityBuilder;
import static com.almasb.fxgl.dsl.FXGLForKtKt.texture;

public class JunkieKartApp extends GameApplication {
    private boolean Hit = false;
    private double HitTimer;

    //collision für map
    private Image collisionImage;
    private PixelReader reader;

    private int respawnX;
    private int respawnY;
    private double RespawnTimer;
    private boolean Respawn;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle("Junkie Kart");
        settings.setWidth(1920);
        settings.setHeight(1080);

        settings.setMainMenuEnabled(true);    //Menü aktivieren
        settings.setSceneFactory(new JunkieKartSceneFactory());
    }

    @Override
    protected void initGame() {
        FXGL.getGameWorld().addEntityFactory(new JunkieKartEntityFactory());

        LoadingScreen loading = new LoadingScreen(() -> {
            FXGL.getGameScene().clearUINodes();

            MapSelectionScreen mapSelect = new MapSelectionScreen(mapId -> {
                FXGL.getGameScene().clearUINodes();

                FXGL.spawn("Bag", 600, 250);
                FXGL.spawn("Bag", 700, 780);
                FXGL.spawn("Bag", 1630, 200);
                FXGL.spawn("Bag", 1200, 645);

                CustomizeOverlay[] customize = new CustomizeOverlay[1];
                customize[0] = new CustomizeOverlay(mapId, () -> {
                    FXGL.getGameScene().clearUINodes();
                    FXGL.spawn(mapId);
                    ImageView collisionView = new ImageView(
                            new Image(getClass().getResource("/assets/textures/maps/collision.png").toExternalForm())
                    );
                    collisionView.setFitWidth(FXGL.getAppWidth());
                    collisionView.setFitHeight(FXGL.getAppHeight());

                    collisionImage = collisionView.getImage();
                    reader = collisionImage.getPixelReader();
                    FXGL.spawn("Player", new SpawnData(200, 540).put("skin", customize[0].getSelectedSkin()));
                });
                FXGL.getGameScene().addUINode(customize[0]);
            });
            FXGL.getGameScene().addUINode(mapSelect);
        });
        FXGL.getGameScene().addUINode(loading);
    }

    @Override
    protected void onUpdate(double tpf) {
        var players = FXGL.getGameWorld().getEntitiesByType(EntityType.PLAYER);
        if (players.isEmpty()) return;

        Entity player = players.get(0);
        var bags = FXGL.getGameWorld().getEntitiesByType(EntityType.BAG);
        var needles = FXGL.getGameWorld().getEntitiesByType(EntityType.NADEL);
        var bottles = FXGL.getGameWorld().getEntitiesByType(EntityType.BEER);
        var vomits = FXGL.getGameWorld().getEntitiesByType(EntityType.VOMIT);

        player.setX(Math.clamp(player.getX(), 0, FXGL.getAppWidth()));
        player.setY(Math.clamp(player.getY(), 0, FXGL.getAppHeight()));

        for (Entity bag : new ArrayList<>(bags)) {
            if (player.distance(bag) < 56) {
                if(player.getComponent(ItemComponent.class).getHeldItem() == null){
                    player.getComponent(ItemComponent.class).giveItem(
                            ItemType.values()[(int)(Math.random() * ItemType.values().length)]
                    );
                }

                respawnX += bag.getX();
                respawnY += bag.getY();
                Respawn = true;
                RespawnTimer = 1;
                bag.removeFromWorld();
            }
        }

        for (Entity needle : new ArrayList<>(needles)) {
            if (player.distance(needle) < 48) {
                if (!player.getComponent(ItemComponent.class).getInvincible()) {
                    player.getComponent(EffectComponent.class).spawnBloodEffect();
                    Hit = true;
                    HitTimer = 2;
                    player.rotateBy((int)(Math.random() * 141) - 70);
                    player.getComponent(CarControlComponent.class).setCurrentSpeed(0);
                }
                needle.removeFromWorld();
            }
        }

        for (Entity vomit : new ArrayList<>(vomits)) {
            if (player.distance(vomit) < 56) {
                if(player.getComponent(ItemComponent.class).getInvincible() == false){
                    player.rotateBy((int)(Math.random() * 171) - 85);
                    Hit = true;
                    HitTimer = 0.3;
                    player.getComponent(CarControlComponent.class).setCurrentSpeed(0);
                }
                vomit.removeFromWorld();
            }
        }

        for (Entity bottle : new ArrayList<>(bottles)) {
            if (player.distance(bottle) < 56) {
                if(player.getComponent(ItemComponent.class).getInvincible() == false){
                    player.getComponent(EffectComponent.class).spawnBloodEffect();
                    Hit = true;
                    HitTimer = 2;
                    player.getComponent(CarControlComponent.class).setCurrentSpeed(0);
                }
                bottle.removeFromWorld();
            }
        }
        if(Hit){
            player.getComponent(CarControlComponent.class).setCurrentSpeed(0);
        }
        HitTimer -= tpf;
        if (HitTimer <= 0) {
            Hit = false;
        }

        RespawnTimer -= tpf;
        if (RespawnTimer <= 0 && Respawn == true) {
            Respawn = false;
            FXGL.spawn("Bag", respawnX, respawnY);
            respawnX = 0;
            respawnY = 0;
        }
    }

    public boolean isOnTrack(double x, double y) {
        if (collisionImage == null) return false;

        double px = x * collisionImage.getWidth() / FXGL.getAppWidth();
        double py = y * collisionImage.getHeight() / FXGL.getAppHeight();

        if (px < 0 || py < 0 || px >= collisionImage.getWidth() || py >= collisionImage.getHeight())
            return false;

        Color color = reader.getColor((int) px, (int) py);
        return color.getRed() > 0.9; // Weiß = Straße
    }

    public static void main(String[] args) {
        launch(args);
    }
}