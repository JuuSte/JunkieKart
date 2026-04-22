package at.htl.junkiekart;

import com.almasb.fxgl.entity.component.Component;

public class BagRespawnComponent extends Component {
    private double RespawnTimer = 3;
    private boolean Respawning = false;
    private double xCoordinate;
    private double yCoordinate;

    public BagRespawnComponent(double xCoordinate, double yCoordinate) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
    }

    public void respawnBag(boolean respawning) {
        Respawning = respawning;
        entity.setX(-200);
        entity.setY(-200);
        RespawnTimer = 3;
    }

    @Override
    public void onUpdate(double tpf) {
        if(Respawning){
            RespawnTimer -= tpf;
        }
        if(RespawnTimer <= 0){
            entity.setPosition(xCoordinate, yCoordinate);
            Respawning = false;
        }
    }
}
