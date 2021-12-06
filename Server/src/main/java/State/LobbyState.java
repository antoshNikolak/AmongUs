package State;

import Client.Client;
import ConnectionServer.ConnectionServer;
import Entity.Player;
import Game.Game;
import Packet.AddEntityReturn.AddEntityReturn;
import Packet.AddEntityReturn.AddLocalEntityReturn;
//import Packet.AddEntityReturn.AddChangingEntityReturn;
import Packet.EntityState.NewAnimatedEntityState;
import PlayerColourManager.PlayerColourFactory;
import StartUpServer.AppServer;
import TimerHandler.TimerStarter;
import World.World;
import System.*;

import static StartUpServer.AppServer.currentGame;

public class LobbyState extends PlayingState {

    public static final int PLAYER_LIMIT = 7;
    private final PlayerColourFactory playerColourFactory = new PlayerColourFactory();

    public LobbyState() {
        super();
    }

    public void handleNewPlayerJoin(Client client) {
        client.createPlayer();
        sendPlayerData(client);
        sendWorldData(client.getConnectionID());
        if (currentGame.getPlayers().size() == PLAYER_LIMIT) {
            TimerStarter.startTimer("GameStartTimer", 5, () -> startGameState());
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
        sendExistingPlayersToClient(client.getConnectionID());
        sendNewPlayerToAll(client);
    }

    private void sendExistingPlayersToClient(int connectionID) {
        for (Player player : currentGame.getPlayers()) {
            if (player.getConnectionID() != connectionID) {
                ConnectionServer.sendTCP(new AddEntityReturn(player.adaptToNewAnimatedEntityState(true)), connectionID);
            }
        }
    }


    private void sendNewPlayerToAll(Client client) {
        NewAnimatedEntityState playerState = client.getPlayer().adaptToNewAnimatedEntityState(true);
        ConnectionServer.sendTCP(new AddLocalEntityReturn(playerState), client.getConnectionID());
        for (Player player : currentGame.getPlayers())
            if (player.getConnectionID() != client.getConnectionID()) {
                ConnectionServer.sendTCP(new AddEntityReturn(playerState), player.getConnectionID());
            }
    }


    @Override
    public void removeClient(Client client) {

    }

    public PlayerColourFactory getPlayerColourFactory() {
        return playerColourFactory;
    }

}
