package game;

import board.Iceboard;

public class IcebergGame {
    public static void main(String[] args) {
        Challenger challenger = new Challenger();
        System.out.print(challenger.boardToString());
        challenger.setBoardFromFile("/home/tom/dev/IA/plateau_initial.txt");
        challenger.setRole("RED");

        challenger.iPlay("A1-A2");
        System.out.print(challenger.boardToString());
        challenger.otherPlay("A5-A4");
        System.out.print(challenger.boardToString());

        System.out.print(challenger.victory());
        System.out.print("\n");
        System.out.print(challenger.defeat());
        System.out.print("\n");
        System.out.print(challenger.tie());
    }
}
