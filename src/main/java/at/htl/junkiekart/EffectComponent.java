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

        double backAngle = Math.toRadians(entity.getRotation() + 180);
        double spread = Math.toRadians((Math.random() - 0.5) * 2000);
        double angle = backAngle + spread;
        double speed = 30 + Math.random() * 30;

        double vx = Math.sin(angle) * speed;
        double vy = -Math.cos(angle) * speed;

        // perpendicular offset for the two lines
        double perpAngle = Math.toRadians(entity.getRotation() + 90);
        double offset = 8;
        double ox = Math.sin(perpAngle) * offset;
        double oy = -Math.cos(perpAngle) * offset;

        if(smokeBool){
            spawmMovingDot(entity.getX() + ox, entity.getY() + oy, vx, vy, 8, Color.DARKGREY, 0.8);
            spawmMovingDot(entity.getX() - ox, entity.getY() - oy, vx, vy, 8, Color.DARKGREY, 0.8);
        }

        if(shroomBool){
            spawmMovingDot(entity.getX() + ox, entity.getY() + oy, vx, vy, 9, Color.GREEN, 0.4);
            spawmMovingDot(entity.getX() - ox, entity.getY() - oy, vx, vy, 9, Color.PURPLE, 0.4);
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