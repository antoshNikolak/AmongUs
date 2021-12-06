package Entity;

import Animation.AnimState;
import Component.AnimationComp;
import Component.ColourComp;
import Component.PosComp;
import Component.VelComp;
import Position.Pos;

public class DeadBody extends Entity {

    public DeadBody(String colour, PosComp pos) {
        super();
        addComponent(new ColourComp(colour));
//        addComponent(new VelComp());
        addComponent(pos);
        addComponent(configAnimationComp(colour));
    }

    private static AnimationComp configAnimationComp(String colour){
        AnimationComp animationComp = new AnimationComp();
        animationComp.addAnimation(AnimState.CONST, "dead-"+colour);
        animationComp.setCurrentAnimation(AnimState.CONST);
        return animationComp;

    }
}
