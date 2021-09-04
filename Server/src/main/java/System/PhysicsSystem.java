package System;

import Client.Client;
import Component.*;
import ConnectionServer.ConnectionServer;
import Entity.Entity;
import Entity.Player;
import Entity.*;
import Packet.Position.PosRequest;
import StartUpServer.AppServer;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PhysicsSystem extends BaseSystem {


    @Override
    public void update() {

    }


//    public static void updatePlayerPosition(PosRequest packet, Player player) {
////        List<Client> clientsPlaying = AppServer.currentGame.getClients();
////        Optional<Client> clientOptional = ConnectionServer.getClientFromConnectionID(clientsPlaying, connectionId);//todo maybe put in super class
////        clientOptional.ifPresent(client -> processPlayerMove(client.getPlayer(), packet));
//        processPlayerMove(player, packet);
//    }

    public static Optional<Client> getPlayerOptional(int connectionId) {
        List<Client> clientsPlaying = AppServer.currentGame.getClients();
        return ConnectionServer.getClientFromConnectionID(clientsPlaying, connectionId);
    }


    public static void processPlayerMove(Player player, PosRequest packet) {
        applyPacketToPlayer(player, packet);
        handleClientReturn(player);
    }

    private static void handleClientReturn(Player player) {
        if (player.getComponent(AliveComp.class).isAlive()) {
            AppServer.currentGame.getEntityReturnBuffer().putEntity(player);
        } else {
            AppServer.currentGame.getEntityReturnBuffer().putEntity(player, ImposterActionsSystem.getGhostConnectionIDs());
        }

    }

    private static void applyPacketToPlayer(Player player, PosRequest packet) {
        PosComp posComp = player.getComponent(PosComp.class);
        VelComp velComp = player.getComponent(VelComp.class);
        AliveComp aliveComp = player.getComponent(AliveComp.class);
        AnimationComp animationComp = player.getComponent(AnimationComp.class);
        applyPacketToPos(posComp, velComp, packet);
        TextureSystem.handlePlayerAnimationState(velComp, animationComp, aliveComp);
        handleCollisions(player, velComp, posComp);
    }

    private static void handleCollisions(Player player, VelComp velComp, PosComp posComp) {
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


    private static void handleCollisionX(VelComp velComp, PosComp posComp, HitBoxComp hitBox) {
        hitBox.setX(posComp.getPos().getX());
        AppServer.currentGame.getStateManager().getCurrentState().getEntities().stream().
                filter(PhysicsSystem::canEntityCollideWithPlayer).
                forEach(entity -> checkCollisionsX(entity.getComponent(HitBoxComp.class), hitBox, posComp, velComp));
    }

    private static void checkCollisionsX(HitBoxComp bumperHitBox, HitBoxComp hitBox, PosComp posComp, VelComp velComp) {
        if (bumperHitBox.intersect(hitBox)) {
            hitBox.incrementX(-velComp.getVelX());
            while (!bumperHitBox.intersect(hitBox)) {
                hitBox.incrementX(Math.signum(velComp.getVelX()));
            }
            hitBox.incrementX(-Math.signum(velComp.getVelX()));
            posComp.getPos().setX(hitBox.getX());
        }
    }

    private static void handleCollisionY(VelComp velComp, PosComp posComp, HitBoxComp hitBox) {//null pointer happens when client sends input but state hasnt been changed yet
            hitBox.setY(posComp.getPos().getY());
            AppServer.currentGame.getStateManager().getCurrentState().getEntities().stream().
                    filter(PhysicsSystem::canEntityCollideWithPlayer).
                    forEach(entity -> checkCollisionsY(entity.getComponent(HitBoxComp.class), hitBox, posComp, velComp));
    }

    private static void checkCollisionsY(HitBoxComp bumperHitBox, HitBoxComp hitBox, PosComp posComp, VelComp velComp) {
        if (bumperHitBox.intersect(hitBox)) {
            hitBox.incrementY(-velComp.getVelY());
            while (!bumperHitBox.intersect(hitBox)) {
                hitBox.incrementY(Math.signum(velComp.getVelY()));
            }
            hitBox.incrementY(-Math.signum(velComp.getVelY()));
            posComp.getPos().setY(hitBox.getY());
        }
    }

    private static boolean canEntityCollideWithPlayer(Entity entity) {
        return entity.hasComponent(HitBoxComp.class) && !(entity instanceof Player);
    }

}
