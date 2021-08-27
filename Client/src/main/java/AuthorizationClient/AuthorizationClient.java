package AuthorizationClient;

import Packet.Registration.LoginRequest;
import Packet.Registration.SignupRequest;
import Screen.LoginScreen;
import Screen.ScreenManager;
import UserData.UserData;
import ConnectionClient.ConnectionClient;
import javafx.scene.control.TextField;

public class AuthorizationClient {

    public static void login() {
        UserData userData = retrieveUserData();
        ConnectionClient.sendTCP(new LoginRequest(userData));
    }

    public static void signup(){
        UserData userData = retrieveUserData();
        ConnectionClient.sendTCP(new SignupRequest(userData));
    }

    private static UserData retrieveUserData(){
        TextField textFieldUserName = (TextField) ScreenManager.getNode(LoginScreen.class, "userName");
        String userName = textFieldUserName.getText();
        TextField textFieldPassword = (TextField) ScreenManager.getNode(LoginScreen.class, "password");
        String password = textFieldPassword.getText();
        return new UserData(userName, password);
    }


}
