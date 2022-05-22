package game.algorithms;

import board.Iceboard;
import game.IcebergRole;

/**
 * Implémentation de l'algorithme Minimax pour l'élaboration de notre stratégie,
 * utilisant Alpha Bêta pour couper les branches non pertinentes
 */
public class AlphaBeta {
    private static final int winRes = 28;

    /** Joueur à maximiser, le nôtre */
    IcebergRole playerMaxRole;
    /** Joueur à minimiser, l'adversaire */
    IcebergRole playerMinRole;
    /** L'heuristique à appliquer */
    IHeuristic heuristic;

    private static final int TIME_LIMIT_MS = 480000/61;

    private static boolean searchAborted = false;

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

        var moves = board.getPossibleMoves(playerRole);

        for (var move : moves) {
            board.emulateMove(move, playerRole);

            int res = minMax(board,depthMax - 1, Integer.MIN_VALUE, Integer.MAX_VALUE);
            if (bestMove == null || res > valMax) {
                bestMove = move;
                valMax = res;
            }
        }

        return bestMove;
    }

    public String bestMoveID(Iceboard board, IcebergRole playerRole) {
        String bestMove = null;
        int valMax = Integer.MIN_VALUE;

        var moves = board.getPossibleMoves(playerRole);

        for (var move : moves) {
            // TODO: Use another object other than board itself?
            board.emulateMove(move, playerRole);

            long computeTimeLimit = (TIME_LIMIT_MS - 1000) / (moves.size());

            int res = computeID(board, computeTimeLimit);

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

    private void checkTimeElapsed(long timeStart, long timeLimit) {
        long timeCurrent = System.currentTimeMillis();
        long timeElapsed = (timeCurrent - timeStart);

        if (timeElapsed >= timeLimit)
            searchAborted = true;
    }

    private int maxMinIt(Iceboard board, int depth, int alpha, int beta, long timeStart, long timeLimit) {
        checkTimeElapsed(timeStart, timeLimit);

        if (searchAborted || depth == 0 || board.isGameOver())
            return this.heuristic.evaluate(board, this.playerMaxRole);

        for (var move : board.getPossibleMoves(this.playerMaxRole)) {
            var childBoard = board.emulateMove(move, this.playerMaxRole);

            alpha = Math.max(alpha, minMaxIt(childBoard, depth - 1, alpha, beta, timeStart, timeLimit));

            if (alpha >= beta)
                return beta;
        }

        return alpha;
    }

    private int minMaxIt(Iceboard board, int depth, int alpha, int beta, long timeStart, long timeLimit) {
        checkTimeElapsed(timeStart, timeLimit);

        if (searchAborted || depth == 0 || board.isGameOver())
            return this.heuristic.evaluate(board, this.playerMinRole);

        for (var move : board.getPossibleMoves(this.playerMinRole)) {
            var b = board.emulateMove(move, this.playerMinRole);

            beta = Math.min(beta, maxMinIt(b, depth - 1, alpha, beta, timeStart, timeLimit));

            if (alpha >= beta)
                return alpha;
        }

        return beta;
    }

    private int computeID(Iceboard board, long timeLimit) {
        long timeStart = System.currentTimeMillis();
        long timeEnd = timeStart + timeLimit;

        int depth = 1;
        int res = 0;

        searchAborted = false;

        while (true) {
            long timeCurrent = System.currentTimeMillis();

            if (timeCurrent >= timeEnd)
                break;

            int itRes = minMaxIt(board, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, timeCurrent, timeEnd - timeCurrent);

            if (itRes >= winRes)
                return itRes;

            if (!searchAborted)
                res = itRes;

            depth++;
        }

        return res;
    }
}