package at.htl.junkiekart;

import com.almasb.fxgl.entity.component.Component;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.*;

public class SkidMarkComponent extends Component {

    private double timeSinceLastMark = 0;
    private boolean active = false;

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public void onUpdate(double tpf) {
        double x = entity.getX();
        double y = entity.getY();
        double rearY = entity.getBottomY();
        double rightX = entity.getRightX();

        if (!active) return;

        timeSinceLastMark += tpf;
        if (timeSinceLastMark >= 0.001) {
            timeSinceLastMark = 0;

            double angle = Math.toRadians(entity.getRotation());

            spawnMark(entity.getX() + 30 * Math.cos(angle) - 5  * Math.sin(angle), entity.getY() + 30 * Math.sin(angle) + 5  * Math.cos(angle)); // top-right
            spawnMark(entity.getX() + 12 * Math.cos(angle) - 5  * Math.sin(angle), entity.getY() + 12 * Math.sin(angle) + 5  * Math.cos(angle)); // top-left
            spawnMark(entity.getX() + 30 * Math.cos(angle) - 30 * Math.sin(angle), entity.getY() + 30 * Math.sin(angle) + 30 * Math.cos(angle)); // bottom-right
            spawnMark(entity.getX() + 12 * Math.cos(angle) - 30 * Math.sin(angle), entity.getY() + 12 * Math.sin(angle) + 30 * Math.cos(angle)); // bottom-left
        }
    }

    private void spawnMark(double x, double y) {
        Rectangle rect = new Rectangle(4, 9, Color.BLACK);

        var mark = entityBuilder()
                .at(x, y)
                .rotate(entity.getRotation())
                .view(rect)
                .zIndex(-1)
                .buildAndAttach();

        animationBuilder()
                .duration(Duration.seconds(2))
                .onFinished(() -> mark.removeFromWorld())
                .fadeOut(mark)
                .buildAndPlay();
    }
}