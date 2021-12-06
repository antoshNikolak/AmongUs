package Entity;

public class GameClientHandler {

//    public static void prepareClientToPlay(Client client) {
//        prepareGame();
//        client.createPlayer();
//        addClientToLobby(client);
//        if (AppServer.currentGame.getClients().size() >= 2) {
//            TimerStarter.startTimer("GameStartTimer", 5, () -> startGameState());
//        }
//    }



//    public static void startGameState() {
//        AppServer.currentGame.getStateManager().popState();
//        AppServer.currentGame.getStateManager().pushState(new GameState());
//    }
//
//    private static void addClientToLobby(Client client) {
//        if (!(AppServer.currentGame.getStateManager().getCurrentState() instanceof LobbyState))
//            throw new IllegalStateException();
//        ((LobbyState) AppServer.currentGame.getStateManager().getCurrentState()).handleNewPlayerJoin(client);
//    }
//
//    private static void prepareGame() {
//        if (AppServer.currentGame == null) {
//            AppServer.currentGame = new Game();
//            AppServer.currentGame.startGame();
//        }
//    }

}
