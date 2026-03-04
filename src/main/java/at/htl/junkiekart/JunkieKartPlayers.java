package at.htl.junkiekart;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;

public class JunkieKartPlayers implements EntityFactory {

    @Spawns("Player1")
    public Entity newPlayer(SpawnData data) {
        return FXGL.entityBuilder(data)
                .view(FXGL.texture("cart.png"))
                .with(new CarControlComponent())
                .build();
    }

}
