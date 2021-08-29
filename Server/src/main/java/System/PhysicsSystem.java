package System;

import Client.Client;
import Component.AnimationComp;
import Component.HitBoxComp;
import Component.PosComp;
import Component.VelComp;
import ConnectionServer.ConnectionServer;
import Entity.Entity;
import Entity.Player;
import Packet.Position.PosRequest;
import StartUpServer.AppServer;

import java.util.List;
import java.util.Optional;

public class PhysicsSystem extends BaseSystem {


    @Override
    public void update() {

    }


    public static void updatePlayerPosition(PosRequest packet, int connectionId) {
        List<Client> clientsPlaying = AppServer.currentGame.getClients();
        Optional<Client> clientOptional = ConnectionServer.getClientFromConnectionID(clientsPlaying, connectionId);//todo maybe put in super class
        clientOptional.ifPresent(client -> processPlayerMove(client.getPlayer(), packet));
    }


    private static void processPlayerMove(Player player, PosRequest packet) {
        applyPacketToPlayer(player, packet);
        AppServer.currentGame.getEntityReturnBuffer().add(player);
    }

    private static void applyPacketToPlayer(Player player, PosRequest packet) {
        PosComp posComp = player.getComponent(PosComp.class);
        VelComp velComp = player.getComponent(VelComp.class);
        AnimationComp animationComp = player.getComponent(AnimationComp.class);
        applyPacketToPos(posComp, velComp, animationComp, packet);
        handleCollisions(player, velComp, posComp);
    }

    private static void handleCollisions(Player player, VelComp velComp, PosComp posComp) {
        if (player.hasComponent(HitBoxComp.class)) {
            HitBoxComp hitBoxComp = player.getComponent(HitBoxComp.class);
            handleCollisionY(velComp, posComp, hitBoxComp);
            handleCollisionX(velComp, posComp, hitBoxComp);
        }
    }

    private static void applyPacketToPos(PosComp posComp, VelComp velComp, AnimationComp animationComp, PosRequest packet) {
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

        TextureSystem.handleAnimationState(velComp, animationComp);

    }


    private static void handleCollisionX(VelComp velComp, PosComp posComp, HitBoxComp hitBox) {//re,e,ber this doesnt happen every frame
        hitBox.setX(posComp.getPos().getX());
        AppServer.currentGame.getCurrentState().getEntities().stream().
                filter(entity -> entity.hasComponent(HitBoxComp.class) && !(entity instanceof Player)).
                forEach(entity ->checkCollisionsX(entity.getComponent(HitBoxComp.class), hitBox, posComp, velComp));
//        for (Entity bumper : AppServer.currentGame.getCurrentState().getWorld().getTiles()) {//n future create component for collsions between specific entites
//            HitBoxComp bumperHitBox = bumper.getComponent(HitBoxComp.class);
//            if (bumperHitBox.intersect(hitBox)) {
//                hitBox.incrementX(-velComp.getVelX());
//                while (!bumperHitBox.intersect(hitBox)) {
//                    hitBox.incrementX(Math.signum(velComp.getVelX()));
//                }
//                hitBox.incrementX(-Math.signum(velComp.getVelX()));
//                posComp.getPos().setX(hitBox.getX());
//            }
//        }
    }

    private static void checkCollisionsX(HitBoxComp bumperHitBox, HitBoxComp hitBox, PosComp posComp, VelComp velComp){
        if (bumperHitBox.intersect(hitBox)) {
            hitBox.incrementX(-velComp.getVelX());
            while (!bumperHitBox.intersect(hitBox)) {
                hitBox.incrementX(Math.signum(velComp.getVelX()));
            }
            hitBox.incrementX(-Math.signum(velComp.getVelX()));
            posComp.getPos().setX(hitBox.getX());
        }

    }

    private static void handleCollisionY(VelComp velComp, PosComp posComp, HitBoxComp hitBox) {
        hitBox.setY(posComp.getPos().getY());
        AppServer.currentGame.getCurrentState().getEntities().stream().
                filter(PhysicsSystem::canEntityCollideWithPlayer).
                forEach(entity ->checkCollisionsY(entity.getComponent(HitBoxComp.class), hitBox, posComp, velComp));
//        for (Entity bumper : AppServer.currentGame.getCurrentState().getWorld().getTiles()) {
//            HitBoxComp bumperHitBox = bumper.getComponent(HitBoxComp.class);
//            if (bumperHitBox.intersect(hitBox)) {
//                hitBox.incrementY(-velComp.getVelY());
//                while (!bumperHitBox.intersect(hitBox)) {
//                    hitBox.incrementY(Math.signum(velComp.getVelY()));
//                }
//                hitBox.incrementY(-Math.signum(velComp.getVelY()));
//                posComp.getPos().setY(hitBox.getY());
//            }
//        }
    }

    private static void checkCollisionsY(HitBoxComp bumperHitBox, HitBoxComp hitBox, PosComp posComp, VelComp velComp){
        if (bumperHitBox.intersect(hitBox)) {
            hitBox.incrementY(-velComp.getVelY());
            while (!bumperHitBox.intersect(hitBox)) {
                hitBox.incrementY(Math.signum(velComp.getVelY()));
            }
            hitBox.incrementY(-Math.signum(velComp.getVelY()));
            posComp.getPos().setY(hitBox.getY());
        }
    }

    private static boolean canEntityCollideWithPlayer(Entity entity){
        return entity.hasComponent(HitBoxComp.class) && !(entity instanceof Player);
    }

}
