package at.htl.junkiekart;

import com.almasb.fxgl.entity.component.Component;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.animationBuilder;
import static com.almasb.fxgl.dsl.FXGL.entityBuilder;

public class SkidMarkComponent extends Component {

    private double timeSinceLastMark = 0;
    private boolean active = false;

    private final double halfWidth;
    private final double halfHeight;
    private static final double DISPLAY_SIZE = 48;

    public SkidMarkComponent(javafx.scene.image.Image image) {
        double aspectRatio = image.getWidth() / image.getHeight();

        double renderedWidth, renderedHeight;
        if (aspectRatio > 1) {
            renderedWidth = DISPLAY_SIZE;
            renderedHeight = DISPLAY_SIZE / aspectRatio;
        } else {
            renderedWidth = DISPLAY_SIZE * aspectRatio;
            renderedHeight = DISPLAY_SIZE;
        }

        this.halfWidth = renderedWidth / 2 * 0.7;
        this.halfHeight = renderedHeight / 2 * 0.8;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public void onUpdate(double tpf) {
        if (!active) return;

        timeSinceLastMark += tpf;
        if (timeSinceLastMark >= 0.05) {
            timeSinceLastMark = 0;

            double angle = Math.toRadians(entity.getRotation());

            double cx = entity.getCenter().getX();
            double cy = entity.getCenter().getY();

            // rear left
            spawnMark(
                    cx - halfWidth * Math.cos(angle) + halfHeight * Math.sin(angle),
                    cy - halfWidth * Math.sin(angle) - halfHeight * Math.cos(angle)
            );
            // rear right
            spawnMark(
                    cx + halfWidth * Math.cos(angle) + halfHeight * Math.sin(angle),
                    cy + halfWidth * Math.sin(angle) - halfHeight * Math.cos(angle)
            );
            // front left
            spawnMark(
                    cx - halfWidth * Math.cos(angle) - halfHeight * Math.sin(angle),
                    cy - halfWidth * Math.sin(angle) + halfHeight * Math.cos(angle)
            );
            // front right
            spawnMark(
                    cx + halfWidth * Math.cos(angle) - halfHeight * Math.sin(angle),
                    cy + halfWidth * Math.sin(angle) + halfHeight * Math.cos(angle)
            );
        }
    }

    private void spawnMark(double x, double y) {
        Rectangle rect = new Rectangle(4, 9, Color.BLACK);

        var mark = entityBuilder()
                .at(x, y)
                .rotate(entity.getRotation())
                .view(rect)
                .buildAndAttach();

        animationBuilder()
                .duration(Duration.seconds(2))
                .onFinished(() -> mark.removeFromWorld())
                .fadeOut(mark)
                .buildAndPlay();
    }
}