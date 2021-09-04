package ConnectionServer;

import AuthorizationServer.AuthorizationServer;
import Client.Client;
import Entity.GameClientHandler;
import Packet.GameStart.StartGameReturn;
import Packet.Position.PosRequest;
import Packet.Registration.LoginRequest;
import Packet.Registration.RegistrationConfirmation;
import Packet.Registration.SignupRequest;
import System.PhysicsSystem;
import System.ImposterActionsSystem;

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
        Optional<Client> optionalClient = PhysicsSystem.getPlayerOptional(connectionId);
        optionalClient.ifPresent(client-> {
            PhysicsSystem.processPlayerMove(optionalClient.get().getPlayer(), packet);
            ImposterActionsSystem.handleSpecialActions(optionalClient.get().getPlayer(), packet);
        });

//        PhysicsSystem.updatePlayerPosition(packet, connectionId);
//        GameLogicSystem.
    }
}
