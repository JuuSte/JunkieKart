package at.htl.junkiekart;

import com.almasb.fxgl.dsl.FXGL;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class LoadingScreen extends StackPane {

    public LoadingScreen(Runnable onComplete) {

        Rectangle background = new Rectangle(FXGL.getAppWidth(), FXGL.getAppHeight());
        background.setFill(Color.web("#1e1e1e")); // dunkelgrau


        Rectangle progressBar = new Rectangle(0, 30); // beginnt bei 0
        progressBar.setFill(Color.web("#00ffea"));
        progressBar.setArcWidth(15);
        progressBar.setArcHeight(15);

        Label loadingText = new Label("Loading...");
        loadingText.setTextFill(Color.WHITE);
        loadingText.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        VBox box = new VBox(20, loadingText, progressBar);
        box.setAlignment(Pos.CENTER);

        getChildren().addAll(background, box);

        // Animation des Fortschritts
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressBar.widthProperty(), 0)),
                new KeyFrame(Duration.seconds(0.2), new KeyValue(progressBar.widthProperty(), 400))
        );

        timeline.setOnFinished(e -> onComplete.run());
        timeline.play();
    }
}