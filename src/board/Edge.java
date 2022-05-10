package board;

public class Edge {
    /** Cell index in array */
    private int fromCellIndex;
    private int toCellIndex;
    private int length;

    public Edge(int fromCellIndex, int toCellIndex, int length) {
        this.fromCellIndex = fromCellIndex;
        this.toCellIndex = toCellIndex;
        this.length = length;
    }

    /**
     * Obtient le voisin d'une cellule dans l'arc
     *
     * @param cellIndex Index de la cellule
     * @return
     */
    public int getNeighbourIndex(int cellIndex) {
        if (this.fromCellIndex == cellIndex)
            return this.fromCellIndex;
        return this.toCellIndex;
    }

    public int getFromCellIndex() {
        return fromCellIndex;
    }

    public void setFromCellIndex(int fromCellIndex) {
        this.fromCellIndex = fromCellIndex;
    }

    public int getToCellIndex() {
        return toCellIndex;
    }

    public void setToCellIndex(int toCellIndex) {
        this.toCellIndex = toCellIndex;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
