package State;

import Client.Client;
import Component.AliveComp;
import Component.ImposterComp;
import Component.PosComp;
import Component.RespawnComp;
import ConnectionServer.ConnectionServer;
import DistanceFinder.DistanceFinder;
import EndGameHandler.EndGameHandler;
import Entity.Player;
import Entity.Tile;
import Packet.Sound.CloseRecordHandler;
import Packet.Sound.OpenRecordHandler;
import Position.Pos;
import VoteHandler.VoteHandler;
import System.*;

import java.util.Optional;

import static StartUpServer.AppServer.currentGame;

public class MeetingState extends PlayingState {
    //Only use this class i game class is on the stack before it

    private VoteHandler voteHandler;

    public static void checkStateValid(Player player) {
        PlayingState gameState = currentGame.getStateManager().getState(GameState.class);
        if (gameState == null) return;
        if (currentGame.getStateManager().hasState(MeetingState.class)) return;
//        if (state.hasSystem(getClass())) return;
//        if (this == currentGame.getStateManager().getCurrentState())return false;
        Tile mainTable = gameState.getWorld().getMainTable();
        double distance = DistanceFinder.getDistanceBetweenEntities(player, mainTable).getDistance();
        if (distance <= 250 && player.getComponent(AliveComp.class).isAlive()) {
            currentGame.getStateManager().pushState(new MeetingState());
        }
    }

    @Override
    protected void createWorld() {
        broadcastVotingPanel();
    }

    @Override
    public void init() {
//        currentGame.getStateManager().getCurrentState().addSystem(this);
        teleportPlayersToEmergencyTable();
//        immobilisePlayers();
        eraseDeadBodies();
        stopPlayerTasks();
        createWorld();
        enableVoiceChat();
    }

    private void eraseDeadBodies() {
        currentGame.getStateManager().getState(GameState.class).getSystem(ReportBodySystem.class).removeDeadBodies();
    }

    private void stopPlayerTasks() {
        currentGame.getPlayers().forEach(Player::stopTask);
//        for (Player player : currentGame.getPlayers()) {//remove these because it assumes there is a state under this
//            player.stopTask();
////            currentGame.getStateManager().getState(GameState.class).getSystem(ImposterActionsSystem.class).stopCrewMateTask(player);
//        }
    }


    private void startUpVoteHandler() {
        this.voteHandler = new VoteHandler(this);
        this.voteHandler.startVotingTimer();
    }

    @Override
    public void close() {
        System.out.println("current game: " + currentGame);
        Optional<Player> playerOptional = voteHandler.getPlayerWithMostVotes();
//        mobilisePlayers();
        disableVoiceChat();
        if (playerOptional.isPresent()) {
            Player suspect = playerOptional.get();
            ejectPlayer(suspect);
            checkEndGame(suspect);
        }
//        playerOptional.ifPresent(this::ejectPlayer);
//        checkCrewWin();
    }

    private void checkEndGame(Player suspect) {
        if (suspect.hasComponent(ImposterComp.class)) {
            EndGameHandler.handleCrewWin();
        }else {
            EndGameHandler.checkImpostorWin();

        }
    }

//    public void onVoteTimerOver() {
//        Optional<Player> playerOptional = voteHandler.getPlayerWithMostVotes();
//        playerOptional.ifPresent(this::ejectPlayer);
//        mobilisePlayers();
//        disableVoiceChat();
////        removeThisFromSystems();
//    }

//    public void removeThisFromSystems() {//todo talk about concurrent modification
//        currentGame.getStateManager().getCurrentState().removeSystem(getClass());
//    }

    private void ejectPlayer(Player player) {
        ImposterActionsSystem killHandler = currentGame.getStateManager().getState(GameState.class).getSystem(ImposterActionsSystem.class);//null here
        killHandler.setGhostAttributes(player);
        killHandler.updateGhostForClients(player);

    }

    private void teleportPlayersToEmergencyTable() {
        for (Player player : currentGame.getPlayers()) {
            Pos respawnPos = player.getComponent(RespawnComp.class).getPos();
            player.getComponent(PosComp.class).setPos(respawnPos);
            currentGame.getEntityReturnBuffer().putEntity(player);
        }
    }

//    private void immobilisePlayers() {
//        currentGame.getStateManager().getCurrentState().removeSystem(PhysicsSystem.class);
//    }

//    private void mobilisePlayers() {
//        State currentState = currentGame.getStateManager().getCurrentState();
//        currentState.addSystem(new PhysicsSystem(currentState.getEntities()));
//    }

//    private void broadcastDeadBodyReportedAnimation() {
//        for (Player player : currentGame.getPlayers()) {
//            ConnectionServer.sendTCP(AnimationFactory.createAnimationDisplayReturn("dead-body-reported"), player.getConnectionID());
//        }
//    }

    private void broadcastVotingPanel() {
        startUpVoteHandler();
        for (Player player : currentGame.getPlayers()) {
            ConnectionServer.sendTCP(voteHandler.createVotingPane(), player.getConnectionID());
        }
    }

    private void enableVoiceChat() {
        ConnectionServer.sendTCPToAllPlayers(new OpenRecordHandler());
    }

    private void disableVoiceChat() {
        ConnectionServer.sendTCPToAllPlayers(new CloseRecordHandler());//migrate this as a system
    }

    public VoteHandler getVoteHandler() {
        return voteHandler;
    }

    @Override
    protected void startSystems() {

    }

    @Override
    public void removeClient(Client client) {

    }
}
