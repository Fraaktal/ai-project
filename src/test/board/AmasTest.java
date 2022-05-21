package test.board;

import board.Cell;
import board.CellState;
import board.Iceboard;
import board.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AmasTest {
    @Test
    void AmasTest_1() {
        Iceboard board = new Iceboard();
        var res = board.getHeap(new Cell(CellState.ICEBERG, new Position(3,3)));
        assertEquals(55, res.size());
    }
}
