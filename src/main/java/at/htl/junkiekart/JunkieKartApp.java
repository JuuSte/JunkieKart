package at.htl.junkiekart;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.input.KeyCode;
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


    private Image imgKokain;
    private Image imgNadel;
    private Image imgShroom;
    private Image imgBeer;


    private ImageView[] itemIconViews;
    private javafx.scene.control.Label[] lapLabels;
    private int playerCount;

    private void ItemUI(int count) {
        playerCount = count;
        // Die item bilder für die item ui laden
        imgKokain = new Image(getClass().getResourceAsStream("/assets/textures/koks.png"));
        imgNadel  = new Image(getClass().getResourceAsStream("/assets/textures/nadels.png"));
        imgShroom = new Image(getClass().getResourceAsStream("/assets/textures/shrooms.png"));
        imgBeer   = new Image(getClass().getResourceAsStream("/assets/textures/beers.png"));

        itemIconViews = new ImageView[count];
        lapLabels     = new javafx.scene.control.Label[count];

        double[] tx = { 20, FXGL.getAppWidth() - 220, 20, FXGL.getAppWidth() - 220 };
        double[] ty = { 10, 10, FXGL.getAppHeight() - 120, FXGL.getAppHeight() - 120 };

        for(int i = 0; i < count; i++) {
            javafx.scene.shape.Rectangle frame = new javafx.scene.shape.Rectangle(80, 80);
            frame.setFill(javafx.scene.paint.Color.web("#0f0f0f", 0.7));
            frame.setStroke(javafx.scene.paint.Color.web("#00ffcc"));
            frame.setStrokeWidth(2);
            frame.setArcWidth(12);
            frame.setArcHeight(12);

            javafx.scene.control.Label label = new javafx.scene.control.Label("ITEM");
            label.setTextFill(javafx.scene.paint.Color.web("#00ffcc"));
            label.setStyle("-fx-font-size: 11px;");

            itemIconViews[i] = new javafx.scene.image.ImageView();
            itemIconViews[i].setFitWidth(56);
            itemIconViews[i].setFitHeight(56);
            itemIconViews[i].setPreserveRatio(true);

            javafx.scene.layout.StackPane iconPane = new javafx.scene.layout.StackPane(frame, itemIconViews[i]);

            //laps
            lapLabels[i] = new javafx.scene.control.Label("Lap: 0/10");
            lapLabels[i].setTextFill(javafx.scene.paint.Color.web("#00ffcc"));
            lapLabels[i].setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

            javafx.scene.layout.VBox box = new javafx.scene.layout.VBox(4, label, iconPane, lapLabels[i]);
            box.setAlignment(javafx.geometry.Pos.CENTER);
            box.setTranslateX(tx[i]);
            box.setTranslateY(ty[i]);

            FXGL.getGameScene().addUINode(box);
        }

        // Position anzeige um zu testen und zum items einfügen (muss später weg)
        javafx.scene.control.Label mousePosLabel = new javafx.scene.control.Label("");
        mousePosLabel.setTextFill(Color.web("#00ffcc"));
        mousePosLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        mousePosLabel.setTranslateX(800);
        mousePosLabel.setTranslateY(10);

        FXGL.getGameScene().getRoot().setOnMouseMoved(e -> {
            mousePosLabel.setText("X: " + (int)e.getX() + "  Y: " + (int)e.getY());
        });

        FXGL.getGameScene().addUINode(mousePosLabel);
    }

    private int location[] = new int[4];
    private int laps[] = new int[4];
    private boolean win = false;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle("Junkie Kart");
        settings.setWidth(1920);
        settings.setHeight(1088);

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

                if(mapId.equals("map1")){
                    FXGL.spawn("Bag", 400, 180);
                    FXGL.spawn("Bag", 1680, 180);
                    FXGL.spawn("Bag", 1200 , 860);
                    FXGL.spawn("Bag", 200, 860);
                    FXGL.spawn("Checkpoint", 880, 70);
                    FXGL.spawn("Checkpoint", 1500, 750);
                    FXGL.spawn("Checkpoint", 320, 750);
                }

                if(mapId.equals("map2")){
                    FXGL.spawn("Bag", 600, 250);
                    FXGL.spawn("Bag", 700, 780);
                    FXGL.spawn("Bag", 1630, 200);
                    FXGL.spawn("Bag", 1200, 645);
                    FXGL.spawn("Checkpoint", 240, 60);
                    FXGL.spawn("Checkpoint", 1400, 460);
                    FXGL.spawn("Checkpoint", 600, 620);
                }

                if(mapId.equals("map3")){
                    FXGL.spawn("Bag", 600, 250);
                    FXGL.spawn("Bag", 700, 780);
                    FXGL.spawn("Bag", 1630, 200);
                    FXGL.spawn("Bag", 1200, 645);
                }

                PlayerSetupScreen setup = new PlayerSetupScreen(configs -> {
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
                    System.out.println("collision size: " + collisionImage.getWidth() + "x" + collisionImage.getHeight());


                    ItemUI(configs.size());

                    for (int i = 0; i < 4; i++) {
                        laps[i] = 10;
                        location[i] = 0;
                    }

                    for (PlayerConfig config : configs) {
                        if(mapId.equals("map1")) {
                            FXGL.spawn("Player", new SpawnData(800 + config.playerIndex * 80, 200).put("config", config));
                        } else if(mapId.equals("map2")) {
                            FXGL.spawn("Player", new SpawnData(80 + config.playerIndex * 80, 170).put("config", config));
                        } else if(mapId.equals("map3")) {
                            FXGL.spawn("Player", new SpawnData(800 + config.playerIndex * 80, 300).put("config", config));
                        }
                        else
                        FXGL.spawn("Player", new SpawnData(800 + config.playerIndex * 80, 200).put("config", config));
                    }

                });
                FXGL.getGameScene().addUINode(setup);
            });
            FXGL.getGameScene().addUINode(mapSelect);
        });
        FXGL.getGameScene().addUINode(loading);
    }

    @Override
    protected void onUpdate(double tpf) {
        var players = FXGL.getGameWorld().getEntitiesByType(EntityType.PLAYER);
        if (players.isEmpty()) return;
        var bags = FXGL.getGameWorld().getEntitiesByType(EntityType.BAG);
        var needles = FXGL.getGameWorld().getEntitiesByType(EntityType.NADEL);
        var bottles = FXGL.getGameWorld().getEntitiesByType(EntityType.BEER);
        var vomits = FXGL.getGameWorld().getEntitiesByType(EntityType.VOMIT);
        var checkpoints = FXGL.getGameWorld().getEntitiesByType(EntityType.CHECKPOINT);

        for (Entity bag : new ArrayList<>(bags)) {
            if (bag.getX() < 0) continue;
            for (Entity player : new ArrayList<>(players)) {
                if (player.distance(bag) < 56) {
                    if (player.getComponent(ItemComponent.class).getHeldItem() == null) {
                        player.getComponent(ItemComponent.class).giveItem(
                                ItemType.values()[(int) (Math.random() * ItemType.values().length)]
                        );
                    }
                    bag.getComponent(BagRespawnComponent.class).respawnBag(true);
                }
            }
        }

        for (Entity needle : new ArrayList<>(needles)) {
            for (Entity player : new ArrayList<>(players)) {
                if (player.distance(needle) < 48) {
                    if (!player.getComponent(ItemComponent.class).getInvincible()) {
                        player.rotateBy((int) (Math.random() * 141) - 70);
                        player.getComponent(EffectComponent.class).spawnBloodEffect();
                        player.getComponent(CarControlComponent.class).setHit(true);
                        HitTimer = 2;
                    }
                    needle.removeFromWorld();
                }
            }

        }

        for (Entity vomit : new ArrayList<>(vomits)) {
            for (Entity player : new ArrayList<>(players)) {
                if (player.distance(vomit) < 56) {
                    if (player.getComponent(ItemComponent.class).getInvincible() == false) {
                        player.rotateBy((int) (Math.random() * 171) - 85);
                        player.getComponent(CarControlComponent.class).setHit(true);
                        HitTimer = 0.2;
                    }
                    vomit.removeFromWorld();
                }
            }
        }

        for (Entity bottle : new ArrayList<>(bottles)) {
            for (Entity player : new ArrayList<>(players)) {
                if (player.distance(bottle) < 56) {
                    if (player.getComponent(ItemComponent.class).getInvincible() == false) {
                        player.rotateBy((int) (Math.random() * 161) - 80);
                        player.getComponent(EffectComponent.class).spawnBloodEffect();
                        player.getComponent(CarControlComponent.class).setHit(true);
                        HitTimer = 2.5;
                    }
                    bottle.removeFromWorld();
                }
            }
        }

        for (Entity checkpoint : new ArrayList<>(checkpoints)) {
            for (int i = 0; i < players.size(); i++) {
                Entity player = players.get(i);
                if (player.distance(checkpoint) < 240) {
                    if (checkpoints.get(0).equals(checkpoint)) {
                        if (location[i] == 2) {
                            laps[i]--;
                            if (laps[i] == 0) {
                                win = true;
                                System.out.println("You win!");
                            }
                            location[i] = 0;
                        }
                        if (location[i] == 1) location[i] = 0;
                    }
                    if (checkpoints.get(1).equals(checkpoint)) {
                        if (location[i] == 0) location[i] = 1;
                    }
                    if (checkpoints.get(2).equals(checkpoint)) {
                        if (location[i] == 1) location[i] = 2;
                    }
                }
            }
        }
        HitTimer -= tpf;
        for (int i = 0; i < players.size(); i++) {
            Entity p = players.get(i);

            if (HitTimer <= 0) {
                p.getComponent(CarControlComponent.class).setHit(false);
            }

            if (itemIconViews != null) {
                ItemType held = p.getComponent(ItemComponent.class).getHeldItem();
                if (held == ItemType.Kokain) itemIconViews[i].setImage(imgKokain);
                else if (held == ItemType.Benutzte_Nadel) itemIconViews[i].setImage(imgNadel);
                else if (held == ItemType.Shroom) itemIconViews[i].setImage(imgShroom);
                else if (held == ItemType.Beer_Bottle) itemIconViews[i].setImage(imgBeer);
                else itemIconViews[i].setImage(null);
            }

            if (lapLabels != null) {
                lapLabels[i].setText("Lap: " + (10 - laps[i]) + "/10");
            }
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