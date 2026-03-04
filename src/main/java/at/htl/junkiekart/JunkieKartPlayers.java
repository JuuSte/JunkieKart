package at.htl.junkiekart;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import javafx.scene.shape.Rectangle;

public class JunkieKartPlayers implements EntityFactory {

    @Spawns("Player1")
    public Entity newPlayer(SpawnData data) {
        return FXGL.entityBuilder(data)
                .view(new Rectangle(60, 80, javafx.scene.paint.Color.RED))
                .anchorFromCenter()
                .with(new CarControlComponent())
                .build();
    }

}
