package Client;

import Entity.Player;
import Packet.UserData.UserData;
import PlayerColourManager.PlayerColourFactory;
import State.LobbyState;

import static StartUpServer.AppServer.currentGame;

public class Client {

    private final int connectionID;
    private Player player;
    private final UserData userData;

    public void createPlayer() {
        PlayerColourFactory colourFactory = currentGame.getStateManager().getState(LobbyState.class).getPlayerColourFactory();
        this.player = new Player(this, colourFactory.getRandomColour());
        currentGame.getStateManager().getTopState().getEntities().add(player);
    }

    public Client(UserData userData, int connectionID) {
        this.userData = userData;
        this.connectionID = connectionID;
//        this.connectionID = connectionID;
    }

//    public int getConnectionID() {
//        return connectionID;
//    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public UserData getUserData() {
        return userData;
    }

    public int getConnectionID() {
        return connectionID;
    }

    //    public boolean isInGame() {
//        return inGame;
//    }
//
//    public void setInGame(boolean inGame) {
//        this.inGame = inGame;
//    }
}
