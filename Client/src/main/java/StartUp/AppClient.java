package StartUp;

import Game.Game;
import Screen.EntryScreen;
import Screen.ScreenManager;
import Screen.TextureManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class AppClient  extends Application {

    public static Game currentGame;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        setUpStage(stage);
    }

    private void setUpStage(Stage stage){
        Scene scene = new Scene(new Pane(), 600, 400);
        stage.setScene(scene);
        ScreenManager.setScene(scene);
        ScreenManager.activate(EntryScreen.class);
        stage.show();
        TextureManager.init();
    }
}
