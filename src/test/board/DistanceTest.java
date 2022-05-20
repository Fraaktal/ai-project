package test.board;

import board.Cell;
import board.CellState;
import board.Iceboard;
import board.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DistanceTest {
    @Test
    void DistanceTest() {
        Iceboard board = new Iceboard();
        int res = board.computeDistance(1,1,4,3);
        assertEquals(3, res);
        res = board.computeDistance(1,1,1,2);
        assertEquals(1, res);
        res = board.computeDistance(1,1,2,1);
        assertEquals(1, res);

        res = board.computeDistance(1,1,4,7);
        assertEquals(6, res);
    }
}
