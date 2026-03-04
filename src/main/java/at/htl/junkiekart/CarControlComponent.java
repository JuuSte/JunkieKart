package at.htl.junkiekart;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.input.UserAction;
import javafx.scene.input.KeyCode;

import static com.almasb.fxgl.dsl.FXGL.getInput;

public class CarControlComponent extends Component {
    private double currentSpeed = 0;

    private double maxSpeed = 10;
    private double acceleration = 0.19;
    private double turnSpeed = 5; // degrees per frame scaled by speed
    private double drift = 0.95;

    private boolean turnLeft = false;
    private boolean turnRight = false;

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
    }

    @Override
    public void onUpdate(double tpf) {
        // Rotate based only on input, not speed sign
        double rotationAmount = turnSpeed * (Math.abs(currentSpeed) / maxSpeed);
        if (turnLeft)  entity.rotateBy(-rotationAmount);
        if (turnRight) entity.rotateBy(rotationAmount);

        // Move forward/backward in direction facing
        double angleRad = Math.toRadians(entity.getRotation());
        double dx = Math.sin(angleRad) * currentSpeed;
        double dy = -Math.cos(angleRad) * currentSpeed;
        entity.translate(dx, dy);

        // Apply friction
        currentSpeed *= drift;
    }
}