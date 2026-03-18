module at.htl.junkiekart {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.almasb.fxgl.all;
    requires java.desktop;
    requires annotations;

    opens at.htl.junkiekart to javafx.fxml;
    exports at.htl.junkiekart;

    // Ressourcen öffnen:
    opens assets.textures;
}