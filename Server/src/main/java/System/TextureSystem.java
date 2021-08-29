package System;

import Animation.AnimState;
import Component.AnimationComp;
import Component.VelComp;
import Entity.Entity;
import StartUpServer.AppServer;
import Entity.*;

public class TextureSystem extends BaseSystem {


    @Override
    public void update() {
        for (Entity entity : AppServer.currentGame.getCurrentState().getEntities()) {
            if (super.checkEntityHasComponents(entity, AnimationComp.class, VelComp.class)) {
                AnimationComp animComp = entity.getComponent(AnimationComp.class);
                VelComp velComp = entity.getComponent(VelComp.class);
                processEntityAnimation(animComp, velComp, entity);
            }
        }
    }

    private void processEntityAnimation(AnimationComp animComp, VelComp velComp, Entity entity) {
        if (animComp.getCurrentAnimation().getIndexesPerFrame() != 0) {
            changeAnimFrame(velComp, animComp);
            AppServer.currentGame.getEntityReturnBuffer().add(entity);
        }
    }

    private void changeAnimFrame(VelComp velComp, AnimationComp animComp) {
        if (velComp.getVelX() != 0 || velComp.getVelY() != 0) {
            animComp.getCurrentAnimation().runAnimation();
        }else {
            animComp.getCurrentAnimation().setIndex(0);
        }
    }


    public static void handleAnimationState(VelComp velComp, AnimationComp animationComp) {
        if (velComp.getVelX() != velComp.getPreviousVelX()) {
            if (velComp.getVelX() < 0) {
                animationComp.setCurrentAnimation(AnimState.LEFT);
            } else if (velComp.getVelX() > 0) {
                animationComp.setCurrentAnimation(AnimState.RIGHT);
            }
        }
    }
}
