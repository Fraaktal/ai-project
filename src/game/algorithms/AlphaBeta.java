package game.algorithms;

import board.Iceboard;
import game.IcebergRole;

/**
 * Implémentation de l'algorithme Minimax pour l'élaboration de notre stratégie,
 * utilisant Alpha Bêta pour couper les branches non pertinentes
 */
public class AlphaBeta {

    /** Joueur à maximiser, le nôtre */
    IcebergRole playerMaxRole;
    /** Joueur à minimiser, l'adversaire */
    IcebergRole playerMinRole;
    /** Heuristic to apply */
    IHeuristic heuristic;

    public AlphaBeta(IcebergRole role, IHeuristic heuristic) {
        this.playerMaxRole = role;
        this.playerMinRole = role == IcebergRole.RED ? IcebergRole.BLACK : IcebergRole.RED;
        this.heuristic = heuristic;
    }

    /**
     * Détermine le meilleur mouvement à faire selon les mouvements autorisés
     *
     * @param board Plateau du jeu actuel
     * @param playerRole Rôle du joueur à faire gagner
     * @param depthMax Profondeur maximale pour l'iterative deepening
     * @return Mouvement qui doit être joué
     */
    public String bestMove(Iceboard board, IcebergRole playerRole, int depthMax) {
        String bestMove = null;
        int valMax = Integer.MIN_VALUE;

        for (var move : board.getPossibleMoves(playerRole)) {
            board.emulateMove(move, playerRole);
            int res = minMax(board,depthMax - 1, Integer.MIN_VALUE, Integer.MAX_VALUE);
            if (bestMove == null || res > valMax) {
                bestMove = move;
                valMax = res;
            }
        }

        return bestMove;
    }

    private int maxMin(Iceboard board, int depth, int alpha, int beta) {
        if (depth == 0 || board.isGameOver()) {
            return this.heuristic.evaluate(board, this.playerMaxRole);
        }

        for (var move : board.getPossibleMoves(this.playerMaxRole)) {
            var b = board.emulateMove(move, this.playerMaxRole);

            alpha = Math.max(alpha, minMax(b, depth - 1, alpha, beta));

            if (alpha >= beta)
                return beta;
        }

        return alpha;
    }

    private int minMax(Iceboard board, int depth, int alpha, int beta) {
        if (depth == 0 || board.isGameOver()) {
            return this.heuristic.evaluate(board, this.playerMinRole);
        }

        for (var move : board.getPossibleMoves(this.playerMinRole)) {
            var b = board.emulateMove(move, this.playerMinRole);

            beta = Math.min(beta, maxMin(b, depth - 1, alpha, beta));

            if (alpha >= beta)
                return alpha;
        }

        return beta;
    }
}