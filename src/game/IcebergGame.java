package game;

import board.Iceboard;

public class IcebergGame {
    public static void main(String[] args) {
        Iceboard board = new Iceboard();
        System.out.print(board.toString());
        board.load("C:\\Users\\trava\\OneDrive\\Bureau\\Plateau_initial.txt");
    }
}
