package Entity;

import Animation.AnimState;
import Component.AnimationComp;
import Component.HitBoxComp;
import Component.PosComp;
import StartUpServer.AppServer;

public class Tile extends Entity {

    public Tile(PosComp posComp, String texture) {
        EntityRegistryServer.addEntity(this);
        addComponent(posComp);
        addComponent(new HitBoxComp(posComp));
        addComponent(configAnimation(texture));
    }

    public AnimationComp configAnimation(String texture){
        AnimationComp animationComp = new AnimationComp();
        animationComp.addAnimation(texture);
        animationComp.setCurrentAnimation(AnimState.CONST);
        return animationComp;
    }
}
