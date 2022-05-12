package board;

/**
 * Différents états d'une case
 */
public enum CellState {
    OUT_OF_BOARD,
    RED,
    BLACK,
    ICEBERG,
    EMPTY;

    public static CellState fromChar(char c) {
        switch (c) {
            case 'o': return CellState.ICEBERG;
            case '•': return CellState.EMPTY;
            case 'R': return CellState.RED;
            case 'B': return CellState.BLACK;
            default: return CellState.OUT_OF_BOARD;
        }
    }
}
