package Entity;

import Animation.AnimState;
import Client.Client;
import Component.*;
import Position.Pos;
import StartUpServer.AppServer;
import State.GameState;
import State.TaskState;

public class Player extends Entity {
    //each player must have its own state
    private TaskState currentTask;
//    private Client client;

    private final int connectionID;


    public Player(String colour, int connectionID) {
        this.connectionID = connectionID;
        startComps(new PosComp(100, 100, 50, 63), colour);
    }

    public Player(String colour, int connectionID, PosComp posComp) {
        super();
        this.connectionID = connectionID;
        startComps(posComp, colour);
    }





    private AnimationComp configAnimation(){
        AnimationComp animationComp = new AnimationComp();
        String colour = getComponent(ColourComp.class).getColour();
        animationComp.addAnimation(AnimState.RIGHT, getFrames(colour), 5);
        animationComp.addAnimation(AnimState.LEFT, getLeftFrames(colour), 5);
        animationComp.addAnimation(AnimState.GHOST_RIGHT, "ghost-right-"+colour);
        animationComp.addAnimation(AnimState.GHOST_LEFT, "ghost-left-"+colour);
        animationComp.setCurrentAnimation(AnimState.RIGHT);
        return animationComp;
    }

    public void startComps(PosComp posComp, String colour){
        addComponent(posComp);
        addComponent(new ColourComp(colour));
        addComponent(new VelComp());
        addComponent(new HitBoxComp(posComp));
        addComponent(new AliveComp(true));
        addComponent(configAnimation());
    }

    private String[] getFrames(String colour){
        return new String[]{"standright-" + colour, "right0-" + colour, "right1-" + colour, "right2-"+ colour, "right3-"+ colour};
    }

    private String[] getLeftFrames(String colour){
        return new String[]{ "standleft-" + colour, "left0-" + colour, "left1-" + colour, "left2-"+ colour, "left3-"+ colour};
    }

    public int getConnectionID() {
        return connectionID;
    }

    public TaskState getCurrentTask() {
        return currentTask;
    }

    public void setCurrentTask(TaskState currentTask) {
        this.currentTask = currentTask;
    }
}
