package Component;

import State.TaskState;

public class TaskPlayerComp implements Component {

    private final int connectionID;//stores connection ID of client playing
    private final TaskState taskState;//stores task player is performing

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
