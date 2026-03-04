package at.htl.junkiekart;

import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.app.scene.FXGLMenu;

public class JunkieKartSceneFactory extends SceneFactory {

    @Override
    public FXGLMenu newMainMenu() {
        return new JunkieKartMenu();
    }
}