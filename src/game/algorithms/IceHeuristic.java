package game.algorithms;

import board.Iceboard;
import game.IcebergRole;

import java.security.KeyPair;

// TODO
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

        var pawns = board.getPawns(role);
        int result = 0;
        for (var pawn:pawns) {
            var icebergs = board.getNearestIcebergs(pawn);
            for (var iceberg:icebergs) {
                var amas = board.getAmas(iceberg);
                int distanceAmi = board.computeDistance(pawn.getPosition().getX(), pawn.getPosition().getY(), iceberg.getPosition().getX(), iceberg.getPosition().getY());
                int distanceEnnemi = board.getMinEnnemiDistance(amas, role);
                result += computeResult(amas.size(), distanceAmi, distanceEnnemi);
            }
        }
        return result;
    }

    private int computeResult(int amas, int distanceAmi, int distanceEnnemi){
        int amiScore = 0;
        int ennemiScore = 0;
        while(amas > 0){
            //on commence par ennemi (on vient de jouer)
            if(distanceEnnemi == 1){
                amas--;
                ennemiScore++;
            }
            else{
                distanceEnnemi--;
            }

            if(amas>0){
                if(distanceAmi == 1){
                    amas--;
                    amiScore++;
                }
                else{
                    distanceAmi--;
                }
            }
        }

        return amiScore-ennemiScore;
    }
}
