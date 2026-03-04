module at.htl.junkiekart {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.almasb.fxgl.all;

    opens at.htl.junkiekart to javafx.fxml;
    exports at.htl.junkiekart;
}