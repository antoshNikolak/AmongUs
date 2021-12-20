package State;

import ConnectionServer.ConnectionServer;
import EndGameHandler.EndGameHandler;
import Entity.EntityRegistryServer;
import Entity.Player;
import Entity.TaskBar;
import Packet.GameEnd.CrewWin;
import Packet.NestedPane.RemoveNestedScreen;
import Packet.Position.TaskBarUpdate;
import StartUpServer.AppServer;

import static EndGameHandler.EndGameHandler.handleCrewWin;

public abstract class TaskState extends State{
    protected Player player;

    @Override
    protected void close(){
        player.setCurrentTask(null);
        ConnectionServer.sendTCP(new RemoveNestedScreen(), player.getConnectionID());
    }


    public TaskState() {
        super();
    }

    public Player getClient() {
        return player;
    }

    public void setPlayer(Player client) {
        this.player = client;
    }

    protected void incrementTaskBar() {
        TaskBar taskBar = AppServer.currentGame.getStateManager().getState(GameState.class).getTaskBar();
        taskBar.incrementProgressBar(50);
        int registrationID = EntityRegistryServer.getEntityID(taskBar);
        ConnectionServer.sendTCPToAllPlayers(new TaskBarUpdate(registrationID, taskBar.getProgressBarWidth()));
        if (taskBar.isFull()){
            handleCrewWin();
        }
    }

}
