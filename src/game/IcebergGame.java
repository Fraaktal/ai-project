package game;

public class IcebergGame {
    public static void main(String[] args) {
        MyChallenger challenger = new MyChallenger();
        System.out.print(challenger.boardToString());
        challenger.setBoardFromFile("C:\\Users\\trava\\OneDrive\\Bureau\\Polytech\\IA\\plateau.txt");
        System.out.print(challenger.boardToString());
        challenger.setBoardFromFile("C:\\Users\\trava\\OneDrive\\Bureau\\Polytech\\IA\\plateau2.txt");
        System.out.print(challenger.boardToString());
        challenger.setBoardFromFile("C:\\Users\\trava\\OneDrive\\Bureau\\Polytech\\IA\\init.txt");
        System.out.print(challenger.boardToString());
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
