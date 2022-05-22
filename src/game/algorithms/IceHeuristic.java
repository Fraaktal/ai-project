package game.algorithms;

import board.Iceboard;
import game.IcebergRole;

public class IceHeuristic implements IHeuristic {
    public IceHeuristic() {}

    @Override
    public int evaluate(Iceboard board, IcebergRole role) {
        /*
        pour chaque pion allié :
	    on récup les iceberg proches
	    pour chaque iceberg :
		nombre dans l'amas
		distance de l'allié
		distance de l'ennemi le plus proche
		resultat += icebergs récup par ami - iceberg récup par ennemi
        retourner result.
        */

        var boats = board.getBoats(role);
        int result = 0;
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
