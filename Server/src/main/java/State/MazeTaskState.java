package State;

import Client.Client;
import ConnectionServer.ConnectionServer;
import Entity.MazePlayer;
import Entity.Player;
import Maze.Maze;
import Packet.AddEntityReturn.AddChangingEntityReturn;
import Packet.AddEntityReturn.AddLineReturn;
import Packet.AddEntityReturn.AddNestedPane;
import Packet.EntityState.NewAnimatedEntityState;
import Packet.EntityState.NewEntityState;
import Position.Pos;
import System.*;

import java.util.ArrayList;
import java.util.List;

public class MazeTaskState extends TaskState{

    private final Maze maze = new Maze();
    private MazePlayer mazePlayer;


    public MazeTaskState() {
        super();
    }

    @Override
    public void init() {
        this.mazePlayer = new MazePlayer(player.getConnectionID());
        maze.start();
        sendClientMazeLines();
    }

    private void sendClientMazeLines(){
        List<NewEntityState> newEntityStates = new ArrayList<>(maze.createLineState());
        newEntityStates.add(mazePlayer.adaptToNewAnimatedEntityState());
        ConnectionServer.sendTCP(new AddNestedPane(newEntityStates, 50, 50, 300, 300), player.getConnectionID());
    }

    @Override
    protected void startSystems() {
        addSystem(new PhysicsSystem());


    }

    @Override
    public void removeClient(Client client) {

    }
}
