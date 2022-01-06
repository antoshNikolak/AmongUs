package Game;

import Client.Client;
import ConnectionServer.ConnectionServer;
import StartUpServer.AppServer;
import State.GameState;
import State.LobbyState;
import State.StateManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import Entity.*;
import Entity.Player;
import Utils.ConcurrentUtils;

public class Game {
    private final StateManager stateManager = new StateManager();
    private final EntityReturnBuffer entityReturnBuffer = new EntityReturnBuffer();
    private final List<Player> players = new ArrayList<>();
    //    private boolean running = true;
    private GameLoop gameLoop;

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
                client.setPlayer(null);
            }
        }
//        AppServer.getClients().stream().
//                map(Client::getPlayer).
//                filter(players::contains).
//                forEach(player -> player = null);

    }

    public void init() {
//        stateManager.setCurrentStateWithClose(new LobbyState());
        stateManager.pushState(new LobbyState());
        players.forEach(player -> System.out.println("play name: " + player.getNameTag()));
    }

    private void startGameLoop() {
        this.gameLoop = new GameLoop(30) {
            @Override
            public void handle() {
                stateManager.updateTop();
                sendGameState();
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
}
