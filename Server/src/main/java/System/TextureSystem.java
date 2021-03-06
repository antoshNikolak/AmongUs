package System;

import Packet.Animation.AnimState;
import Component.AliveComp;
import Component.AnimationComp;
import Component.VelComp;
import Entity.Entity;
import Packet.Position.InputRequest;
import StartUpServer.AppServer;
import Entity.Player;

import java.util.Set;

public class TextureSystem extends BaseSystem {


    @Override
    public void update() {
        for (Entity entity : AppServer.currentGame.getStateManager().getTopState().getEntities()) {
            if (super.checkEntityHasComponents(entity, AnimationComp.class, VelComp.class)) {
                AnimationComp animComp = entity.getComponent(AnimationComp.class);
                VelComp velComp = entity.getComponent(VelComp.class);
                processEntityAnimation(animComp, velComp, entity);
            }
        }
    }

    @Override
    public void handleAction(Player player, InputRequest packet) {}

    private void processEntityAnimation(AnimationComp animComp, VelComp velComp, Entity entity) {
        if (animComp.getCurrentAnimation().getIndexesPerFrame() != 0) {
            changeAnimFrame(velComp, animComp);
            AppServer.currentGame.getEntityReturnBuffer().putEntity(entity);
        }
    }

    private void changeAnimFrame(VelComp velComp, AnimationComp animComp) {
        if (velComp.getVelX() != 0 || velComp.getVelY() != 0) {//if character moving
            animComp.getCurrentAnimation().runAnimation();//change current texture
        } else {
            animComp.getCurrentAnimation().setIndex(0);//set texture to standing still
        }
    }


    public static void handlePlayerAnimationState(VelComp velComp, AnimationComp animationComp, AliveComp aliveComp) {
        if (!checkAnimationIsValid(animationComp)) return;
        if (velComp.getVelX() != velComp.getPreviousVelX()) {
            if (velComp.getVelX() < 0) {
                if (aliveComp.isAlive()) {
                    animationComp.setCurrentAnimation(AnimState.LEFT);
                }else {
                    animationComp.setCurrentAnimation(AnimState.GHOST_LEFT);
                }
            } else if (velComp.getVelX() > 0) {
                if (aliveComp.isAlive()) {
                    animationComp.setCurrentAnimation(AnimState.RIGHT);
                }else {
                    animationComp.setCurrentAnimation(AnimState.GHOST_RIGHT);
                }
            }
        }
    }

    private static boolean checkAnimationIsValid(AnimationComp animationComp){
        Set<AnimState> animStates = animationComp.getAnimStates();
        return animStates.contains(AnimState.GHOST_LEFT) && animStates.contains(AnimState.GHOST_RIGHT);
    }



}
