package System;

import Component.AliveComp;
import Component.ImposterComp;
import Component.TaskComp;
import DistanceFinder.DistanceFinder;
import Entity.Player;
import Entity.Tile;
import Packet.Position.PosRequest;
import StartUpServer.AppServer;
import State.TaskState;

import java.util.Optional;

import static StartUpServer.AppServer.currentGame;

public class TaskSystem extends BaseSystem {

    @Override
    public void update() {
        for (Player player : currentGame.getPlayers()) {
            if (player.getCurrentTask() != null) {
                player.getCurrentTask().update();
            }
        }
    }

    @Override
    public void handleAction(Player player, PosRequest posRequest) {
        if (isPlayerEligibleToPlayerTask(player, posRequest)) {
            Optional<Tile> closestTaskTile = DistanceFinder.getClosestEntity(player, currentGame.getStateManager().getCurrentState().getWorld().getTilesWithTask(), 70);
            closestTaskTile.ifPresent(tile -> createTaskPlayerRelation(player, tile));
        }
    }

//    public void handleTaskAction(Player player, PosRequest posRequest) {
//        if (isPlayerEligibleToPlayerTask(player, posRequest)) {
//            Optional<Tile> closestTaskTile = DistanceFinder.getClosestEntity(player, AppServer.currentGame.getStateManager().getCurrentState().getWorld().getTilesWithTask(), 70);
//            closestTaskTile.ifPresent(tile -> createTaskPlayerRelation(player, tile));
//        }
//    }

    private boolean isPlayerEligibleToPlayerTask(Player player, PosRequest posRequest) {
        return posRequest.isTaskKey() &&
                !player.hasComponent(ImposterComp.class) &&
                player.getCurrentTask() == null &&
                player.getComponent(AliveComp.class).isAlive();
    }

    private void createTaskPlayerRelation(Player player, Tile tile) {
        TaskComp taskComp = tile.getComponent(TaskComp.class);
        TaskState taskState = taskComp.createTaskState(player);
        player.setCurrentTask(taskState);
    }

}
