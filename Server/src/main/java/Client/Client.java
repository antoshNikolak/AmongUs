package Client;

import Entity.Player;
import PlayerColourManager.PlayerColourFactory;
import State.LobbyState;

import static StartUpServer.AppServer.currentGame;

public class Client {

    private final int connectionID;
    private Player player;
//    private boolean inGame = false;

//    public void prepareClientToPlay(Client client) {
////            prepareGame();
//            createPlayer();
//            currentGame.getStateManager().getState(LobbyState.class).handleNewPlayerJoin(client);
////        currentGame.getStateManager().getState(LobbyState.class).prepareClientToPlay(client);
////        }
////        if (AppServer.getClients().size() == 2) {
////            TimerStarter.startTimer("GameStartTimer", 5, () -> startGameState());
////        }
//    }

//    public static void startGameState() {
//        synchronized (currentGame.getStateManager()) {
//            currentGame.getStateManager().popState();
//            currentGame.getStateManager().pushState(new GameState());
//        }
//    }
//





//    private static void prepareGame() {
//        if (currentGame == null) {
//            currentGame = new Game();
//            currentGame.startGame();
//        }
//    }

    public void createPlayer() {
        PlayerColourFactory colourFactory = currentGame.getStateManager().getState(LobbyState.class).getPlayerColourFactory();
        this.player = new Player(this, colourFactory.getRandomColour(), connectionID);
        currentGame.getStateManager().getTopState().getEntities().add(player);
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

    public void setPlayer(Player player) {
        this.player = player;
    }

    //    public boolean isInGame() {
//        return inGame;
//    }
//
//    public void setInGame(boolean inGame) {
//        this.inGame = inGame;
//    }
}
