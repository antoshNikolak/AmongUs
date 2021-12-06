package State;

import ConnectionServer.ConnectionServer;
import Entity.EntityRegistryServer;
import Entity.Player;
import Entity.TaskBar;
import Packet.GameEnd.CrewWin;
import Packet.NestedPane.RemoveNestedScreen;
import Packet.Position.TaskBarUpdate;
import StartUpServer.AppServer;

public abstract class TaskState extends State{

    protected Player player;

    protected void endState(){
        player.setCurrentTask(null);
//        this.entities.clear();
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
    }//solution allows for only 1 client to play a task at a time

    protected void incrementTaskBar() {
        TaskBar taskBar = AppServer.currentGame.getStateManager().getState(GameState.class).getTaskBar();
        taskBar.incrementProgressBar(50);
        int registrationID = EntityRegistryServer.getEntityID(taskBar);
        ConnectionServer.sendTCPToAllPlayers(new TaskBarUpdate(registrationID, taskBar.getProgressBarWidth()));
        if (taskBar.isFull()){
            handleCrewWin();
        }
    }

    private void handleCrewWin(){
        ConnectionServer.sendTCPToAllPlayers(new CrewWin());
        AppServer.currentGame.stopGame();
    }
}
