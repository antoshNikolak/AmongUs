package ScreenController;

import ConnectionClient.ConnectionClient;
import Packet.GameStart.StartGameRequest;

public class MenuScreenController {

    public void startGame() {
        ConnectionClient.sendTCP(new StartGameRequest()); //remember to revieve it
    }

    public void showLeaderBoard() {

    }
}
