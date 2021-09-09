package Component;

import State.TaskState;

public class TaskComp implements Component{
    private final TaskState taskState;

    public TaskComp(TaskState taskState) {
        this.taskState = taskState;
    }

    public TaskState getTaskState() {
        return taskState;
    }
}
