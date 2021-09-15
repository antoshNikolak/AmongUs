package System;

import Component.ImposterComp;
import Component.TaskComp;
import DistanceFinder.DistanceFinder;
import Entity.Player;
import Entity.Tile;
import Packet.Position.PosRequest;
import DistanceFinder.EntityDistance;
import StartUpServer.AppServer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

public class TaskSystem extends BaseSystem {

    @Override
    public void update() {
        for (Player player : AppServer.currentGame.getPlayers()) {
            if (player.getCurrentTask() != null){
                player.getCurrentTask().update();
//                System.out.println("updating task: "+ player.getCurrentTask());
            }
        }
    }

    public static void handleTaskAction(Player player, PosRequest posRequest) {
        if (posRequest.isTaskKey() && !player.hasComponent(ImposterComp.class) && player.getCurrentTask() == null) {
            Optional<Tile> closestTaskTile = getClosestTaskTile(player);
            closestTaskTile.ifPresent(tile ->{
                player.setCurrentTask(tile.getComponent(TaskComp.class).getTaskState());
                tile.getComponent(TaskComp.class).getTaskState().setPlayer(player);//todo WARNING BI DIRECTIONAL RELATIONSHIP
                tile.getComponent(TaskComp.class).getTaskState().init();

            });
        }

    }


    public static Optional<Tile> getClosestTaskTile(Player player) {
        PriorityQueue<EntityDistance<Tile, Player>> tiles = getPlayerTileDistanceList(player);
        if (tiles.peek() != null) {
            return Optional.of(tiles.peek().getEntity());
        }else {
            return Optional.empty();
        }
    }

    private static PriorityQueue<EntityDistance<Tile, Player>> getPlayerTileDistanceList(Player player) {
        PriorityQueue<EntityDistance<Tile, Player>> entityDistanceList = new PriorityQueue<>();
        for (Tile tile : AppServer.currentGame.getStateManager().getCurrentState().getWorld().getTiles()) {
            if (tile.hasComponent(TaskComp.class)) {
                entityDistanceList.add(DistanceFinder.getDistanceBetweenEntities(tile, player));
            }
        }
        return entityDistanceList;


    }


}
