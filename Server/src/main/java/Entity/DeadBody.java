package Entity;

import Packet.Animation.AnimState;
import Component.AnimationComp;
import Component.ColourComp;
import Component.PosComp;

public class DeadBody extends Entity {

    public DeadBody(String colour, PosComp pos) {
        super();
        addComponent(new ColourComp(colour));
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
