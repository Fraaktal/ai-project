package game.algorithms;

import board.Iceboard;
import game.IcebergRole;

public class AlphaBeta {

    public AlphaBeta(IcebergRole role) {
        playerMaxRole = role;
        playerMinRole = role == IcebergRole.RED?IcebergRole.BLACK:IcebergRole.RED;
    }

    IcebergRole playerMaxRole, playerMinRole;


    public String bestMove(Iceboard board, IcebergRole playerRole, int depthMax) {
        System.out.println("[MiniMax]");
        String bestMove = null;
        int valMax = Integer.MIN_VALUE;

        for (var move :board.getPossibleMoves(playerRole)) {
            board.emulateMove(move, playerRole);
            int res = minMax(board,depthMax-1,Integer.MIN_VALUE, Integer.MAX_VALUE);
            System.out.println(res);
            if(bestMove == null || res > valMax){
                bestMove = move;
                valMax = res;
            }
        }
        System.out.println(valMax);
        return bestMove;
    }

    private int maxMin(Iceboard board, int depth, int alpha, int beta) {
        if(depth == 0 || board.isGameOver()) {
            return IcebreakHeuristic.evaluate(board, playerMaxRole);
        }

        for (var move :board.getPossibleMoves(playerMaxRole)) {
            var b = board.emulateMove(move,playerMaxRole);

            alpha = Math.max(alpha, minMax(b, depth-1, alpha, beta));
            if(alpha >= beta){
                return beta;
            }
        }

        return alpha;
    }

    private int minMax(Iceboard board, int depth, int alpha, int beta) {
        if(depth == 0 || board.isGameOver()) {
            return IcebreakHeuristic.evaluate(board, playerMinRole);
        }

        for (var move :board.getPossibleMoves(playerMinRole)) {
            var b = board.emulateMove(move,playerMinRole);

            beta = Math.min(beta, maxMin(b, depth-1, alpha, beta));
            if(alpha >= beta){
                return alpha;
            }
        }

        return beta;
    }
}