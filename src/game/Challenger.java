package game;

import java.util.Set;

public class Challenger implements IChallenger{

    public Challenger(){
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
        Iceboard.playMove(move);
    }

    @Override
    public void otherPlay(String move) {
        Iceboard.playMove(move);
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
        gameBoard.load(fileName);
    }

    @Override
    public Set<String> possibleMoves(String role) {
        return gameBoard.getPossibleMoves(role);
    }
}
