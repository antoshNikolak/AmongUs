package ScreenController;

import ConnectionClient.ConnectionClient;
import Packet.GameStart.StartGameRequest;
import Packet.LeaderBoard.RequestLeaderBoard;
import Packet.Registration.LogoutRequest;
import Screen.EntryScreen;
import Screen.ScreenManager;
import javafx.scene.input.MouseEvent;

public class MenuScreenController {

    public void startGame() {//handle start game button pressed
        ConnectionClient.sendTCP(new StartGameRequest());
    }

    public void showLeaderBoard() {//handle show leader board button being pressed
        ConnectionClient.sendTCP(new RequestLeaderBoard());
    }

    public void logout() {//handle logout button being pressed
        ConnectionClient.sendTCP(new LogoutRequest());
        ScreenManager.activate(EntryScreen.class);
    }

}
