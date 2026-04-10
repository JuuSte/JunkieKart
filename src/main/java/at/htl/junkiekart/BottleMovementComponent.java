package at.htl.junkiekart;

import com.almasb.fxgl.entity.component.Component;

public class BottleMovementComponent extends Component {
    private double dx;
    private double dy;
    private double speed = 20.0;

    public BottleMovementComponent(double angleRad) {
        dx = Math.sin(angleRad) * speed;
        dy = -Math.cos(angleRad) * speed;
    }

    @Override
    public void onUpdate(double tpf) {
        entity.translate(dx, dy);
    }
}