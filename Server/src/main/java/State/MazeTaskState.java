package State;

import Client.Client;
import ConnectionServer.ConnectionServer;
import Maze.Maze;
import Packet.AddEntityReturn.AddLineReturn;
import Packet.AddEntityReturn.AddNestedPane;
import Position.Pos;
import System.*;
public class MazeTaskState extends TaskState{

    private final Maze maze = new Maze();


    public MazeTaskState() {
        super();
    }

    @Override
    public void init() {
        maze.start();
        sendClientMazeLines();
    }

    private void sendClientMazeLines(){
        ConnectionServer.sendTCP(new AddNestedPane(maze.createLineState(), new Pos( 50, 50), 300, 300), player.getConnectionID());
    }

    @Override
    protected void startSystems() {
        addSystem(new PhysicsSystem());


    }

    @Override
    public void removeClient(Client client) {

    }
}
