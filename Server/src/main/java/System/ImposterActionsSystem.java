package System;

import Animation.AnimState;
import Client.Client;
import Component.*;
import ConnectionServer.ConnectionServer;
import DistanceFinder.DistanceFinder;
import Entity.Player;
//import Packet.AddEntityReturn.AddChangingEntityReturn;
import Packet.AddEntityReturn.AddEntityReturn;
import Packet.EntityState.NewEntityState;
import Packet.GameEnd.ImpostorWin;
import Packet.NestedPane.RemoveNestedScreen;
import Packet.Position.*;
import StartUpServer.AppServer;

import java.util.*;
import java.util.stream.Collectors;

import Entity.DeadBody;
import Entity.EntityRegistryServer;
import Entity.*;
import TimerHandler.TimerStarter;

import static StartUpServer.AppServer.currentGame;

public class ImposterActionsSystem extends BaseSystem {

//    private static final List<Player> ghosts = new ArrayList<>();


    @Override
    public void update() {
    }

    @Override
    public void handleAction(Player player, PosRequest packet) {
        if (packet.isKillKey() && player.hasComponent(ImposterComp.class)) {
            if (player.getComponent(ImposterComp.class).isAbleToKill()) {
                handleKillAction(player);
//                checkImpostorWin();
            }
        }
    }

    private void checkImpostorWin() {
        if (getAlivePlayers().size() == 2) {
            ConnectionServer.sendTCPToAllPlayers(new ImpostorWin());
            AppServer.currentGame.stopGame();
        }
    }

//    public void handleSpecialActions(Player player, PosRequest packet) {
//        if (packet.isKillKey() && player.hasComponent(ImposterComp.class)) {
//            if (player.getComponent(ImposterComp.class).isAbleToKill()) {
//                handleKillAction(player);
//            }
//        }
//    }

    private void handleKillAction(Player imposter) {
        Optional<Player> crewMateOptional = getCrewMateToKill(imposter, getCrewMates());
        crewMateOptional.ifPresent(crewMate -> {
            killCrewMate(crewMate);
            startKillImposterCoolDown(imposter);
        });
    }

    private Optional<Player> getCrewMateToKill(Player imposter, List<Player> crewMates) {
        Optional<Player> crewMateOptional = DistanceFinder.getClosestEntity(imposter, crewMates, 100);
        if (crewMateOptional.isPresent() && !crewMateOptional.get().getComponent(AliveComp.class).isAlive()) {
            crewMates.remove(crewMateOptional.get());
            crewMateOptional = getCrewMateToKill(imposter, crewMates);
        }
        return crewMateOptional;
    }

    public void killCrewMate(Player crewMate) {
        stopCrewMateTask(crewMate);
        setGhostAttributes(crewMate);
        sendDeadBodyToClients(crewMate);
        raiseGhost(crewMate);
        updateGhostForClients(crewMate);
    }

    private void registerDeadBody(DeadBody deadBody) {
        currentGame.getStateManager().getCurrentState().getSystem(ReportBodySystem.class).getDeadBodies().add(deadBody);
    }//todo this acc puts the real player, or his pos at least

    public void stopCrewMateTask(Player crewMate) {
        if (crewMate.getCurrentTask() != null) {
            ConnectionServer.sendTCP(new RemoveNestedScreen(), crewMate.getConnectionID());
            crewMate.getCurrentTask().setPlayer(null);
            crewMate.setCurrentTask(null);
        }
    }

    private void startKillImposterCoolDown(Player imposter) {
        ImposterComp imposterComp = imposter.getComponent(ImposterComp.class);
        imposterComp.setAbleToKill(false);
        TimerStarter.startTimer("KillCoolDownTimer", 5, () -> imposterComp.setAbleToKill(true), imposter.getConnectionID());
    }

    public void setGhostAttributes(Player crewMate) {
        crewMate.getComponent(AliveComp.class).setAlive(false);//todo remove comp dont set it to false
        crewMate.getComponent(AnimationComp.class).setCurrentAnimation(AnimState.GHOST_RIGHT);
        crewMate.removeComponent(HitBoxComp.class);
//        ghosts.add(crewMate);

    }

    public void updateGhostForClients(Player ghost) {
        clearGhostsForAlivePlayers(ghost);
        sendNewGhostExistingGhosts(ghost);
        currentGame.getEntityReturnBuffer().putEntity(ghost, getGhostConnectionIDs());
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
//        ConnectionServer.sendTCP(new AddChangingEntityReturn(newEntityStates), connectionID);
        ConnectionServer.sendTCP(new AddEntityReturn(newEntityStates), connectionID);

    }


    public void raiseGhost(Player ghost) {
        PosComp posComp = ghost.getComponent(PosComp.class);
        posComp.getPos().incrementY(-30);
    }


    private void sendDeadBodyToClients(Player crewMate) {
        ColourComp colourComp = crewMate.getComponent(ColourComp.class);
        PosComp posComp = crewMate.getComponent(PosComp.class);
        DeadBody deadBody = new DeadBody(colourComp.getColour(), new PosComp(posComp.getPos().getX(), posComp.getPos().getY(), posComp.getWidth(), posComp.getHeight()));//todo add this to the diary
        ConnectionServer.sendTCPToAllPlayers(new AddEntityReturn(deadBody.adaptToNewAnimatedEntityState(true)));
        registerDeadBody(deadBody);
    }

    //        DeadPlayer deadPlayer = new DeadPlayer(colourComp.getColour(), new PosComp(posComp.getPos(), posComp.getWidth(), posComp.getHeight()));//todo add this to the diary

    private boolean checkPlayerCanDie(Player player, Player impostor) {
        return player != impostor && player.getComponent(AliveComp.class).isAlive();
    }

    public List<Player> getCrewMates() {
        return currentGame.getPlayers().stream().
                filter(player -> !player.hasComponent(ImposterComp.class)).
                collect(Collectors.toList());

    }

    public List<Integer> getGhostConnectionIDs() {
        return ConnectionServer.getPlayerConnectionIDs(getGhostPlayers());
    }

    private static List<Player> getGhostPlayers() {
        return AppServer.getClients().stream().
                map(Client::getPlayer).
                filter(player -> !player.getComponent(AliveComp.class).isAlive()).
                collect(Collectors.toList());
    }


    public static List<Integer> getAliveConnectionIDs() {
        return ConnectionServer.getPlayerConnectionIDs(getAlivePlayers());
    }

    private static List<Player> getAlivePlayers() {
        return currentGame.getPlayers().stream().
                filter(player -> player.getComponent(AliveComp.class).isAlive()).
                collect(Collectors.toList());
    }
}





