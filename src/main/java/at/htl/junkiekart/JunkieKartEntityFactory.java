package at.htl.junkiekart;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;

public class JunkieKartEntityFactory implements EntityFactory {

    @Spawns("Player1")
    public Entity newPlayer(SpawnData data) {
        String skin = data.get("skin");

        var stream = getClass().getResourceAsStream("/assets/textures/karts/" + skin);
        var image = new javafx.scene.image.Image(stream);
        var imageView = new javafx.scene.image.ImageView(image);

        imageView.setFitWidth(72);
        imageView.setFitHeight(72);
        imageView.setPreserveRatio(true);
        imageView.setRotate(-90);

        return FXGL.entityBuilder(data)
                .type(EntityType.PLAYER)
                .view(imageView)
                .anchorFromCenter()
                .with(new CarControlComponent())
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
