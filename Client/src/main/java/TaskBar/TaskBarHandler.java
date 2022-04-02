package TaskBar;

import EntityClient.Entity;
import EntityClient.EntityRegistryClient;
import Screen.ScreenManager;
import Screen.TextureManager;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class TaskBarHandler {
    private static final Rectangle taskBarProgress = new Rectangle(0, 0);

    static {
        taskBarProgress.setFill(Color.GREEN);
        Platform.runLater(() -> ScreenManager.getCurrentScreen().addNode(taskBarProgress));
    }

    public static void updateTaskBar(Entity taskBar, int newWidth ) {
        //moves up progress bar of task bar
        double fillStartX = taskBar.getPos().getX() + 7;//add 7 to x to compensate for the frame of the bar
        double fillEndX = taskBar.getPos().getX() + 7 + newWidth;
        double taskBarHeight = TextureManager.getTexture("task-bar").getHeight();
        taskBarProgress.setX(fillStartX);
        taskBarProgress.setY(taskBar.getPos().getY() + 10);
        taskBarProgress.setWidth(fillEndX - fillStartX);
        taskBarProgress.setHeight(taskBarHeight - 18);
    }

    public static Rectangle getTaskBarProgress() {
        return taskBarProgress;
    }
}
