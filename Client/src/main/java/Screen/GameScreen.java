package Screen;

import Camera.Camera;
import EntityClient.Entity;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameScreen extends Screen {
    private int clearX1 = 0;
    private int clearWidth = 600;
    private int clearY1 = 0;
    private int clearHeight = 400;

    private GameScreen nestedScreen;
    private Camera camera;
    private Canvas canvas;
    private GraphicsContext gc;
    private final List<Entity> entities = new CopyOnWriteArrayList<>();


    public GameScreen(Pane pane) {
        super(pane);
        this.canvas = new Canvas(pane.getPrefWidth(), pane.getPrefHeight());
        canvas.setLayoutX(0);
        canvas.setLayoutY(0);
        this.gc = canvas.getGraphicsContext2D();
        pane.getChildren().add(canvas);
    }

    public void render() {
        gc.clearRect(clearX1, clearY1, clearWidth, clearHeight);
        for (Entity entity : entities) {
            if (camera != null) {
                entity.render(gc, camera);
            } else {
                entity.render(gc);
            }
        }
        if (nestedScreen != null) {
            nestedScreen.render();
        }
//        else {
//            gc.setFill(Color.BLUE);
////            gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
//        }
    }

    public void setNestedScreen(GameScreen nestedPane) {
        pane.getChildren().add(nestedPane.getPane());
        this.nestedScreen = nestedPane;
    }

    public void removeNestedScreen(){
        pane.getChildren().remove(nestedScreen.getPane());
        this.nestedScreen = null;
    }

    public void setClearBoundaries(int x1, int y1, int width, int height) {
        this.clearX1 = x1;
        this.clearY1 = y1;
        this.clearWidth = width;
        this.clearHeight = height;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public Camera getCamera() {
        return camera;
    }

    public GameScreen getNestedScreen() {
        return nestedScreen;
    }
}
