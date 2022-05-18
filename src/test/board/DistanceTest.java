package test.board;

import board.Cell;
import board.CellState;
import board.Iceboard;
import board.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DistanceTest {
    @Test
    void formatTest() {
        Iceboard board = new Iceboard();
        int res = board.computeDistance(new Cell(CellState.ICEBERG,new Position(1,1)), new Cell(CellState.ICEBERG,new Position(4,3)));
        assertEquals(3, res);
        res = board.computeDistance(new Cell(CellState.ICEBERG,new Position(1,1)), new Cell(CellState.ICEBERG,new Position(1,2)));
        assertEquals(1, res);
        res = board.computeDistance(new Cell(CellState.ICEBERG,new Position(1,1)), new Cell(CellState.ICEBERG,new Position(2,1)));
        assertEquals(1, res);

        res = board.computeDistance(new Cell(CellState.ICEBERG,new Position(1,1)), new Cell(CellState.ICEBERG,new Position(4,7)));
        assertEquals(6, res);
    }
}
