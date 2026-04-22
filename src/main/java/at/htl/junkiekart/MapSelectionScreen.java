package at.htl.junkiekart;

import com.almasb.fxgl.dsl.FXGL;
import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.function.Consumer;

public class MapSelectionScreen extends StackPane{
    public MapSelectionScreen(Consumer<String> onMapSelected){
        // Hintergrund
        Rectangle background = new Rectangle(FXGL.getAppWidth(), FXGL.getAppHeight());
        background.setFill(Color.web("#0f0f0f"));

        // Titel
        Text title = new Text("SELECT MAP");
        title.setFont(Font.font("Arial", 52));
        title.setFill(Color.web("#00ffcc"));
        DropShadow glow = new DropShadow();
        glow.setColor(Color.web("#00ffcc"));
        glow.setRadius(30);
        title.setEffect(glow);

        // Map-Karten
        HBox mapsBox = new HBox(40,
                createMapCard("map1", "Easy",   "#ff6b6b", onMapSelected),
                createMapCard("map2", "Hard",  "#ffd93d", onMapSelected)
                //createMapCard("map3", "Training",  "#00ffcc", onMapSelected)
        );
        mapsBox.setAlignment(Pos.CENTER);

        VBox layout = new VBox(50, title, mapsBox);
        layout.setAlignment(Pos.CENTER);
        layout.setPrefSize(FXGL.getAppWidth(), FXGL.getAppHeight());

        getChildren().addAll(background, layout);
    }

    private VBox createMapCard(String mapId, String mapName, String color,
                               java.util.function.Consumer<String> onMapSelected) {

        var stream = getClass().getResourceAsStream("/assets/textures/maps/" + mapId + ".png");
        var image = new javafx.scene.image.Image(stream);
        var imageView = new javafx.scene.image.ImageView(image);
        imageView.setFitWidth(300);
        imageView.setFitHeight(200);
        imageView.setPreserveRatio(true);

        Rectangle border = new Rectangle(300, 200);
        border.setFill(Color.TRANSPARENT);
        border.setStroke(Color.web(color));
        border.setStrokeWidth(2);
        border.setArcWidth(15);
        border.setArcHeight(15);

        StackPane previewPane = new StackPane(imageView, border);

        Text name = new Text(mapName);
        name.setFont(Font.font("Arial", 22));
        name.setFill(Color.WHITE);

        VBox card = new VBox(15, previewPane, name);
        card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-cursor: hand;");

        card.setOnMouseEntered(e -> {
            border.setStrokeWidth(4);
            DropShadow shadow = new DropShadow();
            shadow.setColor(Color.web(color));
            shadow.setRadius(25);
            card.setEffect(shadow);
            card.setScaleX(1.05);
            card.setScaleY(1.05);
        });
        card.setOnMouseExited(e -> {
            border.setStrokeWidth(2);  // ← border statt preview
            card.setEffect(null);
            card.setScaleX(1.0);
            card.setScaleY(1.0);
        });

        card.setOnMouseClicked(e -> onMapSelected.accept(mapId));

        return card;
    }
}
