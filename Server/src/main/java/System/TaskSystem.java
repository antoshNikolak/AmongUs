package System;

import Component.ImposterComp;
import Component.TaskComp;
import DistanceFinder.DistanceFinder;
import Entity.Player;
import Entity.Tile;
import Packet.Position.PosRequest;
import StartUpServer.AppServer;
import State.TaskState;

import java.util.Optional;

public class TaskSystem extends BaseSystem {

    @Override
    public void update() {
        for (Player player : AppServer.currentGame.getPlayers()) {
            if (player.getCurrentTask() != null) {
                player.getCurrentTask().update();
            }
        }
    }

    public void handleTaskAction(Player player, PosRequest posRequest) {
        if (posRequest.isTaskKey() && !player.hasComponent(ImposterComp.class) && player.getCurrentTask() == null) {
            Optional<Tile> closestTaskTile = DistanceFinder.getClosestEntity(player, AppServer.currentGame.getStateManager().getCurrentState().getWorld().getTilesWithTask(), 70);
            closestTaskTile.ifPresent(tile -> createTaskPlayerRelation(player, tile));
        }
    }

    private void createTaskPlayerRelation(Player player, Tile tile) {
        TaskComp taskComp = tile.getComponent(TaskComp.class);
        TaskState taskState = taskComp.createTaskState(player);
        player.setCurrentTask(taskState);
    }


//    private Optional<Tile> getClosestTaskTile(Player player) {
//        System.out.println("pre getplayer tile distance list");
//        PriorityQueue<EntityDistance<Tile, Player>> tiles = getPlayerTileDistanceList(player);
//        System.out.println("post get player tile distance list");
//
//        if (tiles.peek() != null) {
//            return Optional.of(tiles.peek().getEntity());
//        } else {
//            return Optional.empty();
//        }
//    }

//    private PriorityQueue<EntityDistance<Tile, Player>> getPlayerTileDistanceList(Player player) {
//            PriorityQueue<EntityDistance<Tile, Player>> entityDistanceList = new PriorityQueue<>();
//            for (Tile tile : AppServer.currentGame.getStateManager().getCurrentState().getWorld().getTiles()) {
//                if (tile.hasComponent(TaskComp.class)) {
//                    System.out.println("pre get distance between entites");
//                    entityDistanceList.add(DistanceFinder.getDistanceBetweenEntities(tile, player));
//                    System.out.println("post get distance between entites");
//
//                }
//            }
//            return entityDistanceList;
//    }


}
