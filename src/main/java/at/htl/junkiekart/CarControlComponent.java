package at.htl.junkiekart;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.input.UserAction;
import javafx.scene.input.KeyCode;

import static com.almasb.fxgl.dsl.FXGL.getInput;

public class CarControlComponent extends Component {
    //temp
    private double currentSpeed = 0;
    private double turnAmount = 0;

    private double maxSpeed = 10;
    private double acceleration = 1;
    private double turnSpeed = 1;
    private double drift = 1;

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
            protected void onAction() {
                if (currentSpeed != 0) {
                    entity.rotateBy(-turnAmount);
                }
            }
        }, KeyCode.A);

        getInput().addAction(new UserAction("Turn Right") {
            @Override
            protected void onAction() {
                if (currentSpeed != 0) {
                    entity.rotateBy(turnAmount);
                }
            }
        }, KeyCode.D);
    }
}
