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
                .with(new ItemComponent())
                .build();
    }

    @Spawns("Bag")
    public Entity newBag(SpawnData data) {
        var stream = getClass().getResourceAsStream("/assets/textures/bag.png");
        var image = new javafx.scene.image.Image(stream);
        var imageView = new javafx.scene.image.ImageView(image);

        imageView.setFitWidth(64);
        imageView.setFitHeight(64);
        imageView.setPreserveRatio(true);

        return FXGL.entityBuilder(data)
                .type(EntityType.BAG)
                .view(imageView)
                .anchorFromCenter()
                .build();
    }

    @Spawns("map1")
    public Entity newTestMap(SpawnData data) {
        var stream = getClass().getResourceAsStream("/assets/textures/maps/testmap.png");
        var image = new javafx.scene.image.Image(stream);
        var imageView = new javafx.scene.image.ImageView(image);
        imageView.setFitWidth(FXGL.getAppWidth());
        imageView.setFitHeight(FXGL.getAppHeight());

        return FXGL.entityBuilder(data)
                .type(EntityType.MAP)
                .at(0, 0)
                .view(imageView)
                .zIndex(-1)
                .build();
    }

}
