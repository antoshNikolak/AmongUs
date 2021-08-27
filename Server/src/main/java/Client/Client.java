package Client;

import Entity.GameClientHandler;
import Entity.Player;

public class Client {

    private final int connectionID;
    private Player player;

    public void createPlayer(){
        String texture = GameClientHandler.getRandomFreeTexture();

        this.player = new Player("green");
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
