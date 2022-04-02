package ScreenController;

import AuthorizationClient.AuthorizationClient;
import Screen.LoginScreen;
import Screen.ScreenManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class EntryScreenController {

    public void login(){//handle login button being pressed
        ScreenManager.activate(LoginScreen.class);
        Button button = (Button) ScreenManager.getScreen(LoginScreen.class).getNode("proceed");
        button.setOnAction(e -> AuthorizationClient.login());
    }

    public void signup() {//handle sign up button being pressed
        ScreenManager.activate(LoginScreen.class);
        Button button = (Button) ScreenManager.getScreen(LoginScreen.class).getNode("proceed");
        button.setOnAction(e -> AuthorizationClient.signup());
    }
}
