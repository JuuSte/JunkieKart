package at.htl.junkiekart;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import static com.almasb.fxgl.dsl.FXGL.*;

public class JunkieKartMenu extends FXGLMenu {

    public JunkieKartMenu() {
        super(MenuType.MAIN_MENU);

        // Hintergrund
        getContentRoot().setStyle(
                "-fx-background-color: linear-gradient(to bottom, #0f0f0f, #1a1a1a);"
        );

        // Titel
        Text title = new Text("JUNKIE KART");
        title.setFont(Font.font("Arial", 72));
        title.setFill(Color.web("#00ffcc"));

        DropShadow glow = new DropShadow();
        glow.setColor(Color.web("#00ffcc"));
        glow.setRadius(40);
        title.setEffect(glow);

        Button startBtn = createButton("START GAME");
        startBtn.setOnAction(e -> fireNewGame());

        Button exitBtn = createButton("EXIT");
        exitBtn.setOnAction(e -> getGameController().exit());

        VBox menuBox = new VBox(30, title, startBtn, exitBtn);
        menuBox.setAlignment(Pos.CENTER);

        // GANZ WICHTIG:
        menuBox.setPrefWidth(getAppWidth());
        menuBox.setPrefHeight(getAppHeight());

        getContentRoot().getChildren().add(menuBox);
    }

    private Button createButton(String text) {

        Button button = new Button(text);

        button.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-border-color: #00ffcc;" +
                        "-fx-border-width: 2px;" +
                        "-fx-text-fill: #00ffcc;" +
                        "-fx-font-size: 20px;" +
                        "-fx-padding: 10 40 10 40;"
        );

        button.setOnMouseEntered(e ->
                button.setStyle(
                        "-fx-background-color: #00ffcc;" +
                                "-fx-text-fill: black;" +
                                "-fx-font-size: 20px;" +
                                "-fx-padding: 10 40 10 40;"
                )
        );

        button.setOnMouseExited(e ->
                button.setStyle(
                        "-fx-background-color: transparent;" +
                                "-fx-border-color: #00ffcc;" +
                                "-fx-border-width: 2px;" +
                                "-fx-text-fill: #00ffcc;" +
                                "-fx-font-size: 20px;" +
                                "-fx-padding: 10 40 10 40;"
                )
        );

        return button;
    }
}