package Entity;

import Animation.AnimState;
import Component.*;

public class Player extends Entity {
    //each player must have its own state
//    TaskState currentTask;

    public Player(String colour) {
        EntityRegistryServer.addEntity(this);
        addComponent(new PosComp(50, 50, 50, 50));
        addComponent(new ColourComp(colour));
        addComponent(new VelComp());
        addComponent(configAnimation());
    }

    private AnimationComp configAnimation(){
        AnimationComp animationComp = new AnimationComp();
        animationComp.addAnimation(AnimState.RIGHT, getFrames(), 4);
        animationComp.addAnimation(AnimState.LEFT, getLeftFrames(), 4);
        animationComp.setCurrentAnimation(AnimState.RIGHT);
        return animationComp;
    }




    private String[] getFrames(){
        String colour = getComponent(ColourComp.class).getColour();
        return new String[]{"standright-" + colour, "right0-" + colour, "right1-" + colour};
    }

    private String[] getLeftFrames(){
        String colour = getComponent(ColourComp.class).getColour();
        return new String[]{ "standleft-" + colour, "left0-" + colour, "left1-" + colour};
    }
}
