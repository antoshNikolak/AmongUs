package AuthorizationServer;

import Client.Client;
import ConnectionServer.ConnectionServer;
import DataBase.DataBaseUtil;
import Packet.Registration.RegistrationConfirmation;
import StartUpServer.AppServer;
import Packet.UserData.UserData;

import java.util.List;

import Client.ClientOperator;

public final class AuthorizationServer implements ClientOperator {

//    public static final Map<Client, UserData> clientUserDataMap = new HashMap<>();

    public static RegistrationConfirmation handleLogin(UserData userData) {
//        boolean isRegistered = doesAccountAlreadyExists(userData);
        boolean isRegistered = DataBaseUtil.doesUserExist(userData);
        return new RegistrationConfirmation(getLoginFailMessage(userData, isRegistered));
    }

    private static String getLoginFailMessage(UserData userData, boolean doesAccountAlreadyExist){
        if (doesAccountAlreadyExist){
            if (isPlayerAlreadyInGame(userData)){
                return "user is already logged in";
            }
            return "";
        }else {
            return "username or password is incorrect";
        }
    }

    private static boolean isPlayerAlreadyInGame(UserData userData){
        //this didnt work for cool squad and hi from ilyoncars laptop
        for (Client client: AppServer.getClients()){
            if (client.getUserData().equals(userData)){
                return true;
            }
        }
        return false;
    }

    private static boolean doesAccountAlreadyExists(UserData userData) {
        return DataBaseUtil.doesUserExist(userData);
//        List<UserData> matchingAccounts = DataBaseUtil.doesUserExist(userData);
//        return matchingAccounts.size() == 1;
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
            Client client = new Client(userData, connectionID);
            AppServer.getClients().add(client);
//            clientUserDataMap.put(client, userData);
        }
        ConnectionServer.sendTCP(registrationConfirmation, connectionID);
    }

    @Override
    public void removeClient(Client client) {
//        clientUserDataMap.remove(client);
    }

}
