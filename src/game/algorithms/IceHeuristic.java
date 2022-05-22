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

        var pawns = board.getBoats(role);
        int result = 0;
        for (var pawn:pawns) {
            var icebergs = board.getNearestIcebergs(pawn);
            for (var iceberg : icebergs) {
                var heap = board.getHeap(iceberg);
                int distanceAmi = board.computeDistance(pawn.getX(), pawn.getY(), iceberg.getX(), iceberg.getY());
                int distanceEnnemi = board.getMinEnemyDistance(heap, role);
                result += computeResult(heap.size(), distanceAmi, distanceEnnemi);
            }
        }
        return result;
    }

    private int computeResult(int heap, int distanceAmi, int distanceEnnemi){
        int amiScore = 0;
        int ennemiScore = 0;
        while (heap > 0){
            //on commence par ennemi (on vient de jouer)
            if (distanceEnnemi == 1){
                heap--;
                ennemiScore++;
            }
            else {
                distanceEnnemi--;
            }

            if (heap>0) {
                if (distanceAmi == 1) {
                    heap--;
                    amiScore++;
                }
                else {
                    distanceAmi--;
                }
            }
        }

        return amiScore - ennemiScore;
    }
}
