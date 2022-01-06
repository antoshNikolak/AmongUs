package ScreenController;

import ConnectionClient.ConnectionClient;
import Packet.GameStart.StartGameRequest;
import Packet.LeaderBoard.RequestLeaderBoard;
import Packet.Registration.LogoutRequest;
import Screen.EntryScreen;
import Screen.ScreenManager;
import javafx.scene.input.MouseEvent;

public class MenuScreenController {

    public void startGame() {
        ConnectionClient.sendTCP(new StartGameRequest());
    }

    public void showLeaderBoard() {
        System.out.println("showing leaderboard");
        ConnectionClient.sendTCP(new RequestLeaderBoard());
    }

    public void logout() {
        ConnectionClient.sendTCP(new LogoutRequest());
        ScreenManager.activate(EntryScreen.class);
    }

}
