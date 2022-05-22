package game.algorithms;

import board.Iceboard;
import game.IcebergRole;

public class IceHeuristic implements IHeuristic {
    public IceHeuristic() {}

    /**
     * Evalue le plateau
     * @param board Plateau
     * @param role Rôle du joueur actuel
     * @return
     */
    @Override
    public int evaluate(Iceboard board, IcebergRole role) {
        var boats = board.getBoats(role);
        int result = 0;

        if(board.getEnnemyScore(role) == 28){
            return Integer.MIN_VALUE;
        }
        else if (board.getScore(role) == 28){
            return Integer.MAX_VALUE;
        }

        for (var boat : boats) {
            var icebergs = board.getNearestIcebergs(boat);

            for (var iceberg : icebergs) {
                var heap = board.getHeap(iceberg);
                int distanceFriend = board.computeDistance(boat.getX(), boat.getY(), iceberg.getX(), iceberg.getY());
                int distanceEnemy = board.getMinEnemyDistance(heap, role);
                result += computeResult(heap.size(), distanceFriend, distanceEnemy);
            }
        }
        return result;
    }

    /**
     *
     * @param heap Nombre d'icebergs dans l'amas
     * @param distanceAmi Distance du pion allié
     * @param distanceEnnemi Distance du plus proche ennemi
     * @return
     */
    private int computeResult(int heap, int distanceAmi, int distanceEnnemi){
        int scoreFriend = 0;
        int scoreEnemy = 0;
        while (heap > 0){
            //on commence par ennemi (on vient de jouer)
            if (distanceEnnemi == 1){
                heap--;
                scoreEnemy++;
            }
            else {
                distanceEnnemi--;
            }

            if (heap>0) {
                if (distanceAmi == 1) {
                    heap--;
                    scoreFriend++;
                }
                else {
                    distanceAmi--;
                }
            }
        }

        return scoreFriend - scoreEnemy;
    }
}
