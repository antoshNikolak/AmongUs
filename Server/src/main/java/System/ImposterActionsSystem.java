package System;

import EndGameHandler.EndGameHandler;
import Packet.Animation.AnimState;
import Component.*;
import ConnectionServer.ConnectionServer;
import DistanceFinder.DistanceFinder;
import Entity.Player;
//import Packet.AddEntityReturn.AddChangingEntityReturn;
import Packet.AddEntityReturn.AddEntityReturn;
import Packet.EntityState.NewEntityState;
import Packet.NestedPane.RemoveNestedScreen;
import Packet.Position.*;

import java.util.*;
import java.util.stream.Collectors;

import Entity.DeadBody;
import Entity.EntityRegistryServer;
import Entity.*;
import StartUpServer.AppServer;
import State.GameState;
import TimerHandler.TimerStarter;

import static StartUpServer.AppServer.currentGame;

public class ImposterActionsSystem extends BaseSystem {

//    private static final List<Player> ghosts = new ArrayList<>();


    @Override
    public void update() {
    }

    @Override
    public void handleAction(Player player, PosRequest packet) {
        if (packet.isKillKey() && player.hasComponent(ImpostorComp.class)) {
            if (player.getComponent(ImpostorComp.class).isAbleToKill()) {
                handleKillAction(player);
//                EndGameHandler.checkImpostorWin(getAlivePlayers().size());
            }
        }
    }

//    private void checkImpostorWin() {
//        if (getAlivePlayers().size() == 2) {
//            System.out.println("IMPOSTOR WON");
//            recordImpostorWin();
//            ConnectionServer.sendTCPToAllPlayers(new ImpostorWin());
//            AppServer.currentGame.stopGame();
//        }
//    }



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
            AppServer.currentGame.getStateManager().getState(GameState.class).getEndGameHandler().checkImpostorWin();
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
        setGhostAttributes(crewMate);
        crewMate.stopTask();
//        stopCrewMateTask(crewMate);
        sendDeadBodyToClients(crewMate);
        raiseGhost(crewMate);
        updateGhostForClients(crewMate);
    }

    private void registerDeadBody(DeadBody deadBody) {
        currentGame.getStateManager().getTopState().getSystem(ReportBodySystem.class).getDeadBodies().add(deadBody);
    }//todo this acc puts the real player, or his pos at least

    public void stopCrewMateTask(Player crewMate) {//MIGRATE TO PLAYER CLASS
        if (crewMate.getCurrentTask() != null) {
            ConnectionServer.sendTCP(new RemoveNestedScreen(), crewMate.getConnectionID());
            crewMate.getCurrentTask().setPlayer(null);
            crewMate.setCurrentTask(null);
        }
    }

    private void startKillImposterCoolDown(Player imposter) {
        ImpostorComp impostorComp = imposter.getComponent(ImpostorComp.class);
        impostorComp.setAbleToKill(false);
        TimerStarter.startTimer("KillCoolDownTimer", 40, () -> impostorComp.setAbleToKill(true), imposter.getConnectionID());
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
        System.out.println("Placing ghost in entity buffer");
        System.out.println("number of existing ghosts: "+ getGhostConnectionIDs().size());
        currentGame.getEntityReturnBuffer().putEntity(ghost, getGhostConnectionIDs());
    }

    private void clearGhostsForAlivePlayers(Player ghost) {
        for (Player player: getAlivePlayers()) {
            ConnectionServer.sendTCP(new ClearEntityReturn(EntityRegistryServer.getEntityID(ghost)), player.getConnectionID());
        }
    }

    private void sendNewGhostExistingGhosts(Player ghost) {
        List<Player> otherGhosts = getGhostPlayers();
        otherGhosts.remove(ghost);
        List<NewEntityState> newEntityStates = new ArrayList<>(EntityReturnBuffer.adaptCollectionToNewEntityStates(otherGhosts));
        ConnectionServer.sendTCP(new AddEntityReturn(newEntityStates),ghost.getConnectionID());

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



    public List<Player> getCrewMates() {
        return currentGame.getPlayers().stream().
                filter(player -> !player.hasComponent(ImpostorComp.class)).
                collect(Collectors.toList());

    }

    public List<Integer> getGhostConnectionIDs() {
        return ConnectionServer.getPlayerConnectionIDs(getGhostPlayers());
    }

    private static List<Player> getGhostPlayers() {
        return currentGame.getPlayers().stream().
                filter(player -> !player.getComponent(AliveComp.class).isAlive()).
                collect(Collectors.toList());
//        return A.stream().
//                map(Client::getPlayer).
//                filter(player -> !player.getComponent(AliveComp.class).isAlive()).
//                collect(Collectors.toList());
    }


//    public  List<Integer> getAliveConnectionIDs() {
//        return ConnectionServer.getPlayerConnectionIDs(getAlivePlayers());
//    }

    public  List<Player> getAlivePlayers() {
        return currentGame.getPlayers().stream().
                filter(player -> player.getComponent(AliveComp.class).isAlive()).
                collect(Collectors.toList());
    }
}





