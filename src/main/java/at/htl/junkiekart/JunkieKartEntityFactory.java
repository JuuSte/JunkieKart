package at.htl.junkiekart;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;

public class JunkieKartEntityFactory implements EntityFactory {

    @Spawns("Player")
    public Entity newPlayer(SpawnData data) {
        String skin = data.get("skin");

        var stream = getClass().getResourceAsStream("/assets/textures/karts/" + skin);
        var image = new javafx.scene.image.Image(stream);
        var imageView = new javafx.scene.image.ImageView(image);

        imageView.setFitWidth(48);
        imageView.setFitHeight(48);
        imageView.setPreserveRatio(true);
        imageView.setRotate(-90);

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

        imageView.setFitWidth(56);
        imageView.setFitHeight(56);
        imageView.setPreserveRatio(true);

        return FXGL.entityBuilder(data)
                .type(EntityType.BAG)
                .view(imageView)
                .anchorFromCenter()
                .build();
    }

    @Spawns("Nadel")
    public Entity newNadel(SpawnData data) {
        var stream = getClass().getResourceAsStream("/assets/textures/nadel.png");
        var image = new javafx.scene.image.Image(stream);
        var imageView = new javafx.scene.image.ImageView(image);

        imageView.setFitWidth(56);
        imageView.setFitHeight(56);
        imageView.setPreserveRatio(true);

        return FXGL.entityBuilder(data)
                .type(EntityType.NADEL)
                .view(imageView)
                .anchorFromCenter()
                .build();
    }

    @Spawns("map1")
    public Entity newTestMap(SpawnData data) {
        var stream = getClass().getResourceAsStream("/assets/textures/maps/map1.png");
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
