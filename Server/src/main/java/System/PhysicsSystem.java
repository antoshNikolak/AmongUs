package System;

import Component.*;
import Entity.Entity;
import Entity.Player;
import Entity.*;
import Packet.Position.PosRequest;
import StartUpServer.AppServer;
import State.MazeTaskState;

import java.util.List;

public class PhysicsSystem extends BaseSystem {
    private List<Entity> entitiesToCollideWith;

    public PhysicsSystem(List<Entity> entitiesToCollideWith) {
        this.entitiesToCollideWith = entitiesToCollideWith;
    }

    @Override
    public void update() {

    }

    @Override
    public void handleAction(Player player, PosRequest packet) {
        Entity playerForm = getPlayersMovingForm(player);
        applyPacketToPlayer(playerForm, packet);
        handleClientReturn(playerForm);
    }


//    public static void updatePlayerPosition(PosRequest packet, Player player) {
////        List<Client> clientsPlaying = AppServer.currentGame.getClients();
////        Optional<Client> clientOptional = ConnectionServer.getClientFromConnectionID(clientsPlaying, connectionId);//todo maybe put in super class
////        clientOptional.ifPresent(client -> processPlayerMove(client.getPlayer(), packet));
//        processPlayerMove(player, packet);
//    }

//    public static Optional<? extends Entity> getPlayerOptional(int connectionId) {
//        return ConnectionServer.getPlayerFromConnectionID(AppServer.currentGame.getPlayers(), connectionId);
//    }


//    public void processPlayerMove(Player player, PosRequest packet) {
//        Entity playerForm = getPlayersMovingForm(player);
//        applyPacketToPlayer(playerForm, packet);
//        handleClientReturn(playerForm);
//    }

    private Entity getPlayersMovingForm(Player player) {
        if (player.getCurrentTask() != null) {
            if (player.getCurrentTask() instanceof MazeTaskState) {
                return ((MazeTaskState) player.getCurrentTask()).getMazePlayer();
            }
        }
        return player;
    }

    private void handleClientReturn(Entity player) {//todo migrate
        if (player.hasComponent(TaskPlayerComp.class)) {
            AppServer.currentGame.getEntityReturnBuffer().putEntity(player, player.getComponent(TaskPlayerComp.class).getConnectionID());
        } else if (player.getComponent(AliveComp.class).isAlive()) {
            AppServer.currentGame.getEntityReturnBuffer().putEntity(player);
        } else {
            AppServer.currentGame.getEntityReturnBuffer().putEntity(player,
                    AppServer.currentGame.getStateManager().getCurrentState().getSystem(ImposterActionsSystem.class).getGhostConnectionIDs());
        }
    }


    private void applyPacketToPlayer(Entity player, PosRequest packet) {
        PosComp posComp = player.getComponent(PosComp.class);
        VelComp velComp = player.getComponent(VelComp.class);
        AliveComp aliveComp = player.getComponent(AliveComp.class);
        AnimationComp animationComp = player.getComponent(AnimationComp.class);
        applyPacketToPos(posComp, velComp, packet);
        handleCollisions(player, velComp, posComp);
        TextureSystem.handlePlayerAnimationState(velComp, animationComp, aliveComp);

    }

    private void handleCollisions(Entity player, VelComp velComp, PosComp posComp) {
        if (player.hasComponent(HitBoxComp.class)) {
            HitBoxComp hitBoxComp = player.getComponent(HitBoxComp.class);
            handleCollisionY(velComp, posComp, hitBoxComp);
            handleCollisionX(velComp, posComp, hitBoxComp);
        }
    }

    private static void applyPacketToPos(PosComp posComp, VelComp velComp, PosRequest packet) {
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
                filter(this::canEntityCollideWithPlayer).
                forEach(entity -> checkCollisionsX(entity.getComponent(HitBoxComp.class), hitBox, posComp, velComp));
    }

    private void checkCollisionsX(HitBoxComp bumperHitBox, HitBoxComp hitBox, PosComp posComp, VelComp velComp) {
        if (bumperHitBox.intersect(hitBox)) {
            hitBox.incrementX(-velComp.getVelX());
            while (!bumperHitBox.intersect(hitBox)) {
                hitBox.incrementX(Math.signum(velComp.getVelX()));
            }
            hitBox.incrementX(-Math.signum(velComp.getVelX()));
            posComp.getPos().setX(hitBox.getX());
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
