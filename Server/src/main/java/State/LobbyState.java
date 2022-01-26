package State;

import Client.Client;
import ConnectionServer.ConnectionServer;
import Entity.Player;
import Game.Game;
import Packet.AddEntityReturn.AddEntityReturn;
import Packet.AddEntityReturn.AddLocalEntityReturn;
//import Packet.AddEntityReturn.AddChangingEntityReturn;
import Packet.EntityState.NewAnimatedEntityState;
import Packet.CountDown.CountDown;
import PlayerColourManager.PlayerColourFactory;
import TimerHandler.CounterStarter;
import World.World;
import System.*;

import static StartUpServer.AppServer.currentGame;

public class LobbyState extends PlayingState {

    public static final int PLAYER_LIMIT = 2;
    private final PlayerColourFactory playerColourFactory = new PlayerColourFactory();

    public LobbyState() {
        super();
    }

    public void handleNewPlayerJoin(Client client) {
        client.createPlayer();
        sendPlayerData(client);
        sendWorldData(client.getConnectionID());
        if (currentGame.getPlayers().size() == PLAYER_LIMIT) {
            CounterStarter.startCountDown( 5, 200, 200, 50,  () -> startGameState());
        }
    }

    public static void startGameState() {
        synchronized (currentGame.getStateManager()) {
            currentGame.getStateManager().popState();
            currentGame.getStateManager().pushState(new GameState());
        }
    }

    public static void prepareGame() {
        if (currentGame == null) {
            currentGame = new Game();
            currentGame.startGame();
        }
    }


//    private static void prepareGame() {
//        if (currentGame == null) {
//            currentGame = new Game();
//            currentGame.startGame();
//        }
//    }

    @Override
    public void init() {
        startSystems();
        createWorld();
    }

    @Override
    protected void startSystems() {
        this.addSystem(new PhysicsSystem(entities));
        this.addSystem(new TextureSystem());
        this.addSystem(new ImposterActionsSystem());
    }

    @Override
    protected void createWorld() {
        this.world = new World("World/lobby.txt");
    }

    private void sendPlayerData(Client client) {
        sendExistingPlayersToClient(client.getPlayer().getConnectionID());
        sendNewPlayerToAll(client.getPlayer());
    }

    private void sendExistingPlayersToClient(int connectionID) {
        for (Player player : currentGame.getPlayers()) {
            if (player.getConnectionID() != connectionID) {
                ConnectionServer.sendTCP(new AddEntityReturn(player.adaptToNewAnimatedEntityState(true)), connectionID);
            }
        }
    }

    private void sendNewPlayerToAll(Player player) {
        NewAnimatedEntityState playerState = player.adaptToNewAnimatedEntityState(true);
        ConnectionServer.sendTCP(new AddLocalEntityReturn(playerState), player.getConnectionID());
        for (Player player2 : currentGame.getPlayers())
            if (player2.getConnectionID() != player.getConnectionID()) {
                ConnectionServer.sendTCP(new AddEntityReturn(playerState), player2.getConnectionID());
            }
    }


//    @Override
//    public void removeClient(Client client) {
//
//    }

    public PlayerColourFactory getPlayerColourFactory() {
        return playerColourFactory;
    }

    //todo slower players can travel faster

}
