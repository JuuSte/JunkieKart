package at.htl.junkiekart;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import javafx.scene.shape.Rectangle;

import static javafx.scene.paint.Color.RED;

public class JunkieKartEntityFactory implements EntityFactory {

    @Spawns("Player")
    public Entity newPlayer(SpawnData data) {
        String skin = data.get("skin");

        var stream = getClass().getResourceAsStream("/assets/textures/karts/" + skin);
        var image = new javafx.scene.image.Image(stream);
        var imageView = new javafx.scene.image.ImageView(image);

        imageView.setFitWidth(52);
        imageView.setFitHeight(52);
        imageView.setPreserveRatio(true);
        imageView.setRotate(-90);

        return FXGL.entityBuilder(data)
                .type(EntityType.PLAYER)
                .view(imageView)
                .bbox(new HitBox(BoundingShape.box(30, 48)))
                .anchorFromCenter()
                .with(new EffectComponent())
                .with(new SkidMarkComponent())
                .with(new CarControlComponent())
                .with(new ItemComponent())
                .rotate(90)
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
                .with(new BagRespawnComponent(data.getX(), data.getY()))
                .build();
    }

    @Spawns("Nadel")
    public Entity newNadel(SpawnData data) {
        var stream = getClass().getResourceAsStream("/assets/textures/nadel.png");
        var image = new javafx.scene.image.Image(stream);
        var imageView = new javafx.scene.image.ImageView(image);

        imageView.setFitWidth(48);
        imageView.setFitHeight(48);
        imageView.setPreserveRatio(true);

        return FXGL.entityBuilder(data)
                .type(EntityType.NADEL)
                .view(imageView)
                .anchorFromCenter()
                .build();
    }

    @Spawns("Vomit")
    public Entity newVomit(SpawnData data) {
        var stream = getClass().getResourceAsStream("/assets/textures/vomit.png");
        var image = new javafx.scene.image.Image(stream);
        var imageView = new javafx.scene.image.ImageView(image);

        imageView.setFitWidth(72);
        imageView.setFitHeight(72);
        imageView.setPreserveRatio(true);

        return FXGL.entityBuilder(data)
                .type(EntityType.VOMIT)
                .view(imageView)
                .anchorFromCenter()
                .build();
    }

    @Spawns("Beer")
    public Entity newBottle(SpawnData data) {
        var stream = getClass().getResourceAsStream("/assets/textures/bottle.png");
        var image = new javafx.scene.image.Image(stream);
        var imageView = new javafx.scene.image.ImageView(image);

        imageView.setFitWidth(56);
        imageView.setFitHeight(56);
        imageView.setPreserveRatio(true);

        double angleRad = data.hasKey("angleRad") ? (double) data.get("angleRad") : 0.0;

        return FXGL.entityBuilder(data)
                .type(EntityType.BEER)
                .view(imageView)
                .anchorFromCenter()
                .with(new BottleMovementComponent(angleRad))
                .build();
    }

    @Spawns("map1")
    public Entity newMap1(SpawnData data) {
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

    @Spawns("map2")
    public Entity newMap2(SpawnData data) {
        var stream = getClass().getResourceAsStream("/assets/textures/maps/map2.png");
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


    @Spawns("Checkpoint")
    public Entity newCheckpoint(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(EntityType.CHECKPOINT)
                .view(new Rectangle(60, 250, RED))
                .opacity(0.0)
                .anchorFromCenter()
                .collidable()
                .build();
    }
}
