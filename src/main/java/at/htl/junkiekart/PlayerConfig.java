package at.htl.junkiekart;

import javafx.scene.input.KeyCode;

public class PlayerConfig {
    public final int playerIndex;
    public final KeyCode keyUp;
    public final KeyCode keyDown;
    public final KeyCode keyLeft;
    public final KeyCode keyRight;
    public final KeyCode keyDrift;
    public final KeyCode keyItem;
    public final String defaultSkin;

    public PlayerConfig(int playerIndex1, KeyCode keyUp, KeyCode keyDown, KeyCode keyLeft, KeyCode keyRight, KeyCode keyDrift, KeyCode keyItem, String defaultSkin1) {
        this.playerIndex = playerIndex1;
        this.keyUp = keyUp;
        this.keyDown = keyDown;
        this.keyLeft = keyLeft;
        this.keyRight = keyRight;
        this.keyDrift = keyDrift;
        this.keyItem = keyItem;
        this.defaultSkin = defaultSkin1;
    }
}
