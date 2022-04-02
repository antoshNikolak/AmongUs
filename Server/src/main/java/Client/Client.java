package Client;

import Entity.Player;
import Packet.UserData.UserData;
import PlayerColourManager.PlayerColourFactory;
import State.LobbyState;

import static StartUpServer.AppServer.currentGame;


public class Client {//client is user that has logged in.

    private final int connectionID;//client connection id
    private Player player;//player client is responsible from
    private final UserData userData;//client username and password

    public void createPlayer() {//initialize player when he joins game
        PlayerColourFactory colourFactory = currentGame.getStateManager().getState(LobbyState.class).getPlayerColourFactory();//generate random avaliable colour
        this.player = new Player(this, colourFactory.getRandomColour());
        currentGame.getStateManager().getTopState().getEntities().add(player);//add player to game
    }

    public Client(UserData userData, int connectionID) {
        this.userData = userData;
        this.connectionID = connectionID;
    }

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
}
