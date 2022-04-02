module Client {
    requires javafx.fxml;
    requires javafx.controls;

    requires kryonet;
    requires kryo;
    requires minlog;

    requires Shared;
    requires java.desktop;

    opens StartUp;
    opens Screen;
    opens ScreenController;
    opens Texture;
//    opens ScreenCounter;

    exports Node to javafx.fxml;
}