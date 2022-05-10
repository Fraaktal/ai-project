package board;

import java.util.ArrayList;

public class Cell {
    /**
     * Current state
     */
    private CellState state;
    /**
     * Edges of this node (for path-finding)
     */
    private ArrayList<Edge> edges = new ArrayList<Edge>();
    private boolean visited;
    private int distanceFromSource = Integer.MAX_VALUE;

    public Cell(CellState state) {
        this.state = state;
    }

    public CellState getState() {
        return state;
    }

    public void setState(CellState state) {
        this.state = state;
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    public void setEdges(ArrayList<Edge> edges) {
        this.edges = edges;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public int getDistanceFromSource() {
        return distanceFromSource;
    }

    public void setDistanceFromSource(int distanceFromSource) {
        this.distanceFromSource = distanceFromSource;
    }
}
