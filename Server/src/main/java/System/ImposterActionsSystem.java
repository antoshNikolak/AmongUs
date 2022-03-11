package System;

import ClientScreenTracker.ScreenData;
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
import Entity.*;
import Registry.RegistryHandler;
import StartUpServer.AppServer;
import State.GameState;
import TimerHandler.CounterStarter;

import static StartUpServer.AppServer.currentGame;

public class ImposterActionsSystem extends BaseSystem {

//    private static final List<Player> ghosts = new ArrayList<>();


    @Override
    public void update() {
    }

    @Override
    public void handleAction(Player player, InputRequest packet) {
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
        setGhostAttributes(crewMate);//turn crew mate into a ghost
        crewMate.stopTask();//force exit current task crewmate is doing
        sendDeadBodyToClients(crewMate);//broadcast dead body so players can render it
        raiseGhost(crewMate);//decrement y value of dead player
        updateGhostForClients(crewMate);//update player visibility to others
    }

    private void registerDeadBody(DeadBody deadBody) {
        currentGame.getStateManager().getTopState().getSystem(ReportBodySystem.class).getDeadBodies().add(deadBody);
    }

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
        CounterStarter.startCountDown( 20, ScreenData.WIDTH- 60, ScreenData.HEIGHT - 50, 50, () -> impostorComp.setAbleToKill(true), imposter.getConnectionID());
    }

    public void setGhostAttributes(Player crewMate) {
        crewMate.getComponent(AliveComp.class).setAlive(false);
        crewMate.getComponent(AnimationComp.class).setCurrentAnimation(AnimState.GHOST_RIGHT);
        crewMate.removeComponent(HitBoxComp.class);
    }

    public void updateGhostForClients(Player ghost) {
        clearGhostsForAlivePlayers(ghost);
        sendNewGhostExistingGhosts(ghost);
        currentGame.getEntityReturnBuffer().putEntity(ghost, getGhostConnectionIDs());
    }

    private void clearGhostsForAlivePlayers(Player ghost) {
        for (Player player: getAlivePlayers()) {
            ConnectionServer.sendTCP(new ClearEntityReturn(RegistryHandler.entityRegistryServer.getItemID(ghost)), player.getConnectionID());
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





