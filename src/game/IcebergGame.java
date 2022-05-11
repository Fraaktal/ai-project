package game;

import board.Iceboard;

public class IcebergGame {
    public static void main(String[] args) {
        Iceboard board = new Iceboard();
        System.out.print(board.toString());
        board.load("E:\\plateau_init.txt");
    }
}
