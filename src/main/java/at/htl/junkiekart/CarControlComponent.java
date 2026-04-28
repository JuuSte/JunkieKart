package at.htl.junkiekart;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.input.UserAction;
import javafx.scene.input.KeyCode;

import static com.almasb.fxgl.dsl.FXGL.getInput;

public class CarControlComponent extends Component {

    private final PlayerConfig config;

    public CarControlComponent(PlayerConfig config) {
        this.config = config;
    }

    private double currentSpeed = 0;

    private double maxSpeed = 12;
    private double acceleration = 1;
    private double turnSpeed = 10;
    private double drift = 0.95;
    private double friction = 0.93;
    private double driftFriction = 0.98;

    private double dx = 0;
    private double dy = 0;

    private boolean turnLeft = false;
    private boolean turnRight = false;
    private boolean drifting = false;
    private boolean hit = false;

    @Override
    public void onAdded() {

        getInput().addAction(new UserAction("Accelerate_" + config.playerIndex) {
            @Override
            protected void onAction() {
                if(hit){

                }else{
                    currentSpeed += acceleration;
                }

            }
        }, config.keyUp);

        getInput().addAction(new UserAction("Brake_" + config.playerIndex) {
            @Override
            protected void onAction() { currentSpeed -= acceleration; }
        }, config.keyDown);

        getInput().addAction(new UserAction("Turn Left_" + config.playerIndex) {
            @Override
            protected void onActionBegin() { turnLeft = true; }
            @Override
            protected void onActionEnd() { turnLeft = false; }
        }, config.keyLeft);

        getInput().addAction(new UserAction("Turn Right_" + config.playerIndex) {
            @Override
            protected void onActionBegin() { turnRight = true; }
            @Override
            protected void onActionEnd() { turnRight = false; }
        }, config.keyRight);

        getInput().addAction(new UserAction("Drift_" + config.playerIndex) {
            @Override
            protected void onActionBegin() { drifting = true; }
            @Override
            protected void onActionEnd() { drifting = false; }
        }, config.keyDrift);
    }

    @Override
    public void onUpdate(double tpf) {
        JunkieKartApp app = (JunkieKartApp) FXGL.getApp();

        double rotationAmount = 0;
        if (currentSpeed >= 0.15) {
            double speedFactor = Math.abs(currentSpeed);
            double baseTurn = turnSpeed / (1.0 + speedFactor * 0.1) * tpf * 60;
            rotationAmount = drifting ? baseTurn * 1.4 : baseTurn;
            if (currentSpeed < 0) rotationAmount = -rotationAmount;
        }

        if (turnLeft)  entity.rotateBy(-rotationAmount);
        if (turnRight) entity.rotateBy(rotationAmount);

        double angleRad = Math.toRadians(entity.getRotation());
        double targetDx = Math.sin(angleRad) * currentSpeed;
        double targetDy = -Math.cos(angleRad) * currentSpeed;

        double activeDrift = drifting ? 0.88 : drift;

        double dot = (dx * targetDx + dy * targetDy) /
                (Math.sqrt(dx*dx + dy*dy) * Math.sqrt(targetDx*targetDx + targetDy*targetDy) + 0.001);
        if (drifting || dot < 0.99 && currentSpeed > 0.1) {
            dx = dx * activeDrift + targetDx * (1 - activeDrift);
            dy = dy * activeDrift + targetDy * (1 - activeDrift);
            entity.getComponent(SkidMarkComponent.class).setActive(true);
            entity.getComponent(EffectComponent.class).spawnSmokeEffect(true);
        } else {
            dx = targetDx;
            dy = targetDy;
            entity.getComponent(SkidMarkComponent.class).setActive(false);
            entity.getComponent(EffectComponent.class).spawnSmokeEffect(false);
        }


        int steps = 10;
        double stepX = dx / steps;
        double stepY = dy / steps;

        for (int i = 0; i < steps; i++) {
            if (app.isOnTrack(entity.getX() + stepX, entity.getY() + stepY)) {
                entity.translate(stepX, stepY);
            } else if (app.isOnTrack(entity.getX() + stepX, entity.getY())) {
                entity.translateX(stepX);
                dy *= -0.5;
                currentSpeed *= 0.5;
                break;
            } else if (app.isOnTrack(entity.getX(), entity.getY() + stepY)) {
                entity.translateY(stepY);
                dx *= -0.5;
                currentSpeed *= 0.5;
                break;
            } else {
                if (!app.isOnTrack(entity.getX(), entity.getY())) {
                    entity.translateX(-dx);
                    entity.translateY(-dy);
                }
                currentSpeed *= -0.3;
                dx *= -0.3;
                dy *= -0.3;
                break;
            }
        }

        currentSpeed *= drifting ? driftFriction : friction;

        double actualSpeed = Math.sqrt(dx * dx + dy * dy);
        if (actualSpeed > maxSpeed) {
            dx *= maxSpeed / actualSpeed;
            dy *= maxSpeed / actualSpeed;
        }

        if (currentSpeed > maxSpeed) currentSpeed = currentSpeed * 0.98 + maxSpeed * 0.02;
        if (currentSpeed < -maxSpeed / 3) currentSpeed = currentSpeed * 0.98 + (-maxSpeed / 3) * 0.02;
    }

    public double getMaxSpeed() { return maxSpeed; }
    public void setMaxSpeed(double newMax) { maxSpeed = newMax; }
    public double getCurrentSpeed() { return currentSpeed; }
    public void setCurrentSpeed(double newCurrent) { currentSpeed = newCurrent; }
    public void setHit(boolean newHit) { hit = newHit; }
}