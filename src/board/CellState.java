package board;

/**
 * Différents états d'une case
 */
public enum CellState {
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
            default: return null;
        }
    }
}
