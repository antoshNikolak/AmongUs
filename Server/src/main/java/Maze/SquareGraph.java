package Maze;

import Entity.EntityRegistryServer;
import Packet.EntityState.NewLineState;
import Position.Pos;
import javafx.scene.canvas.GraphicsContext;

import java.util.*;
import java.util.stream.Collectors;

public class SquareGraph {

    private final int width;

    public SquareGraph(int width) {
        this.width = width;
    }

    private final List<Edge> edges = new ArrayList<>();
    private final List<Vertex> vertices = new ArrayList<>();


    public Set<Edge> createSpanningTree() {
        Set<Edge> minimalSpanningTree = new HashSet<>();
        PriorityQueue<Edge> edgesConnected = new PriorityQueue<>();


        while (true) {

            if (minimalSpanningTree.isEmpty()) {
                Vertex source = getRandomVertex();
                edgesConnected = getConnectedEdges(source);
            } else {
                edgesConnected = getConnectedEdges(minimalSpanningTree);//cant be part
            }

            if (edgesConnected.isEmpty()) {
                break;
            }

            Edge shortestEdge = edgesConnected.peek();
            minimalSpanningTree.add(shortestEdge);
        }

        return minimalSpanningTree;

    }

    private PriorityQueue<Edge> getConnectedEdges(Set<Edge> MSM) {
        PriorityQueue <Edge> priorityQueue = getEdgePriorityQueue();
        for (Edge edge: edges){
            if (isEdgeValidForMSM(MSM, edge)){
               priorityQueue.add(edge);
            }
        }
        return priorityQueue;
    }

    private PriorityQueue<Edge> getConnectedEdges(Vertex vertex) {
        return edges.stream().filter(edge -> edge.hasConnectionToVertex(vertex))
                .collect(Collectors.toCollection(this::getEdgePriorityQueue));
    }


    private boolean isEdgeValidForMSM(Set<Edge> MSM, Edge currentEdge) {
        return !(MSM.contains(currentEdge))
                && checkEdgeWontLoop(MSM, currentEdge);
    }

    private boolean checkEdgeWontLoop(Set<Edge> MSM, Edge currentEdge) {
        boolean commonVertexOne = false;
        boolean commonVertexTwo = false;
        for (Edge edge : MSM) {
            if (edge.getVertex1() == currentEdge.getVertex1()
                    || edge.getVertex2() == currentEdge.getVertex1()) {
                commonVertexOne = true;
            }
            if (edge.getVertex1() == currentEdge.getVertex2()
                    || edge.getVertex2() == currentEdge.getVertex2()){
                commonVertexTwo = true;
            }
        }
         return oneOfEquals(commonVertexOne, commonVertexTwo, true);


//        return !(commonVertexOne && commonVertexTwo);//todo no need to check if edge connected
        //todo if one vertex is common, edge connected
        //todo if 2 common, edge loops
        //OR get a set of vertices from edge

//        Set<Vertex> uniqueVertices = getUniqueVerticesFromEdges(MSM);
//        return uniqueVertices.contains(currentEdge.getVertex1())
//                && uniqueVertices.contains(currentEdge.getVertex2());

    }

    private boolean oneOfEquals(boolean a, boolean b, boolean expected){
        return a == expected && b != expected
                || a != expected && b == expected;
    }

//    private Set<Vertex> getUniqueVerticesFromEdges(Collection<Edge> edges) {
//        Set<Vertex> vertices = new HashSet<>();
//        for (Edge edge : edges) {
//            vertices.add(edge.getVertex1());
//            vertices.add(edge.getVertex2());
//        }
//        System.out.println(vertices.size());
//        return vertices;
//    }
//
//    private boolean isEdgeConnectedToMSM(Set<Edge> MSM, Edge currentEdge) {
//        for (Edge edge : MSM) {
//            Vertex vertex1 = edge.getVertex1();
//            Vertex vertex2 = edge.getVertex2();//todo forgot to use current edge
//            if (edge.hasConnectionToVertex(vertex1) || edge.hasConnectionToVertex(vertex2)) {
//                return true;
//            }
//        }
//        return false;
//    }


    private void addVertexToGraph(Vertex vertex1, int currentVertex) {
        Vertex vertex2 = new Vertex();
        addVertex(vertex2);

        if (!(currentVertex % width == 1)) {
            connectVertices(vertex1, vertex2);
        }
        if (currentVertex >width) {
            connectToUpperVertex(vertex2, width);
        }
        if (currentVertex == width * width) {
            return;
        }
        addVertexToGraph(vertex2, currentVertex + 1);
    }

    public void init() {
        Vertex vertex = new Vertex();
        addVertex(vertex);
        addVertexToGraph(vertex, 2);
    }

    private Vertex getRandomVertex() {
        return vertices.get(new Random().nextInt(vertices.size()));
    }

    public void addVertex(Vertex vertex) {
        this.vertices.add(vertex);
    }


    public void connectVertices(Vertex v1, Vertex v2) {
        int rand = new Random().nextInt(100) + 1;
        edges.add(new Edge(v1, v2, rand));
    }

    public void connectToUpperVertex(Vertex vertex, int mazeWidth) {
        int index = vertices.indexOf(vertex) - mazeWidth;
        connectVertices(vertex, vertices.toArray(new Vertex[0])[index]);
    }

    private PriorityQueue<Edge> getEdgePriorityQueue(){
        return new PriorityQueue<>(new Comparator<Edge>() {
            @Override
            public int compare(Edge edge1, Edge edge2) {
                return Integer.compare(edge1.getDistance(), edge2.getDistance());
            }
        });
    }

    public List<NewLineState> createLinesStates(Cell[][] cells){
        List<NewLineState> lines = new ArrayList<>();
        for (int y = 0; y < width; y++) {
            for (int x = 0; x < width; x++) {
                Cell cell = cells[y][x];
                int cellX = x*50;
                int cellY = y*50;//may have to swap x and y values
                if (cell.isTopWall()){
                    lines.add(createLineState(cellX, cellY, cellX+50, cellY));
                }
                if (cell.isLeftWall()){
                    lines.add(createLineState(cellX, cellY, cellX, cellY+ 50));
                }
                if (cell.isBottomWall()){
                    lines.add(createLineState(cellX, cellY+50, cellX+50, cellY+ 50));
                }
                if (cell.isRightWall()){
                    lines.add(createLineState(cellX+ 50, cellY, cellX+50, cellY+ 50));
                }
            }
        }
        return lines;
    }

    private NewLineState createLineState(int cellX, int cellY, int cellX2, int cellY2){
        return new NewLineState(new Pos(cellX, cellY), new Pos(cellX2, cellY2), 10);

    }

    public Vertex[][] create2DArray(){
        Vertex [][] vertices = new Vertex[width][width];
        int counter = 0;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j <width ; j++) {//horizontal
                Vertex vertex = this.vertices.get(counter);
                vertices[i][j] = vertex;
                vertex.setX(j*50);
                vertex.setY(i*50);
                counter++;
            }
        }

        return vertices;
    }

    public List<Vertex> getVertices() {
        return vertices;
    }

    public List<Edge> getEdges() {
        return edges;
    }
}