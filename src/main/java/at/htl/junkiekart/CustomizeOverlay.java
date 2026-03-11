package at.htl.junkiekart;

import com.almasb.fxgl.dsl.FXGL;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class CustomizeOverlay extends StackPane {

    //in assets/textures/karts/
    private static final String[] SKINS = {
            "4x4_white.png",
            "cabrio_red.png",
            "minicar_black.png",
            "minicar_blue.png",
            "pickup_gray.png"
    };


    private int selectedIndex = 0;
    private ImageView previewView;

    public CustomizeOverlay(String mapId, Runnable onDone) {

        Rectangle background = new Rectangle(FXGL.getAppWidth() / 2.0, FXGL.getAppHeight() / 2.0);
        background.setFill(Color.web("#0f0f0f", 0.75)); //
        background.setArcWidth(20);
        background.setArcHeight(20);

        //Titel
        Text title = new Text("CUSTOMIZE KART");
        title.setFont(Font.font("Arial", 52));
        title.setFill(Color.web("#00ffcc"));
        DropShadow glow = new DropShadow();
        glow.setColor(Color.web("#00ffcc"));
        glow.setRadius(30);
        title.setEffect(glow);

        // Vorschau
        previewView = new ImageView();
        previewView.setFitWidth(150);
        previewView.setFitHeight(150);
        previewView.setPreserveRatio(true);
        updatePreview();

        // Pfeile zum Durchschalten
        Button leftBtn = createArrowButton("◀");
        leftBtn.setOnAction(e -> {
            selectedIndex = (selectedIndex - 1 + SKINS.length) % SKINS.length;
            updatePreview();
        });

        Button rightBtn = createArrowButton("▶");
        rightBtn.setOnAction(e -> {
            selectedIndex = (selectedIndex + 1) % SKINS.length;
            updatePreview();
        });
        HBox skinSelector = new HBox(30, leftBtn, previewView, rightBtn);
        skinSelector.setAlignment(Pos.CENTER);

        Text skinName = new Text();
        skinName.setFill(Color.WHITE);
        skinName.setFont(Font.font(20));

        Button doneBtn = createButton("DONE");
        doneBtn.setOnAction(e -> onDone.run());

        VBox layout = new VBox(40, title, skinSelector, doneBtn);
        layout.setAlignment(Pos.CENTER);
        layout.setPrefSize(FXGL.getAppWidth() / 2.0, FXGL.getAppHeight() / 2.0);

        setAlignment(Pos.TOP_LEFT);
        getChildren().addAll(background, layout);
    }

    private void updatePreview() {
        var stream = getClass().getResourceAsStream("/assets/textures/karts/" + SKINS[selectedIndex]);
        if (stream != null) {
            previewView.setImage(new javafx.scene.image.Image(stream));
        }

    }

    public String getSelectedSkin() {
        return SKINS[selectedIndex];
    }

    private Button createArrowButton(String text) {
        Button btn = new Button(text);
        btn.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #00ffcc;" +
                        "-fx-font-size: 36px;"
        );
        return btn;
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
        button.setOnMouseEntered(e -> button.setStyle(
                "-fx-background-color: #00ffcc;" +
                        "-fx-text-fill: black;" +
                        "-fx-font-size: 20px;" +
                        "-fx-padding: 10 40 10 40;"
        ));
        button.setOnMouseExited(e -> button.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-border-color: #00ffcc;" +
                        "-fx-border-width: 2px;" +
                        "-fx-text-fill: #00ffcc;" +
                        "-fx-font-size: 20px;" +
                        "-fx-padding: 10 40 10 40;"
        ));
        return button;
    }
}