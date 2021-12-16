package Entity;

import Packet.Animation.AnimState;
import Component.AnimationComp;
import Component.HitBoxComp;
import Component.PosComp;

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
