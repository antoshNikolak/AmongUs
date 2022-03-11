package System;

import Component.*;
import Entity.Entity;
import Entity.Player;
import Entity.*;
import Packet.Position.InputRequest;
import StartUpServer.AppServer;
import State.MazeTaskState;

import java.util.List;

public class PhysicsSystem extends BaseSystem {
    private final List<Entity> entitiesToCollideWith;

    public PhysicsSystem(List<Entity> entitiesToCollideWith) {
        this.entitiesToCollideWith = entitiesToCollideWith;
    }

    @Override
    public void update() {

    }

    @Override
    public void handleAction(Player player, InputRequest packet) {
        Entity playerForm = getPlayersMovingForm(player);//get entity the player wants to move
        applyPacketToPlayer(playerForm, packet);//shift the player's position, based on input
        handleClientReturn(playerForm);//return players new position
    }

    private Entity getPlayersMovingForm(Player player) {
        if (player.getCurrentTask() != null) {
            if (player.getCurrentTask() instanceof MazeTaskState) {
                return ((MazeTaskState) player.getCurrentTask()).getMazePlayer();
            }
        }
        return player;
    }

    private void handleClientReturn(Entity entity) {
        if (entity.hasComponent(TaskPlayerComp.class)) {//player is playing a task
            AppServer.currentGame.getEntityReturnBuffer().putEntity(entity, entity.getComponent(TaskPlayerComp.class).getConnectionID());
            //add entity the client is controlling within the task
        } else if (entity.getComponent(AliveComp.class).isAlive()) {
            AppServer.currentGame.getEntityReturnBuffer().putEntity(entity);//add main game sprite
        } else {
            AppServer.currentGame.getEntityReturnBuffer().putEntity(entity,
                    AppServer.currentGame.getStateManager().getTopState().getSystem(ImposterActionsSystem.class).getGhostConnectionIDs());
            //add ghost, only to be sent to other ghosts
        }
    }


    private void applyPacketToPlayer(Entity player, InputRequest packet) {
        PosComp posComp = player.getComponent(PosComp.class);
        VelComp velComp = player.getComponent(VelComp.class);
        AliveComp aliveComp = player.getComponent(AliveComp.class);
        AnimationComp animationComp = player.getComponent(AnimationComp.class);
        applyPacketToPos(posComp, velComp, packet);//update player position comp
        handleCollisions(player, velComp, posComp);//stop player moving through tile
        TextureSystem.handlePlayerAnimationState(velComp, animationComp, aliveComp);//change players texture

    }

    private void handleCollisions(Entity player, VelComp velComp, PosComp posComp) {
        if (player.hasComponent(HitBoxComp.class)) {
            HitBoxComp hitBoxComp = player.getComponent(HitBoxComp.class);
            handleCollisionY(velComp, posComp, hitBoxComp);
            handleCollisionX(velComp, posComp, hitBoxComp);
        }
    }

    private static void applyPacketToPos(PosComp posComp, VelComp velComp, InputRequest packet) {
        velComp.setPreviousVelX(velComp.getVelX());
        velComp.setPreviousVelY(velComp.getVelY());
        velComp.setVelX(0);
        velComp.setVelY(0);

        if (packet.isRight()) {
            posComp.getPos().incrementX(5);
            velComp.setVelX(5);
        }
        if (packet.isLeft()) {
            posComp.getPos().incrementX(-5);
            velComp.setVelX(-5);
        }
        if (packet.isDown()) {
            posComp.getPos().incrementY(5);
            velComp.setVelY(5);
        }
        if (packet.isUp()) {
            posComp.getPos().incrementY(-5);
            velComp.setVelY(-5);
        }
    }


    private void handleCollisionX(VelComp velComp, PosComp posComp, HitBoxComp hitBox) {
        hitBox.setX(posComp.getPos().getX());
        entitiesToCollideWith.stream().
                filter(this::canEntityCollideWithPlayer).//check player has a hit box
                forEach(entity -> checkCollisionsX(entity.getComponent(HitBoxComp.class), hitBox, posComp, velComp));
    }

    private void checkCollisionsX(HitBoxComp bumperHitBox, HitBoxComp hitBox, PosComp posComp, VelComp velComp) {
        if (bumperHitBox.intersect(hitBox)) {//if collision has occurred
            hitBox.incrementX(-velComp.getVelX());//take a step back
            while (!bumperHitBox.intersect(hitBox)) {
                hitBox.incrementX(Math.signum(velComp.getVelX()));
            }//move forward step by step until collision has happened
            hitBox.incrementX(-Math.signum(velComp.getVelX()));//take step back
            posComp.getPos().setX(hitBox.getX());//update player position
        }
    }

    private void handleCollisionY(VelComp velComp, PosComp posComp, HitBoxComp hitBox) {//null pointer happens when client sends input but state hasnt been changed yet
        hitBox.setY(posComp.getPos().getY());
        entitiesToCollideWith.stream().
                filter(this::canEntityCollideWithPlayer).
                forEach(entity -> {checkCollisionsY(entity.getComponent(HitBoxComp.class), hitBox, posComp, velComp);});
    }

    private void checkCollisionsY(HitBoxComp bumperHitBox, HitBoxComp hitBox, PosComp posComp, VelComp velComp) {
        if (bumperHitBox.intersect(hitBox)) {
            hitBox.incrementY(-velComp.getVelY());
            while (!bumperHitBox.intersect(hitBox)) {
                hitBox.incrementY(Math.signum(velComp.getVelY()));
            }
            hitBox.incrementY(-Math.signum(velComp.getVelY()));
            posComp.getPos().setY(hitBox.getY());
        }
    }

    private boolean canEntityCollideWithPlayer(Entity entity) {
        return entity.hasComponent(HitBoxComp.class) && !(entity instanceof Player) && !(entity instanceof MazePlayer);
    }

}
