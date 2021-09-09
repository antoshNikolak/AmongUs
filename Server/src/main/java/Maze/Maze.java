package Maze;

import Packet.EntityState.NewLineState;
import Position.Pos;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class Maze {

    private final int width = 10;
    private final Cell[][] cells;
    private SquareGraph graph;

    public Maze() {
        this.cells = initializeCells();
    }

    public void start() {
        graph = new SquareGraph(width);
        graph.init();
        Set<Edge> minimalSpanningTree = graph.createSpanningTree();
        createConnectionsBetweenCells(graph, minimalSpanningTree);
        createStartFinish();

//        Canvas canvas = new Canvas(Main.pane.getWidth(), Main.pane.getHeight());
//        Main.pane.getChildren().add(canvas);
//        graph.createLinesStates(cells);
    }

    public List<NewLineState> createLineState(){
        return graph.createLinesStates(cells);
    }

    private void createStartFinish() {
        Random random = new Random();
        int side = random.nextInt(2);
        if (side == 0) {
            creatVerticalOpening(random); //start y is 0 or width
        } else {
            createHorizontalOpenings(random);
        }
    }

    private void creatVerticalOpening(Random random) {
        int startY = 0;
        int startX = random.nextInt(width - 1);
        int endY = width - 1;
        int endX = random.nextInt(width - 1);
        Cell startCell = cells[startY][startX];
        Cell endCell = cells[endY][endX];
        startCell.setTopWall(false);
        endCell.setBottomWall(false);
    }

    private void createHorizontalOpenings(Random random) {
        int startX = 0;
        int startY = random.nextInt(width - 1);
        int endX = width - 1;
        int endY = random.nextInt(width - 1);
        Cell startCell = cells[startY][startX];
        Cell endCell = cells[endY][endX];
        startCell.setLeftWall(false);
        endCell.setRightWall(false);
    }

    private void createConnectionsBetweenCells(SquareGraph graph, Set<Edge> MSM) {
        //for each edge look at its connection and the 2 vertex (use the position of the vertex in the array)
        //get the cells corresponding to those 2 vertex
        //remove the wall between them
        //OR
        //asign a vertex to each cell

        Vertex[][] vertices = graph.create2DArray();


        for (Edge edge : MSM) {
            Pos arrayPos1 = getPosInArray(vertices, edge.getVertex1());
            Pos arrayPos2 = getPosInArray(vertices, edge.getVertex2());//or we could base this on their s co ordinate
            assert arrayPos1 != null && arrayPos2 != null;

            Cell cell1 = cells[(int)arrayPos1.getY()][(int)arrayPos1.getX()];
            Cell cell2 = cells[(int)arrayPos2.getY()][(int)arrayPos2.getX()];

            if (arrayPos1.getX() < arrayPos2.getX()) {
                cell1.setRightWall(false);
                cell2.setLeftWall(false);
            } else if (arrayPos1.getX() > arrayPos2.getX()) {
                cell1.setLeftWall(false);
                cell2.setRightWall(false);
            }

            if (arrayPos1.getY() < arrayPos2.getY()) {
                cell1.setBottomWall(false);
                cell2.setTopWall(false);
            } else if (arrayPos1.getY() > arrayPos2.getY()) {
                cell1.setTopWall(false);
                cell2.setBottomWall(false);
            }
        }

    }

    private <T> Pos getPosInArray(T[][] vertices, T vertex) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < width; y++) {
                if (vertices[y][x] == vertex) {
                    return new Pos(x, y);
                }
            }
        }
        return null;
    }

    private Cell[][] initializeCells() {
        System.out.println("initializing cell");
        Cell[][] cells = new Cell[width][width];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < width; j++) {
                System.out.println("here");
                cells[j][i] = new Cell();
            }
        }
        return cells;
    }


}
