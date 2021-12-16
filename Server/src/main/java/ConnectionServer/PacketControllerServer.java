package ConnectionServer;

import AuthorizationServer.AuthorizationServer;
import Client.Client;
import ClientScreenTracker.ScreenData;
import Entity.Player;
import Packet.GameStart.StartGameReturn;
import Packet.Position.PosRequest;
import Packet.Registration.LoginRequest;
import Packet.Registration.RegistrationConfirmation;
import Packet.Registration.SignupRequest;
import Packet.ScreenData.ScreenInfo;
import Packet.Sound.Sound;
import StartUpServer.AppServer;
import State.State;
import Packet.SudokuPacket.VerifySudokuRequest;
import State.*;
import Packet.UserData.UserData;
import Packet.Voting.ImpostorVote;

import java.util.List;
import java.util.Optional;

import System.*;

import static StartUpServer.AppServer.currentGame;

public class PacketControllerServer {

    public void handleLogin(LoginRequest packet, int connectionID) {
        if (ConnectionServer.getClientFromConnectionID(connectionID).isEmpty()) {//check this client has not already logged in
            UserData userData = packet.getUserData();
            RegistrationConfirmation confirmation = AuthorizationServer.handleLogin(userData);
            AuthorizationServer.handleRegistrationConfirmation(confirmation, userData, connectionID);
        }
    }

    public void handleSignup(SignupRequest packet, int connectionID) {
        UserData userData = packet.getUserData();
        RegistrationConfirmation confirmation = AuthorizationServer.handleSignup(userData);
        AuthorizationServer.handleRegistrationConfirmation(confirmation, userData, connectionID);
    }

    public void handleStartGameRequest(int connectionID) {
        if (currentGame != null && currentGame.getPlayers().size() == LobbyState.PLAYER_LIMIT) {
            ConnectionServer.sendTCP(new StartGameReturn(false), connectionID);//send back rejection
            return;//stops client from entering once game start
        }
        Optional<Client> clientOptional = ConnectionServer.getClientFromConnectionID(connectionID);
        if (clientOptional.isPresent() && clientOptional.get().getPlayer() != null) {
            return;//stops client from pressing button 2x to crash the game
        }
        ConnectionServer.sendTCP(new StartGameReturn(clientOptional.isPresent()), connectionID);//send back confirmation
        clientOptional.ifPresent(client -> {
            LobbyState.prepareGame();
            currentGame.getStateManager().getState(LobbyState.class).handleNewPlayerJoin(client);
        });

//        if (currentGame != null && currentGame.getPlayers().size() == LobbyState.PLAYER_LIMIT) {
//            ConnectionServer.sendTCP(new StartGameReturn(false), connectionID);//send back rejection
//            return;//stops client from entering once game start
//        }
//        Optional<Client> clientOptional = ConnectionServer.getClientFromConnectionID(connectionID);
//        if (clientOptional.isPresent() && clientOptional.get().getPlayer() != null) {
//            ConnectionServer.sendTCP(new StartGameReturn(false), connectionID);//send back rejection
//            return;//stops client from pressing button 2x to crash the game
//        }
//        ConnectionServer.sendTCP(new StartGameReturn(clientOptional.isPresent()), connectionID);//send back confirmation
//        clientOptional.ifPresent(client -> {
//            LobbyState.prepareGame();
//            currentGame.getStateManager().getState(LobbyState.class).handleNewPlayerJoin(client);
//        });
    }



    public void handlePosRequest(PosRequest packet, int connectionId) {
        Optional<Player> optionalPlayer = ConnectionServer.getPlayerFromConnectionID(currentGame.getPlayers(), connectionId);
        if (optionalPlayer.isPresent()) {
            State state = getPlayerState(optionalPlayer.get());
            state.processPlayingSystems(optionalPlayer.get(), packet);
        }
    }

    private State getPlayerState(Player player) {
        if (player.getCurrentTask() != null) {
            return player.getCurrentTask();
        }
        return currentGame.getStateManager().getCurrentState();
    }

    public void handleVerifySudokuRequest(VerifySudokuRequest packet, int connectionID) {
        Optional<Player> playerOptional = ConnectionServer.getPlayerFromConnectionID(connectionID);
        playerOptional.ifPresent(player -> {
            SudokuTaskState sudokuTaskState = (SudokuTaskState) player.getCurrentTask();
            sudokuTaskState.handleCompletionVerification(packet.getSudoku(), player.getClient());
        });
    }

    public void handleVoiceChat(Sound soundPacket, Integer connectionID) {
        List<Integer> connectionIDs = ConnectionServer.getClientConnectionIDs();
        connectionIDs.remove(connectionID);
        ConnectionServer.sendUDP(soundPacket, connectionIDs);
    }

    public void handleImpostorVote(ImpostorVote packet, int connectionID) {
        EmergencyTableSystem emergencyTableSystem = currentGame.getStateManager().getCurrentState().getSystem(EmergencyTableSystem.class);
        Optional<Player> playerOptional = ConnectionServer.getPlayerFromConnectionID(connectionID);
        playerOptional.ifPresent(player -> emergencyTableSystem.getVoteHandler().registerVote(player, packet.getVoteOption()));
    }

    public void handleLogout(int id) {
        Optional<Client> playerOptional = ConnectionServer.getClientFromConnectionID(id);
        playerOptional.ifPresent(client -> {//todo redundant authorized client list
            System.out.println("removing client");
            AppServer.getClients().remove(client);
        });
    }

    public void handleScreenInfo(ScreenInfo packet) {
        ScreenData.HEIGHT = packet.getHeight();
        ScreenData.WIDTH = packet.getWidth();
    }

//    public void handleAnimationOver(AnimationOver packet, int connectionID) {
////        EmergencyTableSystem s = AppServer.currentGame.getStateManager().getCurrentState().getSystem(EmergencyTableSystem.class);//todo NULL POINTER SYSTEM
////        //s is null
////        //todo this gets called twice, because each player sends an anim over request
//////        System.out.println("E"+s);
//////        System.out.println(AppServer.currentGame.getStateManager().getCurrentState().getSystem(EmergencyTableSystem.class));
////        s.onAnimationOver();
////        AppServer.currentGame.getStateManager().getCurrentState().removeSystem(EmergencyTableSystem.class);
//    }
}
