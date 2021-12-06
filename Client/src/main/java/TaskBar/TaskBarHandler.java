package TaskBar;

import EntityClient.Entity;
import EntityClient.EntityRegistryClient;
import Screen.ScreenManager;
import Screen.TextureManager;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class TaskBarHandler {//todo document
    private static Rectangle taskBarProgress = new Rectangle(0, 0);

    static {
        taskBarProgress.setFill(Color.GREEN);
        Platform.runLater(() -> ScreenManager.getCurrentScreen().addNode(taskBarProgress));
    }

    public static void updateTaskBar(Entity taskBar, int newWidth ) {
//        Entity taskBar = EntityRegistryClient.getEntity(packet.getRegistrationID());
        double fillStartX = taskBar.getPos().getX() + 7;//add 10 to compensate for the frame of the bar
        double fillEndX = taskBar.getPos().getX() + 7 + newWidth;
        double taskBarHeight = TextureManager.getTexture("task-bar").getHeight();
//        taskBarHeight
//        taskBarProgress = new Rectangle(fillEndX - fillStartX, taskBarHeight - 18);

//        Rectangle rectangle = new Rectangle(fillEndX - fillStartX, taskBarHeight - 18);
//        taskBarProgress.setFill(Color.GREEN);
        taskBarProgress.setX(fillStartX);
        taskBarProgress.setY(taskBar.getPos().getY() + 10);
        taskBarProgress.setWidth(fillEndX - fillStartX);
        taskBarProgress.setHeight(taskBarHeight - 18);

//        if (taskBarProgress.getWidth() ==50) {
//            Platform.runLater(() -> ScreenManager.getCurrentScreen().addNode(taskBarProgress));
//        }
    }

    public static Rectangle getTaskBarProgress() {
        return taskBarProgress;
    }
}
