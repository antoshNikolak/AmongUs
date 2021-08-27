package StartUpServer;

import ConnectionServer.ConnectionServer;
import Game.Game;

public class AppServer {

    public static Game currentGame;

    public static void main(String[] args) {
        ConnectionServer.start();
    }
}
