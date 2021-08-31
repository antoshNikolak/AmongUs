package Entity;

import Client.Client;
import ConnectionServer.ConnectionServer;
import Game.Game;
import Packet.Timer.GameStartTimerReturn;
import StartUpServer.AppServer;
import State.GameState;
import State.LobbyState;

import java.util.Timer;
import java.util.TimerTask;

public class GameClientHandler {

    public static void prepareClientToPlay(Client client){
        prepareGame();
        client.createPlayer();
        addClientToLobby(client);
        checkGameStateStart();
    }

    private static void checkGameStateStart(){
        if (AppServer.currentGame.getClients().size() < 2)return;
        new Timer().scheduleAtFixedRate(new TimerTask() {
            private int counter = 2;
            @Override
            public void run() {
                counter--;
                ConnectionServer.sendTCPToAllPlayers(new GameStartTimerReturn(counter));
                if (counter == 0){
                    startGameState();
                    cancel();
                }
            }
        }, 0, 1000);
    }

    private static void startGameState(){
        AppServer.currentGame.getStateManager().popState();
        AppServer.currentGame.getStateManager().pushState(new GameState());
    }

    private static void addClientToLobby(Client client) {
        if(!(AppServer.currentGame.getStateManager().getCurrentState() instanceof  LobbyState))throw  new IllegalStateException();
         ((LobbyState)AppServer.currentGame.getStateManager().getCurrentState()).handleNewPlayerJoin(client);
    }

    private static void prepareGame() {
        if (AppServer.currentGame == null) {
            AppServer.currentGame = new Game();
            AppServer.currentGame.startGame();
        }
    }

}
