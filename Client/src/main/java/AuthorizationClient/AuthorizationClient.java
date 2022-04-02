package AuthorizationClient;

import Packet.Registration.LoginRequest;
import Packet.Registration.SignupRequest;
import Screen.LoginScreen;
import Screen.ScreenManager;
import Packet.UserData.UserData;
import ConnectionClient.ConnectionClient;
import javafx.scene.control.TextField;

public class AuthorizationClient {

    public static void login() {//takes user input and send a login request to server
        UserData userData = retrieveUserData();
        ConnectionClient.sendTCP(new LoginRequest(userData));
    }

    public static void signup(){//takes user input and send a sign up request to server
        UserData userData = retrieveUserData();
        ConnectionClient.sendTCP(new SignupRequest(userData));
    }

    private static UserData retrieveUserData(){//returns user input
        TextField textFieldUserName = (TextField) ScreenManager.getScreen(LoginScreen.class).getNode("userName");
        String userName = textFieldUserName.getText();
        TextField textFieldPassword = (TextField) ScreenManager.getScreen(LoginScreen.class).getNode("password");
        String password = textFieldPassword.getText();
        return new UserData(userName, password);
    }


}
