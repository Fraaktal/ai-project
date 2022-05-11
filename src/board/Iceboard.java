package board;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public void load(String fileName) {
        try
        {
            int r = 0;
            var lines= Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);

            for (int lineNumber=0;lineNumber<lines.size();lineNumber++) {
                if(lineNumber == 0){
                    Pattern p= Pattern.compile("Red Score : (?<red>([0-9]+)) --- Black Score : (?<black>([0-9]+))");
                    Matcher m= p.matcher(lines.get(lineNumber));
                    if(m.matches()) {
                        var test = m.toMatchResult().group(1);
                        var test2 = m.toMatchResult().group(3);
                    }
                    lineNumber++;
                }
                else if (lineNumber == 1) continue;
                else{

                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    //TODO: sofi
    public Set<String> getPossibleMoves(String role) {
        return null;
    }

    @Override
    public String toString() {
        String s = "Red Score : " + redScore + " --- Black Score : " + blackScore + "\n\n";
        char line = 'A';
        for(int i = 0; i < 9; i++){
            s += line + " ";
            for(int j = 0; j < 9; j++){
                switch (gameBoard[i][j].getState()){
                    case RED:
                        s+= " R ";
                        break;
                    case BLACK:
                        s += " B ";
                        break;
                    case ICEBERG:
                        s += " O ";
                        break;
                    case EMPTY:
                        s += " • ";
                        break;
                }
            }

            line++;
            s += "\n";
        }

        return s;
    }
}
