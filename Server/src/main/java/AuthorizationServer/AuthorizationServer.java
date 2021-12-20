package AuthorizationServer;

import Client.Client;
import ConnectionServer.ConnectionServer;
import DataBase.DataBaseUtil;
import Packet.Registration.RegistrationConfirmation;
import StartUpServer.AppServer;
import Packet.UserData.UserData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Client.ClientOperator;

public final class AuthorizationServer implements ClientOperator {

//    private static final List<Client> authorizedUsers = new ArrayList<>();
    public static final Map<Client, UserData> clientUserDataMap = new HashMap<>();

    public static RegistrationConfirmation handleLogin(UserData userData) {
        boolean isRegistered = doesAccountAlreadyExists(userData);
        return new RegistrationConfirmation(getLoginFailMessage(isRegistered));
    }

    private static String getLoginFailMessage(boolean doesAccountAlreadyExist){
        return doesAccountAlreadyExist? "": "username or password is incorrect";
    }

    private static boolean doesAccountAlreadyExists(UserData userData) {
        List<UserData> matchingAccounts = DataBaseUtil.getUserDataList(userData);
        return matchingAccounts.size() == 1;
    }

    public static RegistrationConfirmation handleSignup(UserData userData) {
        boolean usernameAvailable = !(DataBaseUtil.doesUsernameExist(userData.getUserName()));
        if (usernameAvailable) DataBaseUtil.addUserToTable(userData);
        return new RegistrationConfirmation(getSignUpFailMessage(userData, usernameAvailable));
    }

    private static String getSignUpFailMessage(UserData userData, boolean userNameAvailable){
        if (!userNameAvailable){
            return "username already exists";
        }else if (userData.getUserName().length() >=12){                                              //todo alter table in DB to varhcar(12)
            return "user name is too long";
        }
        return "";//return empty string
    }

    public static void handleRegistrationConfirmation(RegistrationConfirmation registrationConfirmation, UserData userData, int connectionID) {
        if (registrationConfirmation.isAuthorized()) {
            Client client = new Client(connectionID);
            AppServer.getClients().add(client);
            clientUserDataMap.put(client, userData);
        }
        ConnectionServer.sendTCP(registrationConfirmation, connectionID);
    }

//    public static Optional<Client> getAuthorizedClient(int connectionID) {
//        return ConnectionServer.getClientFromConnectionID(authorizedUsers, connectionID);
//    }

//    public static void removeAuthorizedClient(Client client) {
//        authorizedUsers.remove(client);
//    }





//    private static void addClientToLobby(Client client) {
//        LobbyState lobbyState = (LobbyState) AppServer.currentGame.getStateManager().getCurrentState();
//        lobbyState.handleNewPlayerJoin(client);
//    }

    @Override
    public void removeClient(Client client) {
//        authorizedUsers.remove(client);
    }

}
