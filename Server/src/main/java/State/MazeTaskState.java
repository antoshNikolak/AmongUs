package State;

import Client.Client;
import Component.PosComp;
import ConnectionServer.ConnectionServer;
import Entity.CollidableRect;
import Entity.EntityReturnBuffer;
import Entity.MazePlayer;
import Maze.Maze;
import Packet.AddEntityReturn.AddNestedPane;
import Packet.EntityState.NewEntityState;
import Packet.Position.RemoveNestedScreen;
import Position.Pos;
import System.*;

import java.util.ArrayList;
import java.util.List;

public class MazeTaskState extends TaskState {

    private final int mazeHeight ;
    private final int mazeWidth;
    private final int cellDimension;
    private  Maze maze;
    private MazePlayer mazePlayer;


    public MazeTaskState() {
        super();
        this.mazeHeight = 5;
        this.mazeWidth = 8;
        this.cellDimension = 50;
//        this.maze = new Maze(mazeWidth, mazeHeight, cellDimension);
    }

    @Override
    public void update(){//when this happens, in another thread player isnt initialized
        super.update();
        checkTaskComplete();
    }



    private void checkTaskComplete() {
        if (isMazeComplete()){
            player.setCurrentTask(null);
            this.entities.clear();
            ConnectionServer.sendTCP(new RemoveNestedScreen(), player.getConnectionID());
        }
    }

    public boolean isMazeComplete(){
        return mazePlayer.getComponent(PosComp.class).getPos().getX() >= mazeWidth * cellDimension;
    }

    @Override
    public void init() {
        this.maze = new Maze(mazeWidth, mazeHeight, cellDimension);
        maze.start();
        createMazePlayer();
        sendClientMazeLines();
        startSystems();
    }

    private void createMazePlayer() {
        this.mazePlayer = new MazePlayer(this, player.getConnectionID());
        PosComp posComp = mazePlayer.getComponent(PosComp.class);
        posComp.setPos(new Pos(maze.getStartPos()));
        entities.add(mazePlayer);
    }

    private void sendClientMazeLines() {
        startUpEntities();
        List<NewEntityState> newEntityStates = getNewEntityStateList(getCollidableMazeLines());
        ConnectionServer.sendTCP(new AddNestedPane(newEntityStates, 50, 50, 400, 250), player.getConnectionID());
    }

    private List<CollidableRect> getCollidableMazeLines() {
        ArrayList<CollidableRect> collidableRects = new ArrayList<>(maze.createLineState());
        this.entities.addAll(collidableRects);
        return collidableRects;
    }

    private List<NewEntityState> getNewEntityStateList(List<CollidableRect> collidableRects) {
        ArrayList<NewEntityState> newEntityStates = new ArrayList<>(EntityReturnBuffer.adaptCollectionToNewLineStates(collidableRects));
        newEntityStates.add(mazePlayer.adaptToNewAnimatedEntityState());
        return newEntityStates;
    }

    private void startUpEntities() {
        entities.addAll(maze.createLineState());
    }

    @Override
    protected void startSystems() {
        addSystem(new PhysicsSystem(entities));
    }

    @Override
    public void removeClient(Client client) {

    }

    public MazePlayer getMazePlayer() {
        return mazePlayer;
    }
}
