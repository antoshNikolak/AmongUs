package State;

import ClientScreenTracker.ScreenData;
import Component.AliveComp;
import Component.ImpostorComp;
import Component.PosComp;
import Component.RespawnComp;
import ConnectionServer.ConnectionServer;
import DistanceFinder.DistanceFinder;
import EndGameHandler.EndGameHandler;
import Entity.Player;
import Entity.Tile;
import Packet.NestedPane.AddVotingPane;
import Position.Pos;
import StartUpServer.AppServer;
import VoteHandler.*;
import System.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static StartUpServer.AppServer.currentGame;

public class MeetingState extends PlayingState {
    //Only use this class i game class is on the stack before it

    private VoteHandler voteHandler;

    public static void checkStateValid(Player player) {
        PlayingState gameState = currentGame.getStateManager().getState(GameState.class);
        if (gameState == null) return;//state can only be pushed on top of a game state
        if (currentGame.getStateManager().hasState(MeetingState.class)) return;//ensure players arent already in a meeting
        Tile mainTable = gameState.getWorld().getMainTable();
        double distance = DistanceFinder.getDistanceBetweenEntities(player, mainTable).getDistance();//find distance between player and meeting table
        if (distance <= 250 && player.getComponent(AliveComp.class).isAlive()) {//check player in range and isn't a ghost
            currentGame.getStateManager().pushState(new MeetingState());//push new state
        }
    }

    @Override
    protected void createWorld() {
        broadcastVotingPanel();
    }

    @Override
    public void init() {
        teleportPlayersToEmergencyTable();//gather players around meeting table
        eraseDeadBodies();//remove dead bodies from map
        stopPlayerTasks();//force stop any tasks the players are in
        createWorld();//broadcasts voting panel
    }

    private void eraseDeadBodies() {
        currentGame.getStateManager().getState(GameState.class).getSystem(ReportBodySystem.class).removeDeadBodies();
    }

    private void stopPlayerTasks() {
        currentGame.getPlayers().forEach(Player::stopTask);
    }


    private void startUpVoteHandler() {
        this.voteHandler = new VoteHandler();
        this.voteHandler.startVotingTimer();
    }

    @Override
    public void close() {
        Optional<Player> playerOptional = voteHandler.getPlayerWithMostVotes();
        if (playerOptional.isPresent()) {
            Player suspect = playerOptional.get();
            ejectPlayer(suspect);//turn player with most votes into a ghost
            checkEndGame(suspect);//check in-case someone has won the game
        }
    }

    private void checkEndGame(Player suspect) {
        EndGameHandler endGameHandler = currentGame.getStateManager().getState(GameState.class).getEndGameHandler();
        if (suspect.hasComponent(ImpostorComp.class)) {
            endGameHandler.handleCrewWin();
        } else {
            endGameHandler.checkImpostorWin();
        }
    }

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

    private void broadcastVotingPanel() {
        this.voteHandler = new VoteHandler();
        for (Player player : currentGame.getPlayers()) {
            ConnectionServer.sendTCP(voteHandler.createVotingPane(), player.getConnectionID());
        }
        this.voteHandler.startVotingTimer();
    }

    public VoteHandler getVoteHandler() {
        return voteHandler;
    }

    @Override
    protected void startSystems() {}


}
