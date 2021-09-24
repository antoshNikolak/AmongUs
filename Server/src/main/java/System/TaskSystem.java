package System;

import Component.ImposterComp;
import Component.TaskComp;
import DistanceFinder.DistanceFinder;
import Entity.Player;
import Entity.Tile;
import Packet.Position.PosRequest;
import DistanceFinder.EntityDistance;
import StartUpServer.AppServer;
import State.StateManager;
import State.TaskState;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

public class TaskSystem extends BaseSystem {

    private List<TaskState> currentTaskStates = new ArrayList<>();

    @Override
    public void update() {
//        if (AppServer.currentGame == null) System.out.println("current game null");
//        if (AppServer.currentGame.getStateManager() == null) System.out.println("state manager null");
//        if (AppServer.currentGame.getStateManager().getCurrentState() == null) System.out.println("current state null");
//        if (AppServer.currentGame.getStateManager().getCurrentState().getWorld() == null) System.out.println("world null");
//        StateManager stateManager = AppServer.currentGame.getStateManager();
//            for (Tile tile : stateManager.getCurrentState().getWorld().getTiles()) {
//                if (tile.hasComponent(TaskComp.class)) {
//                    if (tile.getComponent(TaskComp.class).getTaskState().getClient() != null) {
//                        tile.getComponent(TaskComp.class).getTaskState().update();
//                    }
//                }
//            }

        for (Player player : AppServer.currentGame.getPlayers()) {
            if (player.getCurrentTask() != null) {
                player.getCurrentTask().update();
            }
        }
    }

    public void handleTaskAction(Player player, PosRequest posRequest) {
//        synchronized (AppServer.currentGame.getStateManager()) {
        if (posRequest.isTaskKey() && !player.hasComponent(ImposterComp.class) && player.getCurrentTask() == null) {
            Optional<Tile> closestTaskTile = getClosestTaskTile(player);
            closestTaskTile.ifPresent(tile -> createTaskPlayerRelation(player, tile));
        }
//        }
    }

    private void createTaskPlayerRelation(Player player, Tile tile) {
        TaskComp taskComp = tile.getComponent(TaskComp.class);
        taskComp.getTaskState().setPlayer(player);
        taskComp.getTaskState().init();
        player.setCurrentTask(taskComp.getTaskState());
    }


    private Optional<Tile> getClosestTaskTile(Player player) {
        PriorityQueue<EntityDistance<Tile, Player>> tiles = getPlayerTileDistanceList(player);
        if (tiles.peek() != null) {
            return Optional.of(tiles.peek().getEntity());
        } else {
            return Optional.empty();
        }
    }

    private PriorityQueue<EntityDistance<Tile, Player>> getPlayerTileDistanceList(Player player) {
        synchronized (AppServer.currentGame.getStateManager()) {
            PriorityQueue<EntityDistance<Tile, Player>> entityDistanceList = new PriorityQueue<>();
            for (Tile tile : AppServer.currentGame.getStateManager().getCurrentState().getWorld().getTiles()) {
                if (tile.hasComponent(TaskComp.class)) {
                    entityDistanceList.add(DistanceFinder.getDistanceBetweenEntities(tile, player));
                }
            }
            return entityDistanceList;
        }
    }


}
