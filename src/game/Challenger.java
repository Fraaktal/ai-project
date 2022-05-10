package game;

import java.util.Set;

public class Challenger implements IChallenger{
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

    }

    @Override
    public void otherPlay(String move) {

    }

    @Override
    public String bestMove() {
        return null;
    }

    @Override
    public String victory() {
        return null;
    }

    @Override
    public String defeat() {
        return null;
    }

    @Override
    public String tie() {
        return null;
    }

    @Override
    public String boardToString() {
        return null;
    }

    @Override
    public void setBoardFromFile(String fileName) {

    }

    @Override
    public Set<String> possibleMoves(String role) {
        return null;
    }
}
