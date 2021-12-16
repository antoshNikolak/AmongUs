package Maze;

import Entity.CollidableRect;
import Position.Pos;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class Maze {

    private final int width;
    private final int height;
    private final int cellDimension;


    private final Cell[][] cells;
    private SquareGraph graph;

    private Pos startPos, endPos;


    public Maze(int width, int height, int cellDimension) {
        this.width = width;
        this.height = height;
        this.cellDimension = cellDimension;
        this.cells = initializeCells();
    }

    public void start() {
        graph = new SquareGraph(width, height, cellDimension);
        graph.init();
        Set<Edge> minimalSpanningTree = graph.createSpanningTree();
        removeWallsBetweenCells(graph, minimalSpanningTree);
        createStartFinish();
    }

    public List<CollidableRect> createLines() {
        return graph.createLinesStates(cells);
    }

    private void createStartFinish() {
        createStartCell();
        createEndCell();
    }

    private void createStartCell(){
        Random random = new Random();
        int startCellHeight = random.nextInt(height);
//        Cell startCell = cells [startCellHeight][0];
//        startCell.setLeftWall(false);
        this.startPos = new Pos((double) cellDimension/2, startCellHeight*cellDimension + (double)cellDimension/2);
    }

    private void createEndCell(){
        Random random = new Random();
        int endCellHeight = random.nextInt(height);
        Cell endCell = cells [endCellHeight][width-1];
        endCell.setRightWall(false);
        this.endPos = new Pos((width-1)* cellDimension, endCellHeight * cellDimension);
    }


//    private void creatVerticalOpening(Random random) {
//        int startY = 0;
//        int startX = random.nextInt(width - 1);
//        int endY = width - 1;
//        int endX = random.nextInt(width - 1);
//        Cell startCell = cells[startY][startX];
//        Cell endCell = cells[endY][endX];
//        startCell.setTopWall(false);
//        endCell.setBottomWall(false);
//    }
//
//    private void createHorizontalOpenings(Random random) {
//        int startX = 0;
//        int startY = random.nextInt(width - 1);
//        int endX = width - 1;
//        int endY = random.nextInt(width - 1);
//        Cell startCell = cells[startY][startX];
//        Cell endCell = cells[endY][endX];
//        startCell.setLeftWall(false);
//        endCell.setRightWall(false);
//    }

    private void removeWallsBetweenCells(SquareGraph graph, Set<Edge> MSM) {
        Vertex[][] vertices = graph.create2DArray();
        for (Edge edge : MSM) {
            Pos arrayPos1 = getPosInArray(vertices, edge.getVertex1());
            Pos arrayPos2 = getPosInArray(vertices, edge.getVertex2());
            assert arrayPos1 != null && arrayPos2 != null;

            Cell cell1 = cells[(int) arrayPos1.getY()][(int) arrayPos1.getX()];
            Cell cell2 = cells[(int) arrayPos2.getY()][(int) arrayPos2.getX()];

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
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (vertices[y][x] == vertex) {
                    return new Pos(x, y);
                }
            }
        }
        return null;
    }

    private Cell[][] initializeCells() {
        Cell[][] cells = new Cell[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                cells[i][j] = new Cell();
            }
        }
        return cells;
    }

//    public Cell getStartCell() {
//        return startCell;
//    }
//
//    public Cell getEndCell() {
//        return endCell;
//    }


    public Pos getStartPos() {
        return startPos;
    }

    public Pos getEndPos() {
        return endPos;
    }
}
