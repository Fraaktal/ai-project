package board;

import java.util.ArrayList;

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

    public CellState getState() {
        return state;
    }

    public Position getPosition() {
        return position;
    }
}
