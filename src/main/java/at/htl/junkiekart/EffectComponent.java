package at.htl.junkiekart;

import com.almasb.fxgl.entity.component.Component;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.*;

public class EffectComponent extends Component {

    private boolean smokeBool = false;
    private boolean shroomBool = false;

    @Override
    public void onUpdate(double tpf) {

        //movement
        double backAngle = Math.toRadians(entity.getRotation() + 180);
        double spread = Math.toRadians((Math.random() - 0.5) * 2000);
        double angle = backAngle + spread;
        double speed = 30 + Math.random() * 30;

        double vx = Math.sin(angle) * speed;
        double vy = -Math.cos(angle) * speed;

        //location
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);

        double cx = entity.getX() + entity.getWidth()  / 2.0;
        double cy = entity.getY() + entity.getHeight() / 2.0;

        double insetX = entity.getWidth()  * 0.1;
        double insetY = entity.getHeight() * 0.1;
        double hw = entity.getWidth()  / 3.0 - insetX;
        double hh = entity.getHeight() / 3.0 - insetY;

        if(smokeBool){
            spawmMovingDot(cx + (-hw * cos - -hh * sin), cy + (-hw * sin + -hh * cos), vx, vy, 3, Color.DARKGREY, 0.2); // front-left
            spawmMovingDot(cx + ( hw * cos - -hh * sin), cy + ( hw * sin + -hh * cos), vx, vy, 3, Color.DARKGREY, 0.2);
        }

        if(shroomBool){
            spawmMovingDot(cx + (-hw * cos - -hh * sin), cy + (-hw * sin + -hh * cos), vx, vy, 4, Color.LIGHTGREEN, 0.3); // front-left
            spawmMovingDot(cx + ( hw * cos - -hh * sin), cy + ( hw * sin + -hh * cos), vx, vy, 4, Color.LIGHTCORAL, 0.3);
            spawmMovingDot(cx + (-hw * cos - hh * sin), cy + (-hw * sin + hh * cos), vx, vy, 4, Color.LIGHTPINK, 0.3);
            spawmMovingDot(cx + ( hw * cos - hh * sin), cy + ( hw * sin + hh * cos), vx, vy, 4, Color.LIGHTSALMON, 0.3);
        }

    }

    private void spawmMovingDot(double x, double y, double vx, double vy, int size, Color color, double duration) {
        Circle circle = new Circle(size, color);

        var dot = entityBuilder()
                .at(x, y)
                .view(circle)
                .zIndex(1)
                .buildAndAttach();

        animationBuilder()
                .duration(Duration.seconds(1))
                .onFinished(() -> dot.removeFromWorld())
                .fadeOut(dot)
                .buildAndPlay();

        animationBuilder()
                .duration(Duration.seconds(1))
                .translate(dot)
                .from(new javafx.geometry.Point2D(x, y))
                .to(new javafx.geometry.Point2D(x + vx, y + vy))
                .buildAndPlay();
    }

    private void spawnDot(double x, double y, int size, Color color) {
        Circle circle = new Circle(size, color);

        var dot = entityBuilder()
                .at(x, y)
                .view(circle)
                .zIndex(2)
                .buildAndAttach();

        animationBuilder()
                .duration(Duration.seconds(2))
                .onFinished(() -> dot.removeFromWorld())
                .fadeOut(dot)
                .buildAndPlay();
    }

    public void spawnCocainEffect(){
        spawnDot(entity.getX() +10, entity.getY(), 12, Color.WHITE);
        spawnDot(entity.getX() +10, entity.getY() +10, 12, Color.WHITE);
        spawnDot(entity.getX(), entity.getY() +10, 12, Color.WHITE);
        spawnDot(entity.getX(), entity.getY(), 12, Color.WHITE);
    }

    public void spawnBloodEffect(){
        spawnDot(entity.getX() +10, entity.getY(), 12, Color.DARKRED);
        spawnDot(entity.getX() +10, entity.getY() +10, 12, Color.DARKRED);
        spawnDot(entity.getX(), entity.getY() +10, 12, Color.DARKRED);
        spawnDot(entity.getX(), entity.getY(), 12, Color.DARKRED);
    }

    public void spawnSmokeEffect(boolean active) {
        smokeBool = active;
    }

    public void spawnShroomEffect(boolean active) {
        shroomBool = active;

    }
}