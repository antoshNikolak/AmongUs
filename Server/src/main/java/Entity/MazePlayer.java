package Entity;

import Packet.Animation.AnimState;
import Component.*;
import State.TaskState;

//entity used when a player plays the maze mini game
public class MazePlayer extends Entity {

    public MazePlayer(TaskState taskState, int connectionID) {
        super();
        AnimationComp animationComp = new AnimationComp();
        animationComp.addAnimation("red-rect");
        animationComp.setCurrentAnimation(AnimState.CONST);
        addComponent(animationComp);
        PosComp posComp = new PosComp(50, 50, 15, 15);
        addComponent(posComp);
        addComponent(new HitBoxComp(posComp));
        addComponent(new VelComp());
        addComponent(new TaskPlayerComp(taskState, connectionID));


    }
}
