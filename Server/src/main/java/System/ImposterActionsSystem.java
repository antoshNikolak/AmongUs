package System;

import Animation.AnimState;
import Client.Client;
import Component.*;
import ConnectionServer.ConnectionServer;
import DistanceFinder.DistanceFinder;
import Entity.Player;
import Packet.AddEntityReturn.AddChangingEntityReturn;
import Packet.EntityState.NewEntityState;
import Packet.Position.*;
import DistanceFinder.EntityDistance;
import StartUpServer.AppServer;

import java.util.*;
import java.util.stream.Collectors;

import Entity.DeadPlayer;
import Entity.EntityRegistryServer;
import Entity.*;
import TimerHandler.TimerStarter;

public class ImposterActionsSystem extends BaseSystem {

//    private static final List<Player> ghosts = new ArrayList<>();


    @Override
    public void update() {
    }

    public void handleSpecialActions(Player player, PosRequest packet) {
        if (packet.isKillKey() && player.hasComponent(ImposterComp.class)) {
            if (player.getComponent(ImposterComp.class).isAbleToKill()) {
                handleKillAction(player);
            }
        }
    }

    private void handleKillAction(Player imposter) {
        Optional<Player> crewMateOptional = getClosestCrewMate(imposter);
        crewMateOptional.ifPresent(crewMate -> {
            killCrewMate(crewMate);
            startKillImposterCoolDown(imposter);
        });
    }

    private void killCrewMate(Player crewMate) {
        stopCrewMateTask(crewMate);
        setGhostAttributes(crewMate);
        sendDeadBodyToClients(crewMate);
        raiseGhost(crewMate);
        updateGhostForClients(crewMate);
    }

    private void stopCrewMateTask(Player crewMate){
        ConnectionServer.sendTCP(new RemoveNestedScreen(), crewMate.getConnectionID());
        crewMate.getCurrentTask().setPlayer(null);
        crewMate.setCurrentTask(null);
    }

    private void startKillImposterCoolDown(Player imposter) {
        ImposterComp imposterComp = imposter.getComponent(ImposterComp.class);
        imposterComp.setAbleToKill(false);
        TimerStarter.startTimer("KillCoolDownTimer", 5, () -> imposterComp.setAbleToKill(true), imposter.getConnectionID());
    }

    private void setGhostAttributes(Player crewMate) {
        crewMate.getComponent(AliveComp.class).setAlive(false);
        crewMate.getComponent(AnimationComp.class).setCurrentAnimation(AnimState.GHOST_RIGHT);
        crewMate.removeComponent(HitBoxComp.class);
//        ghosts.add(crewMate);

    }

    private void updateGhostForClients(Player ghost) {
        clearGhostsForAlivePlayers(ghost);
        sendNewGhostExistingGhosts(ghost);
        AppServer.currentGame.getEntityReturnBuffer().putEntity(ghost, getGhostConnectionIDs());
    }

    private void clearGhostsForAlivePlayers(Player ghost) {
        for (Integer connectionID : getAliveConnectionIDs()) {
            ConnectionServer.sendTCP(new ClearEntityReturn(EntityRegistryServer.getEntityID(ghost)), connectionID);
        }
    }

    private void sendNewGhostExistingGhosts(Player ghost) {
        int connectionID = ConnectionServer.getConnectionIDFromPlayer(ghost);
        List<Player> otherGhosts = getGhostPlayers();
        otherGhosts.remove(ghost);
        List<NewEntityState> newEntityStates = new ArrayList<>(EntityReturnBuffer.adaptCollectionToNewEntityStates(otherGhosts));
        ConnectionServer.sendTCP(new AddChangingEntityReturn(newEntityStates), connectionID);
    }


    private void raiseGhost(Player ghost) {
        PosComp posComp = ghost.getComponent(PosComp.class);
        posComp.getPos().incrementY(-30);
    }


    private void sendDeadBodyToClients(Player crewMate) {
        ColourComp colourComp = crewMate.getComponent(ColourComp.class);
        PosComp posComp = crewMate.getComponent(PosComp.class);
        ConnectionServer.sendTCPToAllPlayers(new AddChangingEntityReturn(new DeadPlayer(colourComp.getColour(), posComp).adaptToNewAnimatedEntityState()));
    }

    private Optional<Player> getClosestCrewMate(Player impostor) {
        PriorityQueue<EntityDistance<Player, Player>> smallestDistances = new PriorityQueue<>();
        addToSmallestDistancesQueue(smallestDistances, impostor);
        return Optional.ofNullable(popClosestPlayer(smallestDistances));
    }

    private void addToSmallestDistancesQueue(PriorityQueue<EntityDistance<Player, Player>> smallestDistances, Player impostor) {
        AppServer.currentGame.getPlayers().stream().
                filter(player -> checkPlayerCanDie(player, impostor)).
                forEach(player -> smallestDistances.add(DistanceFinder.getDistanceBetweenEntities(player, impostor)));
    }

    private Player popClosestPlayer(PriorityQueue<EntityDistance<Player, Player>> smallestDistances) {
        while (true) {
            EntityDistance<Player, Player> entityDistance = smallestDistances.poll();
            if (entityDistance != null && entityDistance.getDistance() < 300) {
                return entityDistance.getEntity();
            } else {
                return null;
            }
        }
    }

//    private static <T extends Entity> EntityDistance<T> getDistanceBetweenEntities(T e1, T e2) {
////        if (!e1.hasComponent(PosComp.class) || !e2.hasComponent(PosComp.class)) {
////            return null;
////        }
//        checkEntitiesHavePosComps(e1, e2);
//        Pos pos1 = e1.getComponent(PosComp.class).getPos();
//        Pos pos2 = e2.getComponent(PosComp.class).getPos();
//        double deltaX = pos1.getX() - pos2.getX();
//        double deltaY = pos1.getY() - pos2.getY();
//        return new EntityDistance<T>(e1, e2, Math.sqrt(deltaX * deltaX + deltaY * deltaY));
//    }


//    private static void checkEntitiesHavePosComps(Entity ... entities){
//        for (Entity entity: entities){
//            if (!entity.hasComponent(PosComp.class)){
//                throw new IllegalArgumentException("entity doesnt have pos comp");
//            }
//        }
//    }

    private boolean checkPlayerCanDie(Player player, Player impostor) {
        return player != impostor && player.getComponent(AliveComp.class).isAlive();
    }


    public List<Integer> getGhostConnectionIDs() {
        return ConnectionServer.getPlayerConnectionIDs(getGhostPlayers());
    }

    private static List<Player> getGhostPlayers() {
        return AppServer.currentGame.getClients().stream().
                map(Client::getPlayer).
                filter(player -> !player.getComponent(AliveComp.class).isAlive()).
                collect(Collectors.toList());
    }


    public static List<Integer> getAliveConnectionIDs() {
        return ConnectionServer.getPlayerConnectionIDs(getAlivePlayers());
    }

    private static List<Player> getAlivePlayers() {
        return AppServer.currentGame.getPlayers().stream().
                filter(player -> player.getComponent(AliveComp.class).isAlive()).
                collect(Collectors.toList());
    }
}





