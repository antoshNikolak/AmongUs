package State;

import Client.Client;
import ClientScreenTracker.ScreenData;
import Component.PosComp;
import ConnectionServer.ConnectionServer;
import Entity.CollidableRect;
import Entity.MazePlayer;
import Maze.Maze;
import Packet.NestedPane.AddNestedPane;
import Packet.NestedPane.NodeInfo;
import Packet.NestedPane.NodeType;
import Position.Pos;
import System.*;

import java.util.ArrayList;
import java.util.List;

public class MazeTaskState extends TaskState {

    private final int mazeHeight;
    private final int mazeWidth;
    private final int cellDimension;
    private Maze maze;
    private MazePlayer mazePlayer;

    private final List<CollidableRect> mazeLines = new ArrayList<>();


    public MazeTaskState() {
        super();
        this.mazeHeight = 7;
        this.mazeWidth = 12;
        this.cellDimension = 50;
    }

    @Override
    public void update() {
        super.update();
        checkTaskComplete();
    }

    private void checkTaskComplete() {
        if (isMazeComplete()) {
            super.endState();
            super.incrementTaskBar();
        }
    }

    public boolean isMazeComplete() {
        return mazePlayer.getComponent(PosComp.class).getPos().getX() >= mazeWidth * cellDimension;
    }

    @Override
    public void init() {
        startMaze();
        createMazePlayer();
        sendClientMazeLines();
        startSystems();
    }

    private void startMaze() {
        this.maze = new Maze(mazeWidth, mazeHeight, cellDimension);
        this.maze.start();
    }

    private void createMazePlayer() {
        this.mazePlayer = new MazePlayer(this, player.getConnectionID());
        PosComp posComp = mazePlayer.getComponent(PosComp.class);
        posComp.setPos(new Pos(maze.getStartPos()));
        entities.add(mazePlayer);
    }

    private void sendClientMazeLines() {
        startUpEntities();
        ConnectionServer.sendTCP(createNestedPanePacket(), player.getConnectionID());
    }

    private AddNestedPane createNestedPanePacket() {
        int mazePixelWidth = mazeWidth* cellDimension;
        int mazePixelHeight = mazeHeight* cellDimension;
        int x = (ScreenData.WIDTH / 2) - (mazePixelWidth / 2);
        int y = (ScreenData.HEIGHT / 2) - (mazePixelHeight / 2);
        return new AddNestedPane.Builder(x, y, mazePixelWidth, mazePixelHeight)
                .withNode(new NodeInfo(NodeType.CANVAS, 0, 0, mazePixelWidth, mazePixelHeight))
                .withNodes(getLines())
                .withNewEntityState(mazePlayer.adaptToNewAnimatedEntityState(true))
                .build();
    }

//    private List<CollidableRect> getCollidableMazeLines() {
//        ArrayList<CollidableRect> collidableRects = new ArrayList<>(maze.createLines());
//        this.entities.addAll(collidableRects);
//        return collidableRects;
//    }

    private List<NodeInfo> getLines() {
        List<NodeInfo> nodes = new ArrayList<>();
        for (CollidableRect collidableRect : mazeLines) {
            PosComp posComp = collidableRect.getComponent(PosComp.class);
            NodeInfo nodeInfo = new NodeInfo(NodeType.LINE, posComp.getPos().getX(), posComp.getPos().getY(), posComp.getWidth(), posComp.getHeight());
            nodeInfo.setLineWidth(5);
            nodes.add(nodeInfo);
        }
        return nodes;
    }

//    private List<NewAnimatedEntityState> getNewEntityStateList(List<CollidableRect> collidableRects) {
//        ArrayList<NewAnimatedEntityState> newEntityStates = new ArrayList<NewAnimatedEntityState>(EntityReturnBuffer.adaptCollectionToNewLineStates(collidableRects));
//        newEntityStates.add(mazePlayer.adaptToNewAnimatedEntityState());
//        return newEntityStates;
//    }

    private void startUpEntities() {
        mazeLines.addAll(maze.createLines());
        entities.addAll(mazeLines);
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
