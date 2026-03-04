package at.htl.junkiekart;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.input.UserAction;
import javafx.scene.input.KeyCode;

import static com.almasb.fxgl.dsl.FXGL.getInput;

public class CarControlComponent extends Component {
    private double currentSpeed = 0;

    private double maxSpeed = 10;
    private double acceleration = 1;
    private double turnSpeed = 5; // degrees per frame scaled by speed
    private double drift = 0.95;
    private double friction = 0.93; //darf nicht >= 1 sein

    private double dx = 0;
    private double dy = 0;

    private boolean turnLeft = false;
    private boolean turnRight = false;
    private boolean drifting = false;

    @Override
    public void onAdded() {

        // Acceleration
        getInput().addAction(new UserAction("Accelerate") {
            @Override
            protected void onAction() {
                currentSpeed += acceleration;
                if (currentSpeed > maxSpeed)
                    currentSpeed = maxSpeed;
            }
        }, KeyCode.W);

        // Brake / reverse
        getInput().addAction(new UserAction("Brake") {
            @Override
            protected void onAction() {
                currentSpeed -= acceleration;
                if (currentSpeed < -maxSpeed / 3)
                    currentSpeed = -maxSpeed / 3;
            }
        }, KeyCode.S);

        // Turn left
        getInput().addAction(new UserAction("Turn Left") {
            @Override
            protected void onActionBegin() { turnLeft = true; }
            @Override
            protected void onActionEnd() { turnLeft = false; }
        }, KeyCode.A);

        // Turn right
        getInput().addAction(new UserAction("Turn Right") {
            @Override
            protected void onActionBegin() { turnRight = true; }
            @Override
            protected void onActionEnd() { turnRight = false; }
        }, KeyCode.D);

        getInput().addAction(new UserAction("Drift") {
            @Override
            protected void onActionBegin() { drifting = true; }
            @Override
            protected void onActionEnd() { drifting = false; }
        }, KeyCode.SPACE);
    }

    @Override
    public void onUpdate(double tpf) {
        // Preserve speed sign so reverse turning mirrors forward turning
        double speedFactor = currentSpeed / maxSpeed; // ranges from -1/3 to 1.0
        double rotationAmount = turnSpeed * speedFactor; // signed!

        if (turnLeft)  entity.rotateBy(-rotationAmount);
        if (turnRight) entity.rotateBy(rotationAmount);

        double angleRad = Math.toRadians(entity.getRotation());
        if(drifting){
            dx = dx * (1 - drift) + entity.getRotation() * drift;
            dy = dy * (1 - drift) + entity.getRotation() * drift;
        }else{
            dx = Math.cos(angleRad) * currentSpeed;
            dy = -Math.sin(angleRad) * currentSpeed;
        }
        entity.translate(dx, dy);

        // Apply friction
        currentSpeed *= friction;
    }
}