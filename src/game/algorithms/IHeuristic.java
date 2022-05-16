package game.algorithms;

import board.Iceboard;
import game.IcebergRole;

/**
 * Représente une heuristique qui nous permet de déployer une stratégie
 */
public interface IHeuristic {
    /**
     * Evalue l'état d'un tableau selon l'heuristique définie
     *
     * @param board Plateau
     * @param role Rôle du joueur actuel
     * @return Nombre entier qui favorise/défavorise le joueur qui a la main
     */
    int evaluate(Iceboard board, IcebergRole role);
}
