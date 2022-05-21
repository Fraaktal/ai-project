package board;

import java.util.Objects;

/**
 * Représente une case du plateau
 */
public class Cell {
    /**
     * Etat courant
     */
    private final CellState state;

    /**
     * Coordonnées sur le plateau
     */
    private final Position position;

    public Cell(CellState state, Position coordinates) {
        this.state = state;
        this.position = coordinates;
    }

    /**
     * Constructeur de copie
     *
     * @param cell Case du plateau à copier
     */
    public Cell(Cell cell) {
        this.state = cell.getState();
        this.position = new Position(cell.getPosition());
    }

    public CellState getState() {
        return state;
    }

    public Position getPosition() {
        return position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return state == cell.state && Objects.equals(position, cell.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(state, position);
    }

    @Override
    public String toString() {
        return "Cell{" +
                "state=" + state +
                ", position=" + position +
                '}';
    }
}
