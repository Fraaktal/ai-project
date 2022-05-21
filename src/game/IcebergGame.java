package game;

public class IcebergGame {
    public static void main(String[] args) {
        MyChallenger challenger = new MyChallenger();
        System.out.print(challenger.teamName());
        System.out.print("\n");
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

        System.out.print("Possible moves RED :\n");
        System.out.print(challenger.possibleMoves("RED"));
        System.out.print("\nPossible moves BLACK :\n");
        System.out.print(challenger.possibleMoves("BLACK"));
        System.out.print("\n");

        System.out.print(challenger.victory());
        System.out.print("\n");
        System.out.print(challenger.defeat());
        System.out.print("\n");
        System.out.print(challenger.tie());

        System.out.print("\n--------------------\n");
        challenger = new MyChallenger();
        challenger.setRole("RED");
        System.out.print("\nPossible moves RED :\n");
        System.out.print(challenger.possibleMoves("RED"));
        System.out.print(challenger.boardToString());
        var move = challenger.bestMove();
        System.out.print("\nbest move RED :\n");
        System.out.print(move);

        System.out.print(challenger.boardToString());
        System.out.print("\nPossible moves RED :\n");
        System.out.print(challenger.possibleMoves("RED"));
        challenger.iPlay(move);

        System.out.print(challenger.boardToString());

        challenger.otherPlay("A5-A4");

        System.out.print("\nPossible moves RED :\n");
        System.out.print(challenger.possibleMoves("RED"));
        move = challenger.bestMove();
        System.out.print("\nbest move RED :\n");
        System.out.print(move);
        System.out.print("\nPossible moves RED :\n");
        System.out.print(challenger.possibleMoves("RED"));
        challenger.iPlay(move);



    }
}
