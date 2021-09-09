package Maze;

public class Edge {
    private Vertex vertex1;
    private Vertex vertex2;
    private int distance;

    public Edge(Vertex vertex1,Vertex vertex2, int distance) {
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        this.distance = distance;
    }

    public boolean hasConnectionToVertex(Vertex vertex){
        return vertex1 == vertex || vertex2 == vertex;
    }

    public Vertex getVertex1() {
        return vertex1;
    }

    public Vertex getVertex2() {
        return vertex2;
    }

    public int getDistance() {
        return distance;
    }
}
