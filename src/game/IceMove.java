package game;

import board.Position;

/**
 * Représente un mouvement de case Joueur vers une case disponible
 */
public class IceMove {
    /**
     * Coordonnées de la case d'origine
     */
    private final Position origin;
    /**
     * Coordonnées de la case de destination
     */
    private final Position destination;

    public IceMove(Position origin, Position destination) {
        this.origin = origin;
        this.destination = destination;
    }

    /**
     * Constructeur à partir d'un mouvement en format "A0-A0"
     *
     * @param move Chaîne de caractères contenant le mouvement
     */
    public IceMove(String move) {
        String[] fragments = move.split("-");
        this.origin = new Position(fragments[0]);
        this.destination = new Position(fragments[1]);
    }

    public Position getDestination() {
        return destination;
    }

    public Position getOrigin() {
        return origin;
    }

    public String toString() {
        return this.origin.toString() + '-' + this.destination.toString();
    }
}
