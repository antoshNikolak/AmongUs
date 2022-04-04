package ConnectionServer;

import AuthorizationServer.AuthorizationServer;
import Client.Client;
import ClientScreenTracker.ScreenData;
import Component.AliveComp;
import Component.AnimationComp;
import DataBase.DataBaseUtil;
import Entity.Player;
import Packet.Animation.AnimState;
import Packet.GameStart.StartGameReturn;
import Packet.LeaderBoard.*;
import Packet.Position.InputRequest;
import Packet.Registration.LoginRequest;
import Packet.Registration.RegistrationConfirmation;
import Packet.Registration.SignupRequest;
import Packet.ScreenData.ScreenInfo;
import Packet.Voting.ChatMessageRequest;
import Packet.Voting.ChatMessageReturn;
import StartUpServer.AppServer;
import State.State;
import Packet.SudokuPacket.VerifySudokuRequest;
import State.*;
import Packet.UserData.UserData;
import Packet.Voting.ImpostorVote;

import java.util.LinkedHashMap;
import java.util.Objects;

import static StartUpServer.AppServer.currentGame;

public class PacketControllerServer {

    public void handleLogin(LoginRequest packet, int connectionID) {
        if (ConnectionServer.getClientFromConnectionID(connectionID) == null) {//check this client has not already logged in
            UserData userData = packet.getUserData();
            RegistrationConfirmation confirmation = AuthorizationServer.handleLogin(userData);
            AuthorizationServer.handleRegistrationConfirmation(confirmation, userData, connectionID);//notify user if they have been authorized
        }
    }

    public void handleSignup(SignupRequest packet, int connectionID) {
        UserData userData = packet.getUserData();
        RegistrationConfirmation confirmation = AuthorizationServer.handleSignup(userData);
        AuthorizationServer.handleRegistrationConfirmation(confirmation, userData, connectionID);//notify user if they have been authorized
    }

    public void handleStartGameRequest(int connectionID) {
        if (currentGame != null && currentGame.getPlayers().size() == LobbyState.PLAYER_LIMIT) {
            ConnectionServer.sendTCP(new StartGameReturn(false), connectionID);//send back rejection
            return;//stops client from entering once game start
        }
        Client client = ConnectionServer.getClientFromConnectionID(connectionID);
        if (client != null && client.getPlayer() != null) {
            return;//stops client from pressing button 2x to crash the game
        }
        ConnectionServer.sendTCP(new StartGameReturn(client != null), connectionID);//send back confirmation
        LobbyState.prepareGame();
        currentGame.getStateManager().getState(LobbyState.class).handleNewPlayerJoin(Objects.requireNonNull(client));
    }

    public void handleKeyBoardInput(InputRequest packet, int connectionId) {
        Player player = Objects.requireNonNull(ConnectionServer.getPlayerFromConnectionID(currentGame.getPlayers(), connectionId));
        if (packet.isEmergencyMeetingKey()) {//check player pressed E
            MeetingState.checkStateValid(player);//push meeting state onto stack if correct conditions
        }
        State state = getPlayerState(player);
        state.processPlayingSystems(player, packet);

    }

    //returns state of player task
    private State getPlayerState(Player player) {
        if (player.getCurrentTask() != null) {
            return player.getCurrentTask();
        }
        return currentGame.getStateManager().getTopState();
    }

    public void handleVerifySudokuRequest(VerifySudokuRequest packet, int connectionID) {
        //check if sudoku client sent is correctly solved
        Player player = ConnectionServer.getPlayerFromConnectionID(connectionID);
//        playerOptional.ifPresent(player -> {
        SudokuTaskState sudokuTaskState = (SudokuTaskState) player.getCurrentTask();
        sudokuTaskState.handleCompletionVerification(packet.getSudoku(), player.getClient());
//        });
    }


    public void handleImpostorVote(ImpostorVote packet, int connectionID) {
        //handle player vote during meeting state
        MeetingState meetingState = currentGame.getStateManager().getState(MeetingState.class);
        if (meetingState == null) return;
        Player player = ConnectionServer.getPlayerFromConnectionID(connectionID);
        meetingState.getVoteHandler().registerVote(player, packet.getVoteOption());
    }

    public void handleLogout(int id) {//handle client logout request
        Client client = ConnectionServer.getClientFromConnectionID(id);
        AppServer.getClients().remove(client);
    }

    public void handleScreenInfo(ScreenInfo packet) {
        ScreenData.HEIGHT = packet.getHeight();
        ScreenData.WIDTH = packet.getWidth();
    }

    public void handleChatMessageRequest(ChatMessageRequest packet, int connectionID) {
        //handle chat message
        Player player = ConnectionServer.getPlayerFromConnectionID(connectionID);
        if (!player.getComponent(AliveComp.class).isAlive()) return;
        String colour = player.getComponent(AnimationComp.class).getAnimation(AnimState.RIGHT).getFrames()[0];
        ConnectionServer.sendTCPToAllPlayers(new ChatMessageReturn(packet.message, player.getNameTag(), colour));
    }

    public void handleLeaderBoardRequest(int connectionID) {
        //return top 10 players with fastest impostor win time, along with their win time
        LinkedHashMap<String, Double> leaderBoard = DataBaseUtil.getLeaderBoard();
        ConnectionServer.sendTCP(new LeaderBoardReturn(leaderBoard), connectionID);
    }

}
