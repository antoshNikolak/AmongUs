package System;

import Animation.AnimState;
import Client.Client;
import Component.*;
import ConnectionServer.ConnectionServer;
import Entity.Entity;
import Entity.Player;
import Packet.Position.*;
import PlayerDistance.EntityDistance;
import Position.Pos;
import StartUpServer.AppServer;

import java.util.*;
import java.util.stream.Collectors;

import Entity.DeadPlayer;
import Entity.EntityRegistryServer;
import Entity.*;
import TimerHandler.TimerStarter;

public class ImposterActionsSystem extends BaseSystem {

    private static final List<Player> ghosts = new ArrayList<>();


    @Override
    public void update() {
    }

    public static void handleSpecialActions(Player player, PosRequest packet) {
//        if (player.hasComponent(RoleComp.class)) {
//            RoleComp roleComp = player.getComponent(RoleComp.class);
        if (packet.isKillKey() && player.hasComponent(ImposterComp.class)) {
            if (player.getComponent(ImposterComp.class).isAbleToKill()) {
                handleKillAction(player);
            }
        }
//        }
    }

    private static void handleKillAction(Player imposter) {
        Optional<Player> crewMateOptional = getClosestCrewMate(imposter);
        crewMateOptional.ifPresent(crewMate -> {
            killCrewMate(crewMate);
            startKillImposterCoolDown(imposter);
        });
    }

    private static void killCrewMate(Player crewMate) {
        setGhostAttributes(crewMate);
        sendDeadBodyToClients(crewMate);
        raiseGhost(crewMate);
        updateGhostForClients(crewMate);

    }

    private static void startKillImposterCoolDown(Player imposter) {
        ImposterComp imposterComp = imposter.getComponent(ImposterComp.class);
        imposterComp.setAbleToKill(false);
        TimerStarter.startTimer("KillCoolDownTimer", 5, () -> imposterComp.setAbleToKill(true), imposter.getConnectionID());
    }

    private static void setGhostAttributes(Player crewMate) {
        crewMate.getComponent(AliveComp.class).setAlive(false);
        crewMate.getComponent(AnimationComp.class).setCurrentAnimation(AnimState.GHOST_RIGHT);
        crewMate.removeComponent(HitBoxComp.class);
        ghosts.add(crewMate);

    }

    private static void updateGhostForClients(Player ghost) {

        for (Integer connectionID : getAliveConnectionIDs()) {
            System.out.println("sending to : "+ connectionID);
            System.out.println("registration: "+ EntityRegistryServer.getEntityID(ghost));
            ConnectionServer.sendTCP(new ClearEntityReturn(EntityRegistryServer.getEntityID(ghost)), connectionID);
        }
        sendNewGhostExistingGhosts(ghost);
        AppServer.currentGame.getEntityReturnBuffer().putEntity(ghost, getGhostConnectionIDs());
    }

//    private static void clearGhostsForAlivePlayers(){
//        System.out.println("alive connectionIDs: "+ getAliveConnectionIDs().toString());
//        for (Integer connectionID : getAliveConnectionIDs()) {
//            System.out.println("sending to : "+ connectionID);
//            System.out.println("registration: "+ EntityRegistryServer.getEntityID(ghost));
//            ConnectionServer.sendTCP(new ClearEntityReturn(EntityRegistryServer.getEntityID(ghost)), connectionID);
//        }

//    }

    private static void sendNewGhostExistingGhosts(Player ghost) {
        int connectionID = ConnectionServer.getConnectionIDFromPlayer(ghost);
        List<Player> otherGhosts = new ArrayList<>(ghosts);
        otherGhosts.remove(ghost);
        List<NewEntityState> newEntityStates = new ArrayList<>(EntityReturnBuffer.adaptCollectionToNewEntityStates(otherGhosts));
        ConnectionServer.sendTCP(new AddChangingEntityReturn(newEntityStates), connectionID);
    }


    private static void raiseGhost(Player ghost) {
        PosComp posComp = ghost.getComponent(PosComp.class);
        posComp.getPos().incrementY(-30);
    }


    private static void sendDeadBodyToClients(Player crewMate) {
        ColourComp colourComp = crewMate.getComponent(ColourComp.class);
        PosComp posComp = crewMate.getComponent(PosComp.class);
        ConnectionServer.sendTCPToAllPlayers(new AddChangingEntityReturn(new DeadPlayer(colourComp.getColour(), posComp).adaptToNewEntityState()));
    }

    private static Optional<Player> getClosestCrewMate(Player impostor) {
        PriorityQueue<EntityDistance<Player>> smallestDistances = new PriorityQueue<>();
        addToSmallestDistancesQueue(smallestDistances, impostor);
        return Optional.ofNullable(popClosestPlayer(smallestDistances));
    }

    private static void addToSmallestDistancesQueue(PriorityQueue<EntityDistance<Player>> smallestDistances, Player impostor) {
        AppServer.currentGame.getPlayers().stream().
                filter(player -> checkPlayerCanDie(player, impostor)).
                forEach(player -> smallestDistances.add(getDistanceBetweenEntities(player, impostor)));
    }

    private static Player popClosestPlayer(PriorityQueue<EntityDistance<Player>> smallestDistances) {
        while (true) {
            EntityDistance<Player> entityDistance = smallestDistances.poll();
            if (entityDistance == null) return null;
            if (entityDistance.getDistance() < 300) {
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

    private static boolean checkPlayerCanDie(Player player, Player impostor) {
        return player != impostor && player.getComponent(AliveComp.class).isAlive();
    }


    public static List<Integer> getGhostConnectionIDs() {
        return ConnectionServer.getPlayerConnectionIDs(ghosts);

//        return AppServer.currentGame.getClients().stream().
//                filter(client -> !client.getPlayer().getComponent(AliveComp.class).isAlive()).
//                map(Client::getConnectionID).
//                collect(Collectors.toList());
    }

    public static List<Integer> getAliveConnectionIDs() {
        return ConnectionServer.getPlayerConnectionIDs(getAlivePlayers());
//        return AppServer.currentGame.getClients().stream().
//                filter(client -> client.getPlayer().getComponent(AliveComp.class).isAlive()).
//                map(Client::getConnectionID).
//                collect(Collectors.toList());
    }

    private static List<Player> getAlivePlayers(){
        return AppServer.currentGame.getPlayers().stream().
                filter(player -> player.getComponent(AliveComp.class).isAlive()).
                collect(Collectors.toList());
        }
    }





