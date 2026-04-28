package at.htl.junkiekart;

import com.almasb.fxgl.dsl.FXGL;
import at.htl.junkiekart.PlayerConfig;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PlayerSetupScreen extends StackPane {

    private static final String[] SKINS = {
            "Kart1.png",
            "Kart2.png",
            "Kart3.png",
            "Kart4.png",
            "Kart5.png",
            "Kart6.png",
            "Kart7.png",
            "Kart8.png",
            "Kart9.png"
    };

    private static final KeyCode[][] KEYBINDS = {
            { KeyCode.W, KeyCode.S, KeyCode.A, KeyCode.D, KeyCode.SPACE, KeyCode.E },
            { KeyCode.UP, KeyCode.DOWN, KeyCode.LEFT, KeyCode.RIGHT, KeyCode.NUMPAD0, KeyCode.NUMPAD1 },
            { KeyCode.T, KeyCode.G, KeyCode.F, KeyCode.H, KeyCode.Z, KeyCode.U },
            { KeyCode.I, KeyCode.K, KeyCode.J, KeyCode.L, KeyCode.O, KeyCode.P }
    };

   private static final Paint[] COLORS = { Paint.valueOf("#e24b4a"), Paint.valueOf("#378add"), Paint.valueOf("#1d9e75"), Paint.valueOf("#ef9f27")};



    private final int[] skinIndex = { 0, 1, 2, 3 };

    //preview der Cards
    private final ImageView[] previews = new ImageView[4];

    //playercards
    private final VBox[] cards = new VBox[4];

    private int playerCount = 1;

    public PlayerSetupScreen(Consumer<List<PlayerConfig>> onDone) {

        // backgound
        Rectangle bg = new Rectangle(FXGL.getAppWidth(), FXGL.getAppHeight());
        bg.setFill(Color.web("#0f0f0f"));

        Text title = new Text("PLAYER SETUP");
        title.setFont(Font.font("Arial", 52));
        title.setFill(Color.web("#00ffcc"));
        DropShadow glow = new DropShadow();
        glow.setColor(Color.web("#00ffcc"));
        glow.setRadius(30);
        title.setEffect(glow);

        // players
        Text countLabel = new Text("HOW MANY PLAYERS?");
        countLabel.setFont(Font.font("Arial", 22));
        countLabel.setFill(Color.WHITE);

        Button[] countBtns = new Button[4];
        HBox countRow = new HBox(16);
        countRow.setAlignment(Pos.CENTER);

        for (int i = 0; i < 4; i++) {
            final int n = i + 1;
            countBtns[i] = makeButton(String.valueOf(n));
            countBtns[i].setOnAction(e -> {
                playerCount = n;
                for (int j = 0; j < 4; j++) {
                    cards[j].setOpacity(j < playerCount ? 1.0 : 0.3);
                    cards[j].setDisable(j >= playerCount);
                }
                for (Button b : countBtns) b.setStyle(btnStyle(false));
                countBtns[n - 1].setStyle(btnStyle(true));
            });
            countRow.getChildren().add(countBtns[i]);
        }
        countBtns[0].setStyle(btnStyle(true)); //P1 immer aktiv

        //playercards
        HBox cardsRow = new HBox(24);
        cardsRow.setAlignment(Pos.CENTER);

        for (int i = 0; i < 4; i++) {
            cards[i] = makePlayerCard(i);
            cardsRow.getChildren().add(cards[i]);
        }
        //cards 2-4 ausgegraut
        for (int i = 1; i < 4; i++) {
            cards[i].setOpacity(0.3);
            cards[i].setDisable(true);
        }

        Button startBtn = makeButton("START GAME");
        startBtn.setOnAction(e -> {
            List<PlayerConfig> configs = new ArrayList<>();
            for (int i = 0; i < playerCount; i++) {
                KeyCode[] k = KEYBINDS[i];
                configs.add(new PlayerConfig(i, k[0], k[1], k[2], k[3], k[4], k[5], SKINS[skinIndex[i]]));
            }
            onDone.accept(configs);
        });

        VBox layout = new VBox(30, title, countLabel, countRow, cardsRow, startBtn);
        layout.setAlignment(Pos.CENTER);
        layout.setPrefSize(FXGL.getAppWidth(), FXGL.getAppHeight());

        getChildren().addAll(bg, layout);
    }

    private VBox makePlayerCard(int i) {
        Text playerTitle = new Text("PLAYER " + (i + 1));
        playerTitle.setFont(Font.font("Arial", 20));
        playerTitle.setFill(Color.web(COLORS[i].toString()));

        ImageView preview = new ImageView();
        preview.setFitWidth(100);
        preview.setFitHeight(100);
        preview.setPreserveRatio(true);
        previews[i] = preview;
        loadSkin(i);

        Button left  = makeArrowButton("◀");
        Button right = makeArrowButton("▶");
        left.setOnAction(e  -> { skinIndex[i] = (skinIndex[i] - 1 + SKINS.length) % SKINS.length; loadSkin(i); });
        right.setOnAction(e -> { skinIndex[i] = (skinIndex[i] + 1) % SKINS.length; loadSkin(i); });

        HBox arrows = new HBox(16, left, right);
        arrows.setAlignment(Pos.CENTER);

        VBox card = new VBox(12, playerTitle, preview, arrows);
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(180);
        card.setStyle(
                "-fx-background-color: #1a1a1a;" +
                        "-fx-border-color: " + COLORS[i] + ";" +
                        "-fx-border-width: 2px;" +
                        "-fx-border-radius: 12px;" +
                        "-fx-background-radius: 12px;" +
                        "-fx-padding: 20px;"
        );
        return card;
    }

    private void loadSkin(int i) {
        var stream = getClass().getResourceAsStream("/assets/textures/karts/" + SKINS[skinIndex[i]]);
        if (stream != null) previews[i].setImage(new Image(stream));
    }

    private Button makeArrowButton(String text) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #00ffcc; -fx-font-size: 20px;");
        return btn;
    }

    private Button makeButton(String text) {
        Button btn = new Button(text);
        btn.setStyle(btnStyle(false));
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #00ffcc; -fx-text-fill: black; -fx-font-size: 20px; -fx-padding: 10 40 10 40;"));
        btn.setOnMouseExited(e  -> btn.setStyle(btnStyle(false)));
        return btn;
    }

    private String btnStyle(boolean active) {
        if (active) return "-fx-background-color: #00ffcc; -fx-text-fill: black; -fx-font-size: 20px; -fx-padding: 10 40 10 40;";
        return "-fx-background-color: transparent; -fx-border-color: #00ffcc; -fx-border-width: 2px; -fx-text-fill: #00ffcc; -fx-font-size: 20px; -fx-padding: 10 40 10 40;";
    }
}
