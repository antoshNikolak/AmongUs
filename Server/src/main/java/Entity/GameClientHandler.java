package Entity;

import Client.Client;
import Game.Game;
import StartUpServer.AppServer;
import State.LobbyState;

public class GameClientHandler {

    public static void prepareClientToPlay(Client client){
        prepareGame();
        client.createPlayer();
        addClientToLobby(client);
    }

    private static void addClientToLobby(Client client) {
         ((LobbyState)AppServer.currentGame.getCurrentState()).handleNewPlayerJoin(client);
    }

    private static void prepareGame() {
        if (AppServer.currentGame == null) {
            AppServer.currentGame = new Game();
            AppServer.currentGame.startGame();
        }
    }

    public static String getRandomFreeTexture() {
        return "";
    }
}
