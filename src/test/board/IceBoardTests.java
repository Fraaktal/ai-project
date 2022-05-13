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
    private final Iceboard iceboard = new Iceboard();

    @Test
    void possibleMovesTest() {
        Set<String> possibleMoves = iceboard.getPossibleMoves(IcebergRole.RED);
        assertEquals(9, possibleMoves.size());
        Set<String> expectedMoves = new HashSet<>(Arrays.asList("A1-A2", "A1-B1", "A1-B2", "E9-D8", "E9-E8", "E9-F8", "I1-H1", "I1-H2", "I1-I2"));
        assertTrue(expectedMoves.containsAll(possibleMoves));

        possibleMoves = iceboard.getPossibleMoves(IcebergRole.BLACK);
        assertEquals(9, possibleMoves.size());
        expectedMoves = new HashSet<>(Arrays.asList("A5-A4", "A5-B5", "A5-B6", "E1-E2", "E1-D1", "E1-F1", "I5-I4", "I5-H5", "I5-H6"));
        assertTrue(expectedMoves.containsAll(possibleMoves));

        /*
        Iceboard iceboard1 = new Iceboard();
        iceboard1.load(getClass().getResource("/resource/plateau1.txt").getPath());
        Set<String> possibleMoves = iceboard1.getPossibleMoves(IcebergRole.RED);
        System.out.println(possibleMoves);
        assertEquals(11, possibleMoves.size());
        // TODO:
        Set<String> expectedMoves = new HashSet<>(Arrays.asList("C4-C3", "C4-D4", "C4-D5", "H2-H1", "H2-H3", "H2-G2", "H2-G3", "H2-I2", "E9-E8", "E9-D8", "E9-F8"));
        assertTrue(expectedMoves.containsAll(possibleMoves));
        possibleMoves = iceboard1.getPossibleMoves(IcebergRole.BLACK);
        // TODO:
        expectedMoves = new HashSet<>(Arrays.asList("A5-A4", "A5-B5", "A5-B6", "E1-E2", "E1-D1", "E1-F1", "I5-I4", "I5-H5", "I5-H6"));
        assertTrue(expectedMoves.containsAll(possibleMoves));*/

        Iceboard iceboard1 = new Iceboard();
        iceboard1.load(getClass().getResource("/resource/plateau2.txt").getPath());
        System.out.println(iceboard1);
        possibleMoves = iceboard1.getPossibleMoves(IcebergRole.RED);
        System.out.println(possibleMoves);
        assertEquals(11, possibleMoves.size());
        expectedMoves = new HashSet<>(Arrays.asList("C4-C3", "C4-D4", "C4-D5", "H2-H1", "H2-H3", "H2-G2", "H2-G3", "H2-I2", "E9-E8", "E9-D8", "E9-F8"));
        assertTrue(expectedMoves.containsAll(possibleMoves));
        possibleMoves = iceboard1.getPossibleMoves(IcebergRole.BLACK);
        // TODO:
        expectedMoves = new HashSet<>(Arrays.asList("A5-A4", "A5-B5", "A5-B6", "E1-E2", "E1-D1", "E1-F1", "I5-I4", "I5-H5", "I5-H6"));
        assertTrue(expectedMoves.containsAll(possibleMoves));
    }

    @Test
    void moveTest() {
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