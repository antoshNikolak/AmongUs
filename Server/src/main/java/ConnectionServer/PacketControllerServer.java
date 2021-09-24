package ConnectionServer;

import AuthorizationServer.AuthorizationServer;
import Client.Client;
import Entity.Entity;
import Entity.GameClientHandler;
import Entity.Player;
import Packet.GameStart.StartGameReturn;
import Packet.Position.PosRequest;
import Packet.Registration.LoginRequest;
import Packet.Registration.RegistrationConfirmation;
import Packet.Registration.SignupRequest;
import StartUpServer.AppServer;
import State.State;
import System.PhysicsSystem;
import System.ImposterActionsSystem;
import System.*;
import State.*;

import java.util.Optional;

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
            if (state.hasSystem(PhysicsSystem.class)) {
                state.getSystem(PhysicsSystem.class).processPlayerMove(optionalPlayer.get(), packet);
            }
            if (state.hasSystem(ImposterActionsSystem.class)) {
                state.getSystem(ImposterActionsSystem.class).handleSpecialActions(optionalPlayer.get(), packet);
            }
            if (state.hasSystem(TaskSystem.class)) {
                state.getSystem(TaskSystem.class).handleTaskAction(optionalPlayer.get(), packet);
            }
        }
    }

    private State getPlayerState(Player player) {
        if (player.getCurrentTask() != null) {
            return player.getCurrentTask();
        }
        return AppServer.currentGame.getStateManager().getCurrentState();
    }
}
