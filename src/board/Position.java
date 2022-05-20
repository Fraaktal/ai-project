package board;

import java.util.Objects;

public class Position {
    /**
     * Coordonnée dans l'axe x
     */
    private int x;
    /**
     * Coordonnée dans l'axe y
     */
    private int y;

    /**
     * Constructeur par défaut
     *
     * @param x Coordonnée dans l'axe x
     * @param y Coordonnée dans l'axe y
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Constructeur de copie
     *
     * @param position Coordonnées à copier
     */
    public Position(Position position) {
        this.x = position.getX();
        this.y = position.getY();
    }

    /**
     * Constructeur à partir de coordonnées en format L0
     *
     * @param str Coordonnées
     */
    public Position(String str) {
        this.x = str.trim().charAt(0) - 'A';
        this.y = str.trim().charAt(1) - '1';
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String toString() {
        return (x >= 0 && x < 27 ? String.valueOf((char) (x + 'A')) : null) + (y + 1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return x == position.x && y == position.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
