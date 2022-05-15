package test.board;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import board.Cell;
import board.CellState;
import board.Iceboard;

import game.IceMove;
import game.IcebergRole;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

class IceboardTests {

    @Test
    void possibleMovesTest() {
        Iceboard iceboard = new Iceboard();

        Set<String> possibleMoves = iceboard.getPossibleMoves(IcebergRole.RED);
        Set<String> expectedMoves = new HashSet<>(Arrays.asList("A1-A2", "A1-B1", "A1-B2", "E9-D8", "E9-E8", "E9-F8", "I1-H1", "I1-H2", "I1-I2"));
        assertTrue(possibleMoves.containsAll(expectedMoves));
        assertEquals(possibleMoves.size(), expectedMoves.size());

        possibleMoves = iceboard.getPossibleMoves(IcebergRole.BLACK);
        assertEquals(9, possibleMoves.size());
        expectedMoves = new HashSet<>(Arrays.asList("A5-A4", "A5-B5", "A5-B6", "E1-E2", "E1-D1", "E1-F1", "I5-I4", "I5-H5", "I5-H6"));
        assertTrue(possibleMoves.containsAll(expectedMoves));
        assertEquals(possibleMoves.size(), expectedMoves.size());
    }

    @Test
    void getPossibleMoves1Test() {
        Iceboard iceboard = new Iceboard();
        iceboard.load(getClass().getResourceAsStream("/resource/plateau1.txt"));

        Set<String> possibleMoves = iceboard.getPossibleMoves(IcebergRole.RED);
        Set<String> expectedMoves = new HashSet<>(Arrays.asList("A1-A2", "A1-B2", "E9-E8", "E9-D8", "I1-I2"));
        assertTrue(possibleMoves.containsAll(expectedMoves));
        assertEquals(possibleMoves.size(), expectedMoves.size());

        possibleMoves = iceboard.getPossibleMoves(IcebergRole.BLACK);
        expectedMoves = new HashSet<>(Arrays.asList("C6-C5", "I5-I4", "E1-E2"));
        assertTrue(possibleMoves.containsAll(expectedMoves));
        assertEquals(possibleMoves.size(), expectedMoves.size());
    }

    @Test
    void getPossibleMoves2Test() {
        Iceboard iceboard = new Iceboard();
        iceboard.load(getClass().getResourceAsStream("/resource/plateau2.txt"));

        Set<String> possibleMoves = iceboard.getPossibleMoves(IcebergRole.RED);
        Set<String> expectedMoves = new HashSet<>(Arrays.asList("C4-C3", "C4-D4", "C4-D5", "H2-H1", "H2-H3", "H2-G2", "H2-G3", "H2-I2", "E9-E8", "E9-D8", "E9-F8"));
        assertTrue(possibleMoves.containsAll(expectedMoves));
        assertEquals(possibleMoves.size(), expectedMoves.size());

        possibleMoves = iceboard.getPossibleMoves(IcebergRole.BLACK);
        expectedMoves = new HashSet<>(Arrays.asList("E1-E2", "E1-D1", "E1-F1", "H5-H4", "H5-H6", "H5-G5", "H5-G6", "H5-I4", "E6-E5", "E6-E7", "E6-D5", "E6-F5", "E6-F6"));
        assertTrue(possibleMoves.containsAll(expectedMoves));
        assertEquals(possibleMoves.size(), expectedMoves.size());
    }

    @Test
    void getPossibleMoves3Test() {
        Iceboard iceboard = new Iceboard();
        iceboard.load(getClass().getResourceAsStream("/resource/plateau3.txt"));

        Set<String> possibleMoves = iceboard.getPossibleMoves(IcebergRole.RED);
        Set<String> expectedMoves = new HashSet<>();
        assertTrue(possibleMoves.containsAll(expectedMoves));
        assertEquals(possibleMoves.size(), expectedMoves.size());

        possibleMoves = iceboard.getPossibleMoves(IcebergRole.BLACK);
        expectedMoves = new HashSet<>(Arrays.asList("H1-I1", "H2-I1", "I2-I1"));
        assertTrue(possibleMoves.containsAll(expectedMoves));
        assertEquals(possibleMoves.size(), expectedMoves.size());
    }

    @Test
    void getPossibleMoves4Test() {
        Iceboard iceboard = new Iceboard();
        iceboard.load(getClass().getResourceAsStream("/resource/plateau4.txt"));

        Set<String> possibleMoves = iceboard.getPossibleMoves(IcebergRole.RED);
        Set<String> expectedMoves = new HashSet<>(Arrays.asList("G3-F4"));
        assertTrue(possibleMoves.containsAll(expectedMoves));
        assertEquals(possibleMoves.size(), expectedMoves.size());

        possibleMoves = iceboard.getPossibleMoves(IcebergRole.BLACK);
        expectedMoves = new HashSet<>(Arrays.asList("H1-I1", "H2-I1", "I2-I1"));
        assertTrue(possibleMoves.containsAll(expectedMoves));
        assertEquals(possibleMoves.size(), expectedMoves.size());
    }

    @Test
    void getPossibleMoves5Test() {
        Iceboard iceboard = new Iceboard();
        iceboard.load(getClass().getResourceAsStream("/resource/plateau5.txt"));

        Set<String> possibleMoves = iceboard.getPossibleMoves(IcebergRole.RED);
        Set<String> expectedMoves = new HashSet<>(Arrays.asList("A2-A1", "B2-A1", "A3-B4"));
        assertTrue(possibleMoves.containsAll(expectedMoves));
        assertEquals(possibleMoves.size(), expectedMoves.size());
    }

    @Test
    void moveTest() {
        Iceboard iceboard = new Iceboard();
        Set<String> possibleMoves = iceboard.getPossibleMoves(IcebergRole.RED);
        String move = possibleMoves.iterator().next();
        IceMove iceMove = new IceMove(move);
        iceboard.playMove(move, IcebergRole.RED);
        Cell moved = iceboard.getCellAt(iceMove.getDestination());
        Cell origin = iceboard.getCellAt(iceMove.getOrigin());

        assertEquals(iceMove.getDestination().toString(), moved.getPosition().toString());
        assertEquals(CellState.RED, moved.getState());
        assertEquals(iceMove.getOrigin().toString(), origin.getPosition().toString());
        assertEquals(CellState.EMPTY, origin.getState());
    }
}