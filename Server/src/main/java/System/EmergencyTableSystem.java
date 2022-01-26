package System;

import AnimationFactory.AnimationFactory;
import Component.*;
import ConnectionServer.ConnectionServer;
import DistanceFinder.DistanceFinder;
import EndGameHandler.EndGameHandler;
import Entity.Player;
import Packet.GameEnd.CrewWin;
import Packet.GameEnd.GameEnd;
import Packet.Position.PosRequest;
import Position.Pos;
import StartUpServer.AppServer;
import State.State;
import VoteHandler.VoteHandler;
import Entity.Tile;
import State.*;

import java.util.*;

import static StartUpServer.AppServer.currentGame;

//public class EmergencyTableSystem extends BaseSystem {
//    private VoteHandler voteHandler;
//
//
//
//    public EmergencyTableSystem() {
//    }
//
//    @Override
//    public void update() {
//    }
//
//    @Override
//    public void handleAction(Player player, PosRequest packet) {
//        System.out.println("HANDLING ACTION");
//        PlayingState state = currentGame.getStateManager().getCurrentState();
//        if (state.hasSystem(getClass())) return;
//        Tile mainTable = state.getWorld().getMainTable();
//        double distance = DistanceFinder.getDistanceBetweenEntities(player, mainTable).getDistance();
//        if (distance <= 250 && player.getComponent(AliveComp.class).isAlive()) {
//            activate();
//        }
//    }
//
//    public void activate() {
//        currentGame.getStateManager().getCurrentState().addSystem(this);
//        teleportPlayersToEmergencyTable();
//        immobilisePlayers();
//        eraseDeadBodies();
//        stopPlayerTasks();
//        broadcastVotingPanel();
//        enableVoiceChat();
//    }
//
//    private void eraseDeadBodies() {
//        currentGame.getStateManager().getCurrentState().getSystem(ReportBodySystem.class).removeDeadBodies();
//    }
//
//    private void stopPlayerTasks() {
//        for (Player player : currentGame.getPlayers()) {
//            currentGame.getStateManager().getCurrentState().getSystem(ImposterActionsSystem.class).stopCrewMateTask(player);
//        }
//    }
//
//
//    private void startUpVoteHandler() {
////        this.voteHandler = new VoteHandler(this);
//        this.voteHandler.startVotingTimer();
//    }
//
//    public void onVoteTimerOver() {
//        Optional<Player> playerOptional = voteHandler.getPlayerWithMostVotes();
//        playerOptional.ifPresent(this::ejectPlayer);
//        mobilisePlayers();
//        disableVoiceChat();
//        removeThisFromSystems();
//    }
//
//    public void removeThisFromSystems() {//todo talk about concurrent modification
//        currentGame.getStateManager().getCurrentState().removeSystem(getClass());
//    }
//
//    private void ejectPlayer(Player player) {
//        if (player.hasComponent(ImposterComp.class))EndGameHandler.handleCrewWin();
//        ImposterActionsSystem killHandler = currentGame.getStateManager().getCurrentState().getSystem(ImposterActionsSystem.class);
//        killHandler.setGhostAttributes(player);
//        killHandler.updateGhostForClients(player);
//
//    }
//
//    private void teleportPlayersToEmergencyTable() {
//        for (Player player : currentGame.getPlayers()) {
//            Pos respawnPos = player.getComponent(RespawnComp.class).getPos();
//            player.getComponent(PosComp.class).setPos(respawnPos);
//            currentGame.getEntityReturnBuffer().putEntity(player);
//        }
//    }
//
//    private void immobilisePlayers() {
//        currentGame.getStateManager().getCurrentState().removeSystem(PhysicsSystem.class);
//    }
//
//    private void mobilisePlayers() {
//        State currentState = currentGame.getStateManager().getCurrentState();
//        currentState.addSystem(new PhysicsSystem(currentState.getEntities()));
//    }
//
//    private void broadcastDeadBodyReportedAnimation() {
//        for (Player player : currentGame.getPlayers()) {
//            ConnectionServer.sendTCP(AnimationFactory.createAnimationDisplayReturn("dead-body-reported"), player.getConnectionID());
//        }
//    }
//
//    private void broadcastVotingPanel() {
//        startUpVoteHandler();
//        for (Player player : currentGame.getPlayers()) {
//            ConnectionServer.sendTCP(voteHandler.createVotingPane(), player.getConnectionID());
//        }
//    }
//
//    private void enableVoiceChat() {
//        ConnectionServer.sendTCPToAllPlayers(new OpenRecordHandler());
//    }
//
//    private void disableVoiceChat() {
//        ConnectionServer.sendTCPToAllPlayers(new CloseRecordHandler());
//    }
//
//    public VoteHandler getVoteHandler() {
//        return voteHandler;
//    }
//}
//
//
