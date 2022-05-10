package game;

import java.util.ArrayList;
import java.util.Set;

public class Iceboard {
    private final static int SIZE = 9;
    private Cell[][] gameBoard;
    private Edge[] edges;
    private int edgesCount;
    private int redScore, blackScore;

    public Iceboard() {
        redScore = 0;
        blackScore = 0;
        initializeBoard();
    }

    private void initializeBoard() {
        int rangeMin = 0;
        int rangeMax = 4;
        this.gameBoard = new Cell[SIZE][SIZE];
        for(int i = 0 ; i < SIZE ; i++){
            for (int j = 0 ; j < SIZE ; j++) {
                if(j >= rangeMin && j <= rangeMax){
                    // TODO: Set edges
                    this.gameBoard[i][j] = new Cell(CellState.ICEBERG);
                }
                else {
                    // TODO: Set edges for valid cells
                    this.gameBoard[i][j] = new Cell(CellState.OUT_OF_BOARD);
                }
            }
        }

        // Initial player positions
        this.gameBoard[0][4] = new Cell(CellState.RED);
        this.gameBoard[4][0] = new Cell(CellState.BLACK);
        this.gameBoard[4][8] = new Cell(CellState.BLACK);
        this.gameBoard[0][0] = new Cell(CellState.RED);
        this.gameBoard[8][0] = new Cell(CellState.RED);
        this.gameBoard[8][4] = new Cell(CellState.BLACK);
    }

    public static void playMove(String move) {
    }

    private void initializeGameBoard() {

    }

    ArrayList<ArrayList<GameState>> gameBoard;

    public void load(String fileName) {

    }

    public Set<String> getPossibleMoves(String role) {
        return null;
    }

    @Override
    public String toString() {
        return "";
    }
}
