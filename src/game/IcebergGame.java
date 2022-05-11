package game;

import board.Iceboard;

public class IcebergGame {
    public static void main(String[] args) {
        Iceboard board = new Iceboard();
        board.load("/home/tom/dev/IA/plateau_initial.txt");
        System.out.println("Chargement terminé");
        System.out.print(board.toString());
        IceMove move = new IceMove("D2-C3");
    }
}
