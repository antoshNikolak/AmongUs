package State;

import Client.Client;
import ConnectionServer.ConnectionServer;
import Entity.Player;
import Packet.AddEntityReturn.AddNestedPane;

public abstract class TaskState extends State{

    protected Player player;


    public TaskState() {
        super(false);
    }

    public Player getClient() {
        return player;
    }

    public void setPlayer(Player client) {
        this.player = client;
    }//solution allows for only 1 client to play a task at a time
}
