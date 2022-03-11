package Game;

import Client.Client;
import ConnectionServer.ConnectionServer;
import Registry.RegistryHandler;
import StartUpServer.AppServer;
import State.LobbyState;
import State.StateManager;

import java.util.ArrayList;
import java.util.List;

import Entity.*;
import Entity.Player;
import Utils.ConcurrentUtils;

public class Game {
    private final StateManager stateManager = new StateManager();
    private final EntityReturnBuffer entityReturnBuffer = new EntityReturnBuffer();
    private final List<Player> players = new ArrayList<>();
    //    private boolean running = true;
    private GameLoop gameLoop;
//    private EntityRegistryServer entityRegistryServer;

//    private final Object lock = new Object();

    public void startGame() {
        init();
        startGameLoop();
    }

    public void stopGame() {
//        synchronized (lock) {
        //this happens in a timer or clinet thread
//        System.out.println("entered stop game method");
        this.gameLoop.setRunning(false);//stops the game loop
        waitForThreadsToPause();//pause server thread and wait for game loop to terminate
        removeClientPlayers();
        AppServer.currentGame = null;
        ConnectionServer.getServerSemaphor().release();
    }

    public void waitForThreadsToPause() {
        Thread gameLoopThread = ConcurrentUtils.getThreadByName("game loop");
        try {
            if (gameLoopThread != null) {
                gameLoopThread.join();
            }
            ConnectionServer.getServerSemaphor().acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void removeClientPlayers() {
        for (Client client : AppServer.getClients()) {
            if (client.getPlayer() != null && players.contains(client.getPlayer())) {
                RegistryHandler.entityRegistryServer.removeEntity(client.getPlayer());
                client.setPlayer(null);
            }
        }
//        AppServer.getClients().stream().
//                map(Client::getPlayer).
//                filter(players::contains).
//                forEach(player -> player = null);

    }

    public void init() {
        stateManager.pushState(new LobbyState());
    }

    private void startGameLoop() {
        this.gameLoop = new GameLoop(30) {//game loop runs at 30 fps
            @Override
            public void handle() {
                stateManager.updateTop();//update state that is at the top of stack
                sendGameState();//sends state of the game to every relevant client
            }
        };
        gameLoop.start();
    }

    private void sendGameState() {
        this.entityReturnBuffer.sendGameState();
    }

    public synchronized StateManager getStateManager() {
        return stateManager;
    }

    public EntityReturnBuffer getEntityReturnBuffer() {
        return entityReturnBuffer;
    }

    public List<Player> getPlayers() {
        return players;
//        return AppServer.getClients().stream().
//                map(Client::getPlayer)
//                .collect(Collectors.toList());
    }

//    public EntityRegistryServer getEntityRegistryServer() {
//        return entityRegistryServer;
//    }
}
