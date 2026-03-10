package at.htl.junkiekart;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.input.UserAction;
import javafx.scene.input.KeyCode;

import static com.almasb.fxgl.dsl.FXGL.getInput;

public class CarControlComponent extends Component {
    private double currentSpeed = 0;

    private double maxSpeed = 20;
    private double acceleration = 1;
    private double turnSpeed = 5;
    private double drift = 0.95;
    private double friction = 0.93;
    private double driftFriction = 0.98;
    private double reGrip = 0.85; // how fast velocity snaps back after drift (lower = slower re-grip)

    private double dx = 0;
    private double dy = 0;

    private boolean turnLeft = false;
    private boolean turnRight = false;
    private boolean drifting = false;
    private boolean reGripping = false;

    @Override
    public void onAdded() {

        getInput().addAction(new UserAction("Accelerate") {
            @Override
            protected void onAction() {
                currentSpeed += acceleration;
                if (currentSpeed > maxSpeed)
                    currentSpeed = maxSpeed;
            }
        }, KeyCode.W);

        getInput().addAction(new UserAction("Brake") {
            @Override
            protected void onAction() {
                currentSpeed -= acceleration;
                if (currentSpeed < -maxSpeed / 3)
                    currentSpeed = -maxSpeed / 3;
            }
        }, KeyCode.S);

        getInput().addAction(new UserAction("Turn Left") {
            @Override
            protected void onActionBegin() { turnLeft = true; }
            @Override
            protected void onActionEnd() { turnLeft = false; }
        }, KeyCode.A);

        getInput().addAction(new UserAction("Turn Right") {
            @Override
            protected void onActionBegin() { turnRight = true; }
            @Override
            protected void onActionEnd() { turnRight = false; }
        }, KeyCode.D);

        getInput().addAction(new UserAction("Drift") {
            @Override
            protected void onActionBegin() {
                double angleRad = Math.toRadians(entity.getRotation());
                dx = Math.sin(angleRad) * currentSpeed;
                dy = -Math.cos(angleRad) * currentSpeed;
                drifting = true;
                reGripping = false;
            }
            @Override
            protected void onActionEnd() {
                drifting = false;
                reGripping = true; // start gradual re-grip instead of snapping
            }
        }, KeyCode.SPACE);
    }

    @Override
    public void onUpdate(double tpf) {
        double rotationAmount = turnSpeed * currentSpeed / maxSpeed;

        if (turnLeft)  entity.rotateBy(-rotationAmount);
        if (turnRight) entity.rotateBy(rotationAmount);

        double angleRad = Math.toRadians(entity.getRotation());
        double targetDx = Math.sin(angleRad) * currentSpeed;
        double targetDy = -Math.cos(angleRad) * currentSpeed;

        if (drifting) {
            // Driften berrechnen
            dx = dx * drift + targetDx * (1 - drift);
            dy = dy * drift + targetDy * (1 - drift);
        } else if (reGripping) {
            // Re Gripen
            dx = dx * (1 - reGrip) + targetDx * reGrip;
            dy = dy * (1 - reGrip) + targetDy * reGrip;

            // wieder gerade aus nur
            double diffX = Math.abs(dx - targetDx);
            double diffY = Math.abs(dy - targetDy);
            if (diffX < 0.1 && diffY < 0.1) {
                reGripping = false;
            }
        } else {
            dx = targetDx;
            dy = targetDy;
        }

        entity.translate(dx, dy);
        currentSpeed *= drifting ? driftFriction : friction;
    }
}