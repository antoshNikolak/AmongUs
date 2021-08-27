package Entity;

import Animation.AnimState;
import Component.AnimationComp;
import Component.PosComp;

public class Tile extends Entity {

    public Tile(PosComp posComp, String texture) {
        EntityRegistryServer.addEntity(this);
        addComponent(posComp);
        addComponent(configAnimation(texture));
    }

    public AnimationComp configAnimation(String texture){
        AnimationComp animationComp = new AnimationComp();
        animationComp.addAnimation(texture);
        animationComp.setCurrentAnimation(AnimState.CONST);
        return animationComp;
    }
}
