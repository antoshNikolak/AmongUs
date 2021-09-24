package Component;

import State.TaskState;

public class TaskPlayerComp implements Component {

    private int connectionID;
    private TaskState taskState;

    public TaskPlayerComp(TaskState taskState, int connectionID) {
        this.connectionID = connectionID;
        this.taskState = taskState;
    }

    public int getConnectionID() {
        return connectionID;
    }

    public TaskState getTaskState() {
        return taskState;
    }
}
