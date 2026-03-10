package at.htl.junkiekart;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;

import static com.almasb.fxgl.dsl.FXGLForKtKt.entityBuilder;
import static com.almasb.fxgl.dsl.FXGLForKtKt.texture;

public class JunkieKartApp extends GameApplication {

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle("Junkie Kart");
        settings.setWidth(1280);
        settings.setWidth(1920);

        settings.setMainMenuEnabled(true);    //Menü aktivieren
        settings.setSceneFactory(new JunkieKartSceneFactory());
    }

    @Override
    protected void initGame() {
        FXGL.getGameWorld().addEntityFactory(new JunkieKartPlayers());

        // Auf den JavaFX Thread warten
        javafx.application.Platform.runLater(() -> {
            FXGL.spawn("Player1", 400, 300);
        });
    }
    public static void main(String[] args) {
        launch(args);
    }
}