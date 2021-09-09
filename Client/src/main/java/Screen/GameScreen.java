package Screen;

import EntityClient.Entity;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameScreen extends Screen {
    private int clearX1= 0;
    private int clearX2 = 600;
    private int clearY1= 0;
    private int clearY2= 400;

    private GameScreen nestedScreen;
    private final List<Entity> entities = new CopyOnWriteArrayList<>();


    public GameScreen(Pane pane) {
        super(pane);
    }

    public void render(GraphicsContext gc) {
        gc.clearRect(clearX1, clearY1, clearX2, clearX2);
        for (Entity entity : entities) {
            entity.render(gc);
        }
        if (nestedScreen != null) {
            nestedScreen.render(gc);
        }
    }

    public Screen getNestedScreen() {
        return nestedScreen;
    }

    public void setNestedScreen(GameScreen nestedPane) {
        pane.getChildren().add(nestedPane.getPane());
        this.nestedScreen = nestedPane;
    }

    public void setClearBoundaries(int x1, int y1, int x2, int y2){
        this.clearX1 = x1;
        this.clearY1 = y1;
        this.clearX2 = x2;
        this.clearY2 = y2;
    }

    public List<Entity> getEntities() {
        return entities;
    }
}
