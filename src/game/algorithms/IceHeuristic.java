package game.algorithms;

import board.Iceboard;
import game.IcebergRole;

// TODO
public class IceHeuristic implements IHeuristic {
    public IceHeuristic() {}

    @Override
    public int evaluate(Iceboard board, IcebergRole role) {
        return Integer.MIN_VALUE;
    }
}
