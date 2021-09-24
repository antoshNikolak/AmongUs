package Entity;

import Animation.AnimState;
import Component.*;
import State.TaskState;

public class MazePlayer extends Entity {
    private int connectionID;

    public MazePlayer(TaskState taskState, int connectionID) {
        super();
        this.connectionID = connectionID;
        AnimationComp animationComp = new AnimationComp();
        animationComp.addAnimation("red-rect");
        animationComp.setCurrentAnimation(AnimState.CONST);
        addComponent(animationComp);
        PosComp posComp = new PosComp(50, 50, 15, 15);
        addComponent(posComp);
        addComponent(new HitBoxComp(posComp));
        addComponent(new VelComp());
        addComponent(new TaskPlayerComp(taskState, connectionID));//todo what this mean


    }
}
