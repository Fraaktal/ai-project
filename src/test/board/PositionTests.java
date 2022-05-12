package test.board;

import static org.junit.jupiter.api.Assertions.assertEquals;

import board.Position;

import org.junit.jupiter.api.Test;

public class PositionTests {
    final static int SIZE = 9;

    @Test
    void formatTest() {
        Position p = new Position(0, 0);
        assertEquals("A1", p.toString());

        p = new Position(4, 8);
        assertEquals("E9", p.toString());
    }
}
