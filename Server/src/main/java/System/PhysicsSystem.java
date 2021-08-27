package System;

import Animation.AnimState;
import Client.Client;
import Component.AnimationComp;
import Component.PosComp;
import Component.VelComp;
import ConnectionServer.ConnectionServer;
import Entity.Player;
import Packet.Packet;
import Packet.Position.PosRequest;
import StartUpServer.AppServer;

import java.util.List;
import java.util.Objects;
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
}
