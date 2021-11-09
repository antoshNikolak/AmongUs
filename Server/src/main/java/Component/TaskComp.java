package Component;

import Entity.Player;
import State.TaskState;
import javafx.scene.layout.Pane;

import java.lang.reflect.InvocationTargetException;

public class TaskComp implements Component{
//    private final TaskState taskState;// todo pass in the class and create it using reflection
    private final Class<? extends  TaskState> taskStateClass;

//    public TaskComp(TaskState taskState) {
//        this.taskState = taskState;
//    }


    public TaskComp(Class<? extends TaskState> taskStateClass) {
        this.taskStateClass = taskStateClass;
    }

    public TaskState createTaskState(Player player) {
        try {
            TaskState taskState = taskStateClass.getConstructor().newInstance();
            taskState.setPlayer(player);
            taskState.init();
            return taskState;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }
}
