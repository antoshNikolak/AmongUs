package Entity;

import Animation.AnimState;
import Component.AnimationComp;
import Component.PosComp;
import Component.RenderComp;

public class MazePlayer extends Entity {
    private int connectionID;

    public MazePlayer(int connectionID) {
        this.connectionID = connectionID;
        AnimationComp animationComp = new AnimationComp();
        animationComp.addAnimation("red-rect");
        animationComp.setCurrentAnimation(AnimState.CONST);
        addComponent(animationComp);
        addComponent(new PosComp(50, 50, 50, 50));

    }
}
