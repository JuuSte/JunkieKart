package at.htl.junkiekart;

import com.almasb.fxgl.entity.component.Component;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.*;

public class smokeEffectComponent extends Component {

    private boolean active = false;

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public void onUpdate(double tpf) {
        if (!active) return;

        double backAngle = Math.toRadians(entity.getRotation() + 180);
        double spread = Math.toRadians((Math.random() - 0.5) * 60);
        double angle = backAngle + spread;
        double speed = 30 + Math.random() * 30;

        double vx = Math.sin(angle) * speed;
        double vy = -Math.cos(angle) * speed;

        // perpendicular offset for the two lines
        double perpAngle = Math.toRadians(entity.getRotation() + 90);
        double offset = 8;
        double ox = Math.sin(perpAngle) * offset;
        double oy = -Math.cos(perpAngle) * offset;

        spawnSmoke(entity.getX() + ox, entity.getY() + oy, vx, vy);
        spawnSmoke(entity.getX() - ox, entity.getY() - oy, vx, vy);
    }

    private void spawnSmoke(double x, double y, double vx, double vy) {
        double size = 4 + Math.random() * 6;
        Circle circle = new Circle(size, Color.color(0.7, 0.7, 0.7, 0.7));

        var smoke = entityBuilder()
                .at(x, y)
                .view(circle)
                .zIndex(2)
                .buildAndAttach();

        animationBuilder()
                .duration(Duration.seconds(1))
                .onFinished(() -> smoke.removeFromWorld())
                .fadeOut(smoke)
                .buildAndPlay();

        animationBuilder()
                .duration(Duration.seconds(1))
                .translate(smoke)
                .from(new javafx.geometry.Point2D(x, y))
                .to(new javafx.geometry.Point2D(x + vx, y + vy))
                .buildAndPlay();
    }
}