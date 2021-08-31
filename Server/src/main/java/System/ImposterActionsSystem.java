package System;

import Animation.AnimState;
import Client.Client;
import Component.*;
import ConnectionServer.ConnectionServer;
import Entity.Entity;
import Entity.Player;
import Packet.Position.AddChangingEntityReturn;
import Packet.Position.ClearEntityReturn;
import Packet.Position.PosRequest;
import PlayerDistance.EntityDistance;
import Position.Pos;
import StartUpServer.AppServer;

import java.util.List;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

import Entity.DeadPlayer;
import Entity.EntityRegistryServer;
public class ImposterActionsSystem extends BaseSystem {



    @Override
    public void update() {}

    public static void handleSpecialActions(Player player, PosRequest packet) {
        if (player.hasComponent(RoleComp.class)) {
            RoleComp roleComp = player.getComponent(RoleComp.class);
            if (packet.isKillKey() && roleComp.getRole() == RoleComp.Role.IMPOSTER) {
                handleKillAction(player);
            }
        }
    }

    private static void handleKillAction(Player imposter) {
        Optional<Player> crewMateOptional = getClosestCrewMate(imposter);
        crewMateOptional.ifPresent(crewMate -> killCrewMate(crewMate));
    }

    private static void killCrewMate(Player crewMate) {
        setGhostAttributes(crewMate);
        setGhostAttributes(crewMate);
        sendDeadBodyToClients(crewMate);
        raiseGhost(crewMate);
        updateGhostForClients(crewMate);

    }

    private static void setGhostAttributes(Player crewMate) {
        crewMate.getComponent(AliveComp.class).setAlive(false);
        crewMate.getComponent(AnimationComp.class).setCurrentAnimation(AnimState.GHOST_RIGHT);
        crewMate.removeComponent(HitBoxComp.class);
    }

    private static void updateGhostForClients(Player ghost){
        for (Integer connectionID: getAliveConnectionIDs()){
            ConnectionServer.sendTCP(new ClearEntityReturn(EntityRegistryServer.getEntityID(ghost)), connectionID);
        }
        AppServer.currentGame.getEntityReturnBuffer().putEntity(ghost, getGhostConnectionIDs());
    }

    private static void raiseGhost(Player ghost){
        PosComp posComp = ghost.getComponent(PosComp.class);
        posComp.getPos().incrementY(-30);

    }


    private static void sendDeadBodyToClients(Player crewMate){
        ColourComp colourComp = crewMate.getComponent(ColourComp.class);
        PosComp posComp = crewMate.getComponent(PosComp.class);
        ConnectionServer.sendTCPToAllPlayers(new AddChangingEntityReturn(new DeadPlayer(colourComp.getColour(), posComp).adaptToNewEntityState()));
        posComp.getPos().incrementY(-30);

    }

    private static Optional<Player> getClosestCrewMate(Player impostor) {
        PriorityQueue<EntityDistance<Player>> smallestDistances = new PriorityQueue<>();
        AppServer.currentGame.getPlayers().stream().
                filter(player -> checkPlayerCanDie(player, impostor)).
                forEach(player -> smallestDistances.add(getDistanceBetweenEntities(player, impostor)));
        return Optional.ofNullable(popClosestPlayer(smallestDistances));
    }

    private static Player popClosestPlayer(PriorityQueue<EntityDistance<Player>> smallestDistances) {
        while (true) {
            EntityDistance<Player> entityDistance = smallestDistances.poll();
            if (entityDistance == null) return null;
            if (entityDistance.getDistance() < 50) {
                return entityDistance.getEntity();
            } else {
                return null;
            }
        }
    }

    private static <T extends Entity> EntityDistance<T> getDistanceBetweenEntities(T e1, T e2) {
//        if (!e1.hasComponent(PosComp.class) || !e2.hasComponent(PosComp.class)) {
//            return null;
//        }//todo we can ask
        Pos pos1 = e1.getComponent(PosComp.class).getPos();
        Pos pos2 = e2.getComponent(PosComp.class).getPos();
        double deltaX = pos1.getX() - pos2.getX();
        double deltaY = pos1.getY() - pos2.getY();
        return new EntityDistance<T>(e1, e2, Math.sqrt(deltaX * deltaX + deltaY * deltaY));
    }

    private static boolean checkPlayerCanDie(Player player, Player impostor){
        return player != impostor && player.getComponent(AliveComp.class).isAlive();
    }


    public static List<Integer> getGhostConnectionIDs() {
        return AppServer.currentGame.getClients().stream().
                filter(client -> !client.getPlayer().getComponent(AliveComp.class).isAlive()).
                map(Client::getConnectionID).
                collect(Collectors.toList());
    }

    public static List<Integer> getAliveConnectionIDs() {
        return AppServer.currentGame.getClients().stream().
                filter(client -> client.getPlayer().getComponent(AliveComp.class).isAlive()).
                map(Client::getConnectionID).
                collect(Collectors.toList());
    }

}



