package Entity;

import AuthorizationServer.AuthorizationServer;
import Packet.Animation.AnimState;
import Client.Client;
import Component.*;
import Packet.EntityState.NewAnimatedEntityState;
import State.TaskState;

import static StartUpServer.AppServer.currentGame;

public class Player extends Entity {
    //each player must have its own state
    private TaskState currentTask;
    private final Client client;
    private final int connectionID;
    private final String nameTag;


    public Player(Client client, String colour, int connectionID) {
        this.connectionID = connectionID;
        this.client = client;
        this.nameTag = AuthorizationServer.clientUserDataMap.get(client).getUserName();
        startComps(new PosComp(100, 100, 50, 63), colour, connectionID);
        currentGame.getPlayers().add(this);
    }

//    public Player(String colour, int connectionID, PosComp posComp) {
//        super();
//        this.connectionID = connectionID;
//        startComps(posComp, colour);
//    }


    @Override
    public NewAnimatedEntityState adaptToNewAnimatedEntityState(boolean scrollable) {
        PosComp posComp = getComponent(PosComp.class);
        AnimationComp animationComp = getComponent(AnimationComp.class);
        int registrationID = EntityRegistryServer.getEntityID(this);
        NewAnimatedEntityState newAnimatedEntityState = new NewAnimatedEntityState(registrationID, posComp.getPos(), animationComp.adaptToAllNewAnimations(), animationComp.getCurrentAnimationState());
        newAnimatedEntityState.setScrollable(scrollable);
        newAnimatedEntityState.setNameTag(this.nameTag);
        return newAnimatedEntityState;//todo record in document and simplify
    }

    public String getNameTag() {
        return nameTag;
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

    public void startComps(PosComp posComp, String colour, int connectionID){
        addComponent(posComp);
        addComponent(new ColourComp(colour));
        addComponent(new VelComp());
        addComponent(new HitBoxComp(posComp));
        addComponent(new AliveComp(true));
//        addComponent(new ConnectionComp(connectionID));
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

    public Client getClient() {
        return client;
    }
}
