package AuthorizationServer;

import Client.Client;
import ConnectionServer.ConnectionServer;
import DataBase.DataBaseUtil;
import Packet.Registration.RegistrationConfirmation;
import StartUpServer.AppServer;
import State.LobbyState;
import UserData.UserData;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import ConnectionServer.ClientOperator;

public final class AuthorizationServer implements ClientOperator {

    private static final List<Client> authorizedUsers = new ArrayList<>();

    public static RegistrationConfirmation handleLogin(UserData userData) {
        return new RegistrationConfirmation(doesAccountAlreadyExists(userData));
    }

    private static boolean doesAccountAlreadyExists(UserData userData) {
        List<UserData> matchingAccounts = DataBaseUtil.getUserDataList(userData);
        return matchingAccounts.size() == 1;
    }

    public static RegistrationConfirmation handleSignup(UserData userData) {
        boolean usernameAvailable = !(DataBaseUtil.doesUsernameExist(userData.getUserName()));
        if (usernameAvailable) DataBaseUtil.addUserToTable(userData);
        return new RegistrationConfirmation(usernameAvailable);
    }

    public static void handleRegistrationConfirmation(RegistrationConfirmation registrationConfirmation, int connectionID) {
        if (registrationConfirmation.isAuthorized()) authorizedUsers.add(new Client(connectionID));
        ConnectionServer.sendTCP(registrationConfirmation, connectionID);
    }

    public static Optional<Client> getAuthorizedClient(int connectionID) {
        return ConnectionServer.getClientFromConnectionID(authorizedUsers, connectionID);
    }



//    private static void addClientToLobby(Client client) {
//        LobbyState lobbyState = (LobbyState) AppServer.currentGame.getStateManager().getCurrentState();
//        lobbyState.handleNewPlayerJoin(client);
//    }

    @Override
    public void removeClient(Client client) {
        authorizedUsers.remove(client);
    }

}
