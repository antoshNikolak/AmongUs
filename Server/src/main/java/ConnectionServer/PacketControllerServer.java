package ConnectionServer;

import Animation.AnimationOver;
import AuthorizationServer.AuthorizationServer;
import Client.Client;
import Entity.Player;
import Packet.GameStart.StartGameReturn;
import Packet.Position.PosRequest;
import Packet.Registration.LoginRequest;
import Packet.Registration.RegistrationConfirmation;
import Packet.Registration.SignupRequest;
import Packet.Sound.Sound;
import StartUpServer.AppServer;
import State.State;
import SudokuPacket.VerifySudokuRequest;
import State.*;
import Voting.ImpostorVote;

import java.util.List;
import java.util.Optional;
import System.*;

public class PacketControllerServer {

    public void handleLogin(LoginRequest packet, int connectionID) {
        RegistrationConfirmation confirmation = AuthorizationServer.handleLogin(packet.getUserData());
        AuthorizationServer.handleRegistrationConfirmation(confirmation, connectionID);
    }

    public void handleSignup(SignupRequest packet, int connectionID) {
        RegistrationConfirmation confirmation = AuthorizationServer.handleSignup(packet.getUserData());
        AuthorizationServer.handleRegistrationConfirmation(confirmation, connectionID);
    }

    public void handleStartGameRequest(int connectionID) {
        Optional<Client> client = AuthorizationServer.getAuthorizedClient(connectionID);
        ConnectionServer.sendTCP(new StartGameReturn(client.isPresent()), connectionID);
        client.ifPresent(Client::prepareClientToPlay);

    }

    public void handlePosRequest(PosRequest packet, int connectionId) {
        Optional<Player> optionalPlayer = ConnectionServer.getPlayerFromConnectionID(AppServer.currentGame.getPlayers(), connectionId);
        if (optionalPlayer.isPresent()) {
            State state = getPlayerState(optionalPlayer.get());
            state.processInputSystems(optionalPlayer.get(), packet);
//            if (state.hasSystem(PhysicsSystem.class)) {
//                state.getSystem(PhysicsSystem.class).processPlayerMove(optionalPlayer.get(), packet);
//            }
//            if (state.hasSystem(ImposterActionsSystem.class)) {
//                state.getSystem(ImposterActionsSystem.class).handleSpecialActions(optionalPlayer.get(), packet);
//            }
//            if (state.hasSystem(CrewMateActionSystem.class)){
//                state.getSystem(CrewMateActionSystem.class).handleSpecialAction(optionalPlayer.get(), packet);
//            }
//            if (state.hasSystem(TaskSystem.class)) {
//                state.getSystem(TaskSystem.class).handleTaskAction(optionalPlayer.get(), packet);
//            }
        }
    }

    private State getPlayerState(Player player) {
        if (player.getCurrentTask() != null) {
            return player.getCurrentTask();
        }
        return AppServer.currentGame.getStateManager().getCurrentState();
    }

    public void handleVerifySudokuRequest(VerifySudokuRequest packet, int connectionID) {
        Optional<Player> playerOptional = ConnectionServer.getPlayerFromConnectionID(connectionID);
        playerOptional.ifPresent(player -> {
            SudokuTaskState sudokuTaskState = (SudokuTaskState) player.getCurrentTask();
            sudokuTaskState.handleCompletionVerification(packet.getSudoku());
        });
    }

    public void handleVoiceChat(Sound soundPacket, Integer connectionID) {
        List<Integer> connectionIDs = ConnectionServer.getClientConnectionIDs();
        connectionIDs.remove(connectionID);
        ConnectionServer.sendUDPTo(soundPacket, connectionIDs);
    }

    public void handleImpostorVote(ImpostorVote packet, int connectionID) {
        EmergencyTableSystem emergencyTableSystem = AppServer.currentGame.getStateManager().getCurrentState().getSystem(EmergencyTableSystem.class);
        Optional<Player> playerOptional = ConnectionServer.getPlayerFromConnectionID(connectionID);
        playerOptional.ifPresent(player -> emergencyTableSystem.processVote(player, packet.getCandidateTexture()));
    }

    public void handleAnimationOver(AnimationOver packet, int connectionID) {
        AppServer.currentGame.getStateManager().getCurrentState().getSystem(EmergencyTableSystem.class).onAnimationOver();
        //maybe observer patter would be useful
    }
}
