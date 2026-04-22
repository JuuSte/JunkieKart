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
    private javafx.scene.image.ImageView itemIconView;

    private Image imgKokain;
    private Image imgNadel;
    private Image imgShroom;
    private Image imgBeer;
    private javafx.scene.control.Label lapLabel;

    private void ItemUI() {
        imgKokain = new Image(getClass().getResourceAsStream("/assets/textures/koks.png"));
        imgNadel  = new Image(getClass().getResourceAsStream("/assets/textures/nadels.png"));
        imgShroom = new Image(getClass().getResourceAsStream("/assets/textures/shrooms.png"));
        imgBeer   = new Image(getClass().getResourceAsStream("/assets/textures/beers.png"));

        javafx.scene.shape.Rectangle frame = new javafx.scene.shape.Rectangle(80, 80);
        frame.setFill(javafx.scene.paint.Color.web("#0f0f0f", 0.7));
        frame.setStroke(javafx.scene.paint.Color.web("#00ffcc"));
        frame.setStrokeWidth(2);
        frame.setArcWidth(12);
        frame.setArcHeight(12);

        javafx.scene.control.Label label = new javafx.scene.control.Label("ITEM");
        label.setTextFill(javafx.scene.paint.Color.web("#00ffcc"));
        label.setStyle("-fx-font-size: 11px;");

        itemIconView = new javafx.scene.image.ImageView();
        itemIconView.setFitWidth(56);
        itemIconView.setFitHeight(56);
        itemIconView.setPreserveRatio(true);

        javafx.scene.layout.StackPane iconPane = new javafx.scene.layout.StackPane(frame, itemIconView);
        javafx.scene.layout.VBox itemBox = new javafx.scene.layout.VBox(4, label, iconPane);
        itemBox.setAlignment(javafx.geometry.Pos.CENTER);
        itemBox.setTranslateX(20);
        itemBox.setTranslateY(10);

        lapLabel = new javafx.scene.control.Label("");
        lapLabel.setTextFill(javafx.scene.paint.Color.web("#000000"));
        lapLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        lapLabel.setTranslateX(120);
        lapLabel.setTranslateY(30);
        FXGL.getGameScene().addUINode(lapLabel);

        FXGL.getGameScene().addUINode(itemBox);
    }


    private int respawnX;
    private int respawnY;
    private double RespawnTimer;
    private boolean Respawn;
    private int location = 0;
    private int laps = 2;
    private boolean win = false;

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

                if(mapId.equals("map2")){
                FXGL.spawn("Bag", 600, 250);
                FXGL.spawn("Bag", 700, 780);
                FXGL.spawn("Bag", 1630, 200);
                FXGL.spawn("Bag", 1200, 645);
                FXGL.spawn("Checkpoint", 240, 60);
                FXGL.spawn("Checkpoint", 1400, 460);
                FXGL.spawn("Checkpoint", 600, 620);
}
                CustomizeOverlay[] customize = new CustomizeOverlay[1];
                customize[0] = new CustomizeOverlay(mapId, () -> {
                    FXGL.getGameScene().clearUINodes();
                    FXGL.spawn(mapId);

                    ImageView collisionView = new ImageView(
                            collisionImage = new Image(
                                    getClass().getResourceAsStream("/assets/textures/maps/" + mapId + "_collision.png")
                            )
                    );
                    collisionView.setFitWidth(FXGL.getAppWidth());
                    collisionView.setFitHeight(FXGL.getAppHeight());
                    collisionImage = collisionView.getImage();
                    reader = collisionImage.getPixelReader();


                    ItemUI();                                                // 2.
                    FXGL.spawn("Player", new SpawnData(180, 140)// 3.
                            .put("skin", customize[0].getSelectedSkin()));
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
        var checkpoints = FXGL.getGameWorld().getEntitiesByType(EntityType.CHECKPOINT);

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
        for (Entity checkpoint : new ArrayList<>(checkpoints)) {
            if (player.distance(checkpoint) < 240) {
                if(checkpoints.get(0).equals(checkpoint)){
                    if(location == 2){
                        laps --;
                        if(laps == 0){
                            win = true;
                            System.out.println("You win!");
                        }else{
                            System.out.println("Laps remaining: " + laps);
                        }
                        location = 0;
                        System.out.println("Reset Location = 0");
                    }if(location == 1){
                        location = 0;
                    }
                }if(checkpoints.get(1).equals(checkpoint)){
                    if(location == 0){
                        location = 1;
                        System.out.println("Location = 1");
                    }
                }if(checkpoints.get(2).equals(checkpoint)){
                    if(location == 1){
                        location = 2;
                        System.out.println("Location = 2");
                    }
                }
            }
        }
        if(Hit){
            player.getComponent(CarControlComponent.class).setCurrentSpeed(0);
        }
        HitTimer -= tpf;
        if (HitTimer <= 0) {
            Hit = false;
        }
        if (itemIconView != null && !players.isEmpty()) {
            ItemType held = player.getComponent(ItemComponent.class).getHeldItem();
            if (held == ItemType.Kokain)          itemIconView.setImage(imgKokain);
            else if (held == ItemType.Benutzte_Nadel) itemIconView.setImage(imgNadel);
            else if (held == ItemType.Shroom)     itemIconView.setImage(imgShroom);
            else if (held == ItemType.Beer_Bottle) itemIconView.setImage(imgBeer);
            else                                   itemIconView.setImage(null);
        }

        RespawnTimer -= tpf;
        if (RespawnTimer <= 0 && Respawn == true) {
            Respawn = false;
            FXGL.spawn("Bag", respawnX, respawnY);
            respawnX = 0;
            respawnY = 0;
        }
        if (lapLabel != null) {
            lapLabel.setText("Lap: " + (3 - laps) + "/10");
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