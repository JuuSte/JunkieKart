package at.htl.junkiekart;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;

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

                CustomizeOverlay[] customize = new CustomizeOverlay[1];
                customize[0] = new CustomizeOverlay(mapId, () -> {
                    FXGL.getGameScene().clearUINodes();
                    FXGL.spawn(mapId);
                    FXGL.spawn("Player1", new SpawnData(960, 540).put("skin", customize[0].getSelectedSkin()));
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

        Entity player = entities.get(0);

        double x = player.getX();
        double y = player.getY();
        double w = FXGL.getAppWidth();
        double h = FXGL.getAppHeight();

        if (x < 0) player.setX(0);
        if (y < 0) player.setY(0);
        if (x > w) player.setX(w);
        if (y > h) player.setY(h);
    }
    public static void main(String[] args) {
        launch(args);
    }
}