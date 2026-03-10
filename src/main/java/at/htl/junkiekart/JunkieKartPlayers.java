package at.htl.junkiekart;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;

public class JunkieKartPlayers implements EntityFactory {

    @Spawns("Player1")
    public Entity newPlayer(SpawnData data) {
        // Manuell laden weil FXGL.texture() nicht funktioniert
        var stream = getClass().getResourceAsStream("/assets/textures/cart.png");
        var image = new javafx.scene.image.Image(stream);
        var imageView = new javafx.scene.image.ImageView(image);

        imageView.setFitWidth(72);
        imageView.setFitHeight(72);
        imageView.setPreserveRatio(true);

        return FXGL.entityBuilder(data)
                .view(imageView)
                .build();
    }

}
