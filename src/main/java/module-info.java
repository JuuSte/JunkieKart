module at.htl.junkiekart {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.almasb.fxgl.all;
    requires com.almasb.fxgl.core;
    requires javafx.graphics;

    opens at.htl.junkiekart to javafx.fxml;
    exports at.htl.junkiekart;
}