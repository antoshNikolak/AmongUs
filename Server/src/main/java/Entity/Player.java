package Entity;

import Animation.AnimState;
import Component.*;
import Position.Pos;
import StartUpServer.AppServer;
import State.GameState;

public class Player extends Entity {
    //each player must have its own state
//    TaskState currentTask;

    public Player(String colour) {
        EntityRegistryServer.addEntity(this);
        PosComp posComp = new PosComp(100, 100, 50, 63);
        addComponent(posComp);
        addComponent(new ColourComp(colour));
        addComponent(new VelComp());
        addComponent(new HitBoxComp(posComp));
        addComponent(configAnimation());
    }

    private AnimationComp configAnimation(){
        AnimationComp animationComp = new AnimationComp();
        animationComp.addAnimation(AnimState.RIGHT, getFrames(), 5);
        animationComp.addAnimation(AnimState.LEFT, getLeftFrames(), 5);
        animationComp.setCurrentAnimation(AnimState.RIGHT);
        return animationComp;
    }

    private String[] getFrames(){
        String colour = getComponent(ColourComp.class).getColour();
        return new String[]{"standright-" + colour, "right0-" + colour, "right1-" + colour, "right2-"+ colour, "right3-"+ colour};
    }

    private String[] getLeftFrames(){
        String colour = getComponent(ColourComp.class).getColour();
        return new String[]{ "standleft-" + colour, "left0-" + colour, "left1-" + colour, "left2-"+ colour, "left3-"+ colour};
    }
}
