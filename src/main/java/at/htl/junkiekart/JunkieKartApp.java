package at.htl.junkiekart;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import java.util.ArrayList;

import static com.almasb.fxgl.dsl.FXGLForKtKt.entityBuilder;
import static com.almasb.fxgl.dsl.FXGLForKtKt.texture;

public class JunkieKartApp extends GameApplication {

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
                FXGL.spawn("Nadel", 700, 800);
                FXGL.spawn("Nadel", 500, 600);

                CustomizeOverlay[] customize = new CustomizeOverlay[1];
                customize[0] = new CustomizeOverlay(mapId, () -> {
                    FXGL.getGameScene().clearUINodes();
                    FXGL.spawn(mapId);
                    FXGL.spawn("Player", new SpawnData(960, 540).put("skin", customize[0].getSelectedSkin()));
                });

                FXGL.getGameScene().addUINode(customize[0]);
            });

            FXGL.getGameScene().addUINode(mapSelect);
        });

        FXGL.getGameScene().addUINode(loading);
    }

    @Override
    protected void onUpdate(double tpf) {
        var entities = FXGL.getGameWorld().getEntitiesByType(EntityType.PLAYER);
        if (entities.isEmpty()) return;

        var bags = FXGL.getGameWorld().getEntitiesByType(EntityType.BAG);
        var needles = FXGL.getGameWorld().getEntitiesByType(EntityType.NADEL);

        Entity player = entities.get(0);

        player.setX(Math.clamp(player.getX(), 0, FXGL.getAppWidth()));
        player.setY(Math.clamp(player.getY(), 0, FXGL.getAppHeight()));

        for (Entity bag : new ArrayList<>(bags)) {
            if (player.distance(bag) < 56) {
                player.getComponent(ItemComponent.class).giveItem(ItemType.values()[(int)(Math.random() * ItemType.values().length)]);//Random Item geben
                bag.removeFromWorld();
            }
        }

        for (Entity needle : new ArrayList<>(needles)) {
            if (player.distance(needle) < 56) {
                player.getComponent(CarControlComponent.class).setCurrentSpeed(0);
                needle.removeFromWorld();
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}