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

    private static final int TIME_LIMIT_MS = 480000;

    /** Interrupt search when the time limit dedicated to it is reached */
    private static boolean searchAborted = false;

    public AlphaBeta(IcebergRole role, IHeuristic heuristic) {
        this.playerMaxRole = role;
        this.playerMinRole = role == IcebergRole.RED ? IcebergRole.BLACK : IcebergRole.RED;
        this.heuristic = heuristic;
    }

    /**
     * Détermine le meilleur mouvement à faire selon les mouvements autorisés
     * avec une profondeur maximale définie à l'avance
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

    /**
     * Trouve le meilleur mouvement à faire parmi les mouvements possibles
     * en utilisant l'iterative deepening
     *
     * @param board Plateau à un état précis
     * @param playerRole Rôle du joueur à maximiser
     * @return Mouvement au format "LN-LN"
     */
    public String bestMoveID(Iceboard board, IcebergRole playerRole) {
        String bestMove = null;
        int valMax = Integer.MIN_VALUE;

        var moves = board.getPossibleMoves(playerRole);

        for (var move : moves) {
            board.emulateMove(move, playerRole);

            long computeTimeLimit = getTimeLimit2(board.getTurns(), moves.size());

            int res = computeID(board, computeTimeLimit);

            if (bestMove == null || res > valMax) {
                bestMove = move;
                valMax = res;
            }
        }

        return bestMove;
    }

    /**
     * Permet d'attribuer le temps de calcul
     * Gagne contre alphabeta fourni 28/27 si RED
     * Perds contre alphabeta fourni 26/28 si BLACK
     * @param turns
     * @param moves
     * @return
     */
    private long getTimeLimit(int turns, int moves) {
        if(turns < 15){return TIME_LIMIT_MS * 10 / 100 / 16 / moves;}
        else if(turns < 30){return TIME_LIMIT_MS * 30 / 100 / 15 / moves;}
        else if(turns < 50){return TIME_LIMIT_MS * 40 / 100 / 20 / moves;}
        else{return TIME_LIMIT_MS * 20 / 100 / 20 / moves;}
    }

    /**
     * Permet d'attribuer le temps de calcul
     * Gagne contre alphabeta fourni 28/27 si RED
     * Perds contre alphabeta fourni 26/28 si BLACK
     * @param turns
     * @param moves
     * @return
     */
    private long getTimeLimit2(int turns, int moves) {
        if(turns < 5){return TIME_LIMIT_MS * 10 / 100 / 5 / moves;}
        else if(turns < 20){return TIME_LIMIT_MS * 20 / 100 / 15 / moves;}
        else if(turns < 40){return TIME_LIMIT_MS * 25 / 100 / 10 / moves;}
        else if(turns < 50){return TIME_LIMIT_MS * 30/ 100 / 10 / moves;}
        else{return TIME_LIMIT_MS * 15 / 100 / 20 / moves;}
    }

    /**
     * partie max de l'algorithme minimax
     * @param board Plateau à un état précis
     * @param depth Profondeur restante
     * @param alpha Valeur courante d'alpha
     * @param beta Valeur courante de beta
     * @return
     */
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

    /**
     * partie min de l'algorithme minimax
     * @param board Plateau à un état précis
     * @param depth Profondeur restante
     * @param alpha Valeur courante d'alpha
     * @param beta Valeur courante de beta
     * @return
     */
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

    /**
     * Vérifie si le temps alloué est écoulé
     * @param timeStart Temps de départ
     * @param timeLimit Limite de temps
     */
    private void checkTimeElapsed(long timeStart, long timeLimit) {
        long timeCurrent = System.currentTimeMillis();
        long timeElapsed = (timeCurrent - timeStart);

        if (timeElapsed >= timeLimit)
            searchAborted = true;
    }

    /**
     * partie max de l'algorithme minimax avec Iterative Deepening
     * @param board Plateau à un état précis
     * @param depth Profondeur restante
     * @param alpha Valeur courante d'alpha
     * @param beta Valeur courante de beta
     * @param timeStart Temps de départ
     * @param timeLimit Limite de temps
     * @return
     */
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

    /**
     * partie min de l'algorithme minimax avec Iterative Deepening
     * @param board Plateau à un état précis
     * @param depth Profondeur restante
     * @param alpha Valeur courante d'alpha
     * @param beta Valeur courante de beta
     * @param timeStart Temps de départ
     * @param timeLimit Limite de temps
     * @return
     */
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

    /**
     * Utilise l'Iterative Deepening pour trouver le meilleur coup
     * @param board Plateau à un état précis
     * @param timeLimit Limite de temps
     * @return
     */
    private int computeID(Iceboard board, long timeLimit) {
        long timeStart = System.currentTimeMillis();
        long timeEnd = timeStart + timeLimit;

        int depth = 1;
        int score = 0;

        searchAborted = false;

        while (true) {
            long timeCurrent = System.currentTimeMillis();

            if (timeCurrent >= timeEnd)
                break;

            int itScore = minMaxIt(board, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, timeCurrent, timeEnd - timeCurrent);

            if (itScore >= winRes)
                return itScore;

            if (!searchAborted)
                score = itScore;

            depth++;
        }

        return score;
    }
}