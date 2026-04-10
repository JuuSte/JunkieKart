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

    private void ItemUI() {
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

        //oben rechts
        itemBox.setTranslateX(FXGL.getAppWidth() - 110);
        itemBox.setTranslateY(20);

        FXGL.getGameScene().addUINode(itemBox);

        FXGL.getWorldProperties()
                .addListener("heldItem", (oldVal, newVal) -> {
                    System.out.println("Listener gefeuert: " + newVal); // DEBUG
                    updateItemIcon(newVal.toString());
                });


        updateItemIcon("none");
    }
    private void updateItemIcon(String held) {
        System.out.println("updateItemIcon called: " + held); // DEBUG
        String texturePath = switch (held) {
            case "Kokain"         -> "/assets/textures/koks.png";
            case "Benutzte_Nadel" -> "/assets/textures/nadels.png";
            case "Shroom"         -> "/assets/textures/shrooms.png";
            case "Beer_Bottle"    -> "/assets/textures/beers.png";
            default               -> null;
        };

        System.out.println("texturePath: " + texturePath); // DEBUG

        if (texturePath != null) {
            var stream = getClass().getResourceAsStream(texturePath);
            System.out.println("stream: " + stream); // DEBUG — null = Datei nicht gefunden!
            if (stream != null) {
                itemIconView.setImage(new javafx.scene.image.Image(stream));
                return;
            }
        }
        itemIconView.setImage(null);
    }


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

                FXGL.spawn("Bag", 600, 300);
                FXGL.spawn("Bag", 200, 500);
                FXGL.spawn("Bag", 800, 200);
                FXGL.spawn("Bag", 600, 800);
                FXGL.spawn("Nadel", 700, 800);
                FXGL.spawn("Nadel", 500, 600);
                FXGL.spawn("Vomit", 1200, 600);
                FXGL.spawn("Vomit",1400, 400);

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
                    FXGL.getWorldProperties().setValue("heldItem", "none");

                    FXGL.spawn("Player", new SpawnData(200, 540).put("skin", customize[0].getSelectedSkin()));

                    ItemUI();


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
        var bottles = FXGL.getGameWorld().getEntitiesByType(EntityType.BOTTLE);
        var vomits = FXGL.getGameWorld().getEntitiesByType(EntityType.VOMIT);

        player.setX(Math.clamp(player.getX(), 0, FXGL.getAppWidth()));
        player.setY(Math.clamp(player.getY(), 0, FXGL.getAppHeight()));

        for (Entity bag : new ArrayList<>(bags)) {
            if (player.distance(bag) < 56) {
                player.getComponent(ItemComponent.class).giveItem(
                        ItemType.values()[(int)(Math.random() * ItemType.values().length)]
                );
                bag.removeFromWorld();
            }
        }

        for (Entity needle : new ArrayList<>(needles)) {
            if (player.distance(needle) < 56) {
                if (!player.getComponent(ItemComponent.class).getInvincible()) {
                    player.getComponent(EffectComponent.class).spawnBloodEffect();
                    Hit = true;
                    HitTimer = 0.1;
                    player.rotateBy(-10);
                    player.getComponent(CarControlComponent.class).setCurrentSpeed(0);
                }
                needle.removeFromWorld();
            }
        }

        for (Entity vomit : new ArrayList<>(vomits)) {
            if (player.distance(vomit) < 56) {
                if(player.getComponent(ItemComponent.class).getInvincible() == false){
                    player.rotateBy(70);
                    Hit = true;
                    HitTimer = 2;
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