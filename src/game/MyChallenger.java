package game;

import board.Iceboard;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Set;

public class MyChallenger implements IChallenger {

    public MyChallenger() {
        gameBoard = new Iceboard();
        bestMoveSimulator = new BestMoveSimulator();
    }

    private BestMoveSimulator bestMoveSimulator;
    private Iceboard gameBoard;
    private IcebergRole role;

    @Override
    public String teamName() {
        return "Augeraud-Nguyen-Ravanel";
    }

    @Override
    public void setRole(String role) {
        this.role = IcebergRole.valueOf(role);
    }

    @Override
    public void iPlay(String move) {
        gameBoard.playMove(move, role);
    }

    @Override
    public void otherPlay(String move) {
        var r = role == IcebergRole.RED?IcebergRole.BLACK:IcebergRole.RED;
        gameBoard.playMove(move, r);
    }

    @Override
    public String bestMove() {
        return bestMoveSimulator.getBestMove(gameBoard, role);
    }

    @Override
    public String victory() {
        return "fanfare_theme_final_fantasy.mp3";
    }

    @Override
    public String defeat() {
        return "Call an ambulance, for me... :'(";
    }

    @Override
    public String tie() {
        return "We both suck bro";
    }

    @Override
    public String boardToString() {
        return gameBoard.toString();
    }

    @Override
    public void setBoardFromFile(String fileName) {
        try {
            gameBoard.load(new FileInputStream(fileName));
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Set<String> possibleMoves(String role) {
        return gameBoard.getPossibleMoves(IcebergRole.valueOf(role));
    }
}
