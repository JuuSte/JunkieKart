package at.htl.junkiekart;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;

public class JunkieKartEntityFactory implements EntityFactory {

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
                .type(EntityType.PLAYER)
                .view(imageView)
                .anchorFromCenter()
                .with(new CarControlComponent())
                .build();
    }

}
