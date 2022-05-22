package test.board;

import board.Iceboard;
import board.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HeapTest {
    @Test
    void HeapAtInitTest() {
        Iceboard board = new Iceboard();
        var res = board.getHeap(new Position(3,3));
        assertEquals(55, res.size());
    }
}
