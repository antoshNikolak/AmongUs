package AuthorizationServer;

import Client.Client;
import ConnectionServer.ConnectionServer;
import DataBase.DataBaseUtil;
import Packet.Registration.RegistrationConfirmation;
import StartUpServer.AppServer;
import Packet.UserData.UserData;

public final class AuthorizationServer {

    public static RegistrationConfirmation handleLogin(UserData userData) {
        boolean isRegistered = DataBaseUtil.doesUserExist(userData);//if details exist in data base, user is register to login
        return new RegistrationConfirmation(getLoginFailMessage(userData, isRegistered));
    }

    private static String getLoginFailMessage(UserData userData, boolean doesAccountAlreadyExist) {
        if (doesAccountAlreadyExist) {
            if (isPlayerAlreadyInGame(userData)) {
                return "user is already logged in";//a user with this username is currently in the game
            }
            return "";//user valid
        } else {
            return "username or password is incorrect";//details entered dont match to any record
        }
    }

    private static boolean isPlayerAlreadyInGame(UserData userData) {
        //check if client with username and password has logged in
        for (Client client : AppServer.getClients()) {
            if (client.getUserData().equals(userData)) {
                return true;
            }
        }
        return false;
    }

    public static RegistrationConfirmation handleSignup(UserData userData) {
        boolean usernameAvailable = !(DataBaseUtil.doesUsernameExist(userData.getUserName()));//check if user name is already taken
        if (usernameAvailable)
            DataBaseUtil.addUserToTable(userData);//if not already taken, insert new record with input
        return new RegistrationConfirmation(getSignUpFailMessage(userData, usernameAvailable));
    }

    private static String getSignUpFailMessage(UserData userData, boolean userNameAvailable) {
        if (!userNameAvailable) {
            return "username already exists";//username already taken by another user
        } else if (userData.getUserName().length() >= 12) {
            return "user name is too long";
        }
        return "";//return empty string, sign up valid
    }

    public static void handleRegistrationConfirmation(RegistrationConfirmation registrationConfirmation, UserData userData, int connectionID) {
        if (registrationConfirmation.isAuthorized()) {
            Client client = new Client(userData, connectionID);
            AppServer.getClients().add(client);//add new client to game
        }
        ConnectionServer.sendTCP(registrationConfirmation, connectionID);//notify client he is authorized
    }


}
