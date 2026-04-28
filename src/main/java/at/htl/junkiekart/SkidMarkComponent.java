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
        if (!active) return;
        double angle = Math.toRadians(entity.getRotation());
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);

        double cx = entity.getX() + entity.getWidth()  / 2.0;
        double cy = entity.getY() + entity.getHeight() / 2.0;

        double insetX = entity.getWidth()  * 0.1;
        double insetY = entity.getHeight() * 0.1;
        double hw = entity.getWidth()  / 3.0 - insetX;
        double hh = entity.getHeight() / 3.0 - insetY;

        spawnMark(cx + (-hw * cos - -hh * sin), cy + (-hw * sin + -hh * cos)); // front-left
        spawnMark(cx + ( hw * cos - -hh * sin), cy + ( hw * sin + -hh * cos)); // front-right
        spawnMark(cx + (-hw * cos -  hh * sin), cy + (-hw * sin +  hh * cos)); // rear-left
        spawnMark(cx + ( hw * cos -  hh * sin), cy + ( hw * sin +  hh * cos)); // rear-right
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