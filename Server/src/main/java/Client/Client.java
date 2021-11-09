package Client;

import Entity.GameClientHandler;
import Entity.Player;
import Game.Game;
import PlayerColourManager.PlayerColourFactory;
import StartUpServer.AppServer;
import State.GameState;
import State.LobbyState;
import TimerHandler.TimerStarter;

import java.util.concurrent.ConcurrentHashMap;

public class Client {

    private final int connectionID;
    private Player player;
    private boolean inGame = false;

    public static void prepareClientToPlay(Client client) {
        prepareGame();
        client.createPlayer();
        AppServer.currentGame.getStateManager().getState(LobbyState.class).handleNewPlayerJoin(client);
        if (AppServer.currentGame.getClients().size() == 2) {
            TimerStarter.startTimer("GameStartTimer", 5, () -> startGameState());
        }
    }

    public static void startGameState() {
        synchronized (AppServer.currentGame.getStateManager()) {
            AppServer.currentGame.getStateManager().popState();
            AppServer.currentGame.getStateManager().pushState(new GameState());
        }
    }

    private static void prepareGame() {
        if (AppServer.currentGame == null) {
            AppServer.currentGame = new Game();
            AppServer.currentGame.startGame();
        }
    }

    public void createPlayer() {
        this.player = new Player(PlayerColourFactory.getRandomColour(), connectionID);
        AppServer.currentGame.getStateManager().getCurrentState().getEntities().add(player);

    }

    public Client(int connectionID) {
        this.connectionID = connectionID;
    }

    public int getConnectionID() {
        return connectionID;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isInGame() {
        return inGame;
    }

    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }
}
