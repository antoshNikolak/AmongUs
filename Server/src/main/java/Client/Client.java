package Client;

import Entity.GameClientHandler;
import Entity.Player;
import Game.Game;
import PlayerColourManager.PlayerColourFactory;
import StartUpServer.AppServer;
import State.GameState;
import State.LobbyState;
import TimerHandler.TimerStarter;

public class Client {

    private final int connectionID;
    private Player player;

    public static void prepareClientToPlay(Client client) {
        prepareGame();
        client.createPlayer();
        addClientToLobby(client);
        if (AppServer.currentGame.getClients().size() == 2) {
            TimerStarter.startTimer("GameStartTimer", 5, () -> startGameState());
        }
    }

    public static void startGameState() {//todo ask if accessing this twice affect objects getting locks
        AppServer.currentGame.getStateManager().popState();
        AppServer.currentGame.getStateManager().pushState(new GameState());
    }

    private static void addClientToLobby(Client client) {
//        LobbyState.handleNewPlayerJoin();
//        if (!(AppServer.currentGame.getStateManager().getCurrentState() instanceof LobbyState))
//            throw new IllegalStateException();

        AppServer.currentGame.getStateManager().getState(LobbyState.class).handleNewPlayerJoin(client);
//        ((LobbyState) AppServer.currentGame.getStateManager().getCurrentState()).handleNewPlayerJoin(client);
    }

    private static void prepareGame() {
        if (AppServer.currentGame == null) {
            AppServer.currentGame = new Game();
            AppServer.currentGame.startGame();
        }
    }

    public void createPlayer() {
        this.player = new Player(PlayerColourFactory.getRandomColour(), connectionID);
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
}
