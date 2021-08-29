package Client;

import Entity.GameClientHandler;
import Entity.Player;
import PlayerColourManager.PlayerColourFactory;

public class Client {

    private final int connectionID;
    private Player player;

    public void createPlayer(){
        this.player = new Player(PlayerColourFactory.getRandomColour());
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
