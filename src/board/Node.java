package board;

/**
 * Noeud pour le BFS
 */
public class Node implements Comparable<Node> {
    /**
     * Case sur le plateau
     */
    private final Cell cell;
    /**
     * Niveau de profondeur
     * Permet ensuite de récupérer les icebergs situés au même niveau
     */
    private final int depth;

    public Node(Cell cell, int depth) {
        this.cell = cell;
        this.depth = depth;
    }

    @Override
    public int compareTo(Node other) {
        return this.cell.getPosition().toString().compareTo(other.getCell().getPosition().toString());
    }

    public Cell getCell() {
        return cell;
    }

    public int getDepth() {
        return depth;
    }
}
