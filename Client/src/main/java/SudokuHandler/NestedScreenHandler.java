package SudokuHandler;

import Packet.NestedPane.AddsPane;
import Screen.GameScreen;
import Screen.ScreenManager;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class NestedScreenHandler {

    public static GameScreen createGameScreen(AddsPane packet) {
        Pane pane = createPane(packet);
        createBackground(pane);
        GameScreen gameScreen = new GameScreen.Builder(pane)
                .withNodes(packet.getNodes())
                .withNewEntityStates(packet.getNewEntityStates())
                .build();

        addNestedScreen(gameScreen);
        gameScreen.setClearBoundaries(0, 0, packet.getPaneWidth(), packet.getPaneHeight());
        return gameScreen;
    }

    private static Pane createPane(AddsPane packet) {
        Pane pane = new Pane();
        pane.setLayoutX(packet.getPaneX());
        pane.setLayoutY(packet.getPaneY());
        pane.setPrefWidth(packet.getPaneWidth());
        pane.setPrefHeight(packet.getPaneHeight());
        return pane;
    }

    private static void createBackground(Pane pane){
        Rectangle rectangle = new Rectangle(0 , 0, pane.getPrefWidth(), pane.getPrefHeight());
        rectangle.setFill(Color.WHITE);
        pane.getChildren().add(rectangle);
    }

    public static void addNestedScreen(GameScreen gameScreen) {
        ScreenManager.getScreen(GameScreen.class).setNestedScreen(gameScreen);
    }

}
