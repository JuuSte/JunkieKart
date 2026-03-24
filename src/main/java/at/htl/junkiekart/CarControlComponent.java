package at.htl.junkiekart;

import com.almasb.fxgl.core.Inject;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.particle.ParticleEmitter;
import javafx.scene.input.KeyCode;

import static com.almasb.fxgl.dsl.FXGL.getInput;

public class CarControlComponent extends Component {
    private double currentSpeed = 0;

    private double maxSpeed = 12;
    private double acceleration = 1;
    private double turnSpeed = 5;
    private double drift = 0.95;
    private double friction = 0.93;
    private double driftFriction = 0.98;

    private double dx = 0;
    private double dy = 0;

    private boolean turnLeft = false;
    private boolean turnRight = false;
    private boolean drifting = false;

    @Override
    public void onAdded() {

        getInput().addAction(new UserAction("Accelerate") {
            @Override
            protected void onAction() {
                currentSpeed += acceleration;
            }
        }, KeyCode.W);

        getInput().addAction(new UserAction("Brake") {
            @Override
            protected void onAction() {
                currentSpeed -= acceleration;
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
            protected void onAction() {
                drifting = true;
                entity.getComponent(smokeEffectComponent.class).setActive(true);
            }
            protected void onActionEnd() {
                drifting = false;
            }
        }, KeyCode.SPACE);
    }

    @Override
    public void onUpdate(double tpf) {
        double rotationAmount = turnSpeed * Math.abs(currentSpeed) / 20.0;

        if (turnLeft)  entity.rotateBy(-rotationAmount);
        if (turnRight) entity.rotateBy(rotationAmount);

        double angleRad = Math.toRadians(entity.getRotation());
        double targetDx = Math.sin(angleRad) * currentSpeed;
        double targetDy = -Math.cos(angleRad) * currentSpeed;

        double dot = (dx * targetDx + dy * targetDy) /
                (Math.sqrt(dx*dx + dy*dy) * Math.sqrt(targetDx*targetDx + targetDy*targetDy) + 0.001);
        if (drifting || dot < 0.99) {
            dx = dx * drift + targetDx * (1 - drift);
            dy = dy * drift + targetDy * (1 - drift);
            entity.getComponent(SkidMarkComponent.class).setActive(true);
        } else {
            dx = targetDx;
            dy = targetDy;
            entity.getComponent(SkidMarkComponent.class).setActive(false);
            entity.getComponent(smokeEffectComponent.class).setActive(false);
        }

        entity.translate(dx, dy);
        currentSpeed *= drifting ? driftFriction : friction;

        double actualSpeed = Math.sqrt(dx * dx + dy * dy);
        if (actualSpeed > maxSpeed) {
            dx *= maxSpeed / actualSpeed;
            dy *= maxSpeed / actualSpeed;
        }

        if (currentSpeed > maxSpeed) {
            currentSpeed = currentSpeed * 0.98 + maxSpeed * 0.02;
        }
        if (currentSpeed < -maxSpeed / 3) {
            currentSpeed = currentSpeed * 0.98 + (-maxSpeed / 3) * 0.02;
        }
    }

    //Für ItemComponent
    public double getMaxSpeed() {
        return maxSpeed;
    }
    public void setMaxSpeed(double newMax) {
        maxSpeed = newMax;
    }

    public double getCurrentSpeed() {
        return currentSpeed;
    }
    public void setCurrentSpeed(double newCurrent) {
        currentSpeed = newCurrent;
    }
}