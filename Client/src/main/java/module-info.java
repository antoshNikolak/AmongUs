module Client {
    requires javafx.fxml;
    requires javafx.controls;

    requires kryonet;
    requires kryo;

    requires Shared;

    opens StartUp;
    opens Screen;
    opens ScreenController;
    opens Texture;
    opens ScreenCounter;
}