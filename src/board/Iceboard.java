package board;

import game.IceMove;

import java.io.*;
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

    public void playMove(String move) {
        IceMove iceMove = new IceMove(move);
        var cell = gameBoard[iceMove.getOriginLine()][iceMove.getOriginColumn()];
        gameBoard[iceMove.getDestinationLine()][iceMove.getDestinationColumn()] = cell;
        gameBoard[iceMove.getOriginLine()][iceMove.getOriginColumn()] = new Cell(CellState.EMPTY);
    }

    public void load(String fileName) {
        try {
            gameBoard = new Cell[9][9];

            File file = new File(fileName);
            InputStreamReader isr = new InputStreamReader(new FileInputStream(file));
            BufferedReader reader = new BufferedReader(isr);

            String line = reader.readLine();
            setScore(line);

            //on saute une ligne
            reader.readLine();
            int lineNumber = 0;

            while (reader.ready()) {
                line = reader.readLine();
                int index = 0;

                for (int i = 2; i < line.length(); i++) {
                    char c = line.charAt(i);
                    if(c == ' ') continue;
                    gameBoard[lineNumber][index] = new Cell(CellState.fromChar(c));
                    index++;
                }

                lineNumber++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setScore(String scoreLine){
        Pattern p = Pattern.compile(" Red Score : (?<red>([0-9]+)) --- Black Score : (?<black>([0-9]+))");
        Matcher m = p.matcher(scoreLine);
        if (m.find()) {
            redScore = Integer.parseInt(m.toMatchResult().group(1));
            blackScore = Integer.parseInt(m.toMatchResult().group(3));
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
            String toPrint = "";
            int charCount = 0;

            for(int j = 0; j < gameBoard[i].length; j++){
                if(gameBoard[i][j] != null){
                    switch (gameBoard[i][j].getState()){
                        case RED:
                            toPrint+= " R  ";
                            charCount++;
                            break;
                        case BLACK:
                            toPrint += " B  ";
                            charCount++;
                            break;
                        case ICEBERG:
                            toPrint += " o  ";
                            charCount++;
                            break;
                        case EMPTY:
                            toPrint += " •  ";
                            charCount++;
                            break;
                    }
                }
            }

            for(int space = 0;space < 9-charCount;space++){
                toPrint = "  " + toPrint;
            }

            s += line + " " + toPrint + "\n";
            line++;
        }

        return s;
    }
}
