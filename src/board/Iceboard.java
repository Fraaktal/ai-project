package board;

import game.IceMove;
import game.IcebergRole;

import java.io.*;
import java.util.*;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Gestion du jeu Icebreaker
 */
public class Iceboard {
    /**
     * Taille du plateau
     */
    private final static int SIZE = 9;
    /**
     * Directions possibles pour les lignes au-dessus de E
     */
    private final static Position[] DIRECTIONS_TOP =
        new Position[]{
            new Position(-1, -1), new Position(-1, 0),
            new Position(0, -1), new Position(0, 1),
            new Position(1, 0), new Position(1, 1)
        };
    /**
     * Directions possibles pour la ligne E
     */
    private final static Position[] DIRECTIONS_MID =
        new Position[]{
            new Position(-1, -1), new Position(-1, 0),
            new Position(0, -1), new Position(0, 1),
            new Position(1, -1), new Position(1, 0)
        };
    /**
     * Directions possibles pour les lignes en dessous de E
     */
    private final static Position[] DIRECTIONS_DOWN =
        new Position[]{
            new Position(-1, 0), new Position(-1, 1),
            new Position(0, -1), new Position(0, 1),
            new Position(1, -1), new Position(1, 0)
        };

    /**
     * Cases du joueur rouge
     */
    private ArrayList<Cell> redPawns;
    /**
     * Cases du joueur noir
     */
    private ArrayList<Cell> blackPawns;
    /**
     * Plateau du jeu
     */
    private Cell[][] gameBoard;
    /**
     * Scores des joueurs
     */
    private int redScore, blackScore;

    /**
     * Constructeur par défaut
     */
    public Iceboard() {
        this.redScore = 0;
        this.blackScore = 0;

        this.redPawns = new ArrayList<>();
        this.blackPawns = new ArrayList<>();
        initializeBoard();
    }

    /**
     * Initialise le plateau avec le fichier par défaut
     */
    private void initializeBoard() {
        load(getClass().getResource("/resource/plateau_init.txt").getPath());
    }

    /**
     * Trouve la case dans le plateau
     *
     * @param p Coordonnées
     * @return Case correspondante
     */
    public Cell getCellAt(Position p) {
        // TODO: Verification (out of bounds)
        return this.gameBoard[p.getX()][p.getY()];
    }

    /**
     * Joue le mouvement sélectionné par le joueur
     *
     * @param move Mouvement
     * @param role Rôle du joueur
     */
    public void playMove(String move, IcebergRole role) {
        IceMove iceMove = new IceMove(move);
        var originCell = gameBoard[iceMove.getOrigin().getX()][iceMove.getOrigin().getY()];
        var destinationCell = gameBoard[iceMove.getDestination().getX()][iceMove.getDestination().getY()];

        if (destinationCell.getState() == CellState.ICEBERG) {
            if (role == IcebergRole.RED)
                redScore++;
            else
                blackScore++;
        }

        gameBoard[iceMove.getDestination().getX()][iceMove.getDestination().getY()] = originCell;
        // Mise à jour des coordonnées des pions
        originCell.getPosition().setX(iceMove.getDestination().getX());
        originCell.getPosition().setY(iceMove.getDestination().getY());

        gameBoard[iceMove.getOrigin().getX()][iceMove.getOrigin().getY()] = new Cell(CellState.EMPTY, iceMove.getOrigin());
    }

    /**
     * Calcule la nouvelle position à partir d'une case et la valide
     *
     * @param current Case actuelle
     * @param direction Direction à appliquer
     * @return Position si valide, null sinon
     */
    private Position getNeighborPosition(Position current, Position direction) {
        Position newPosition = new Position(current.getX() + direction.getX(), current.getY() + direction.getY());

        return newPosition.getX() >= 0 && newPosition.getX() < SIZE && newPosition.getY() >= 0 && newPosition.getY() < SIZE ? newPosition : null;
    }

    /**
     * Obtient un voisin éligible d'une case en fonction de la direction
     *
     * @param current Coordonnées de la case actuelle
     * @param direction Vecteur de direction pour obtenir le voisin
     * @param opponent Type de case adversaire
     * @return Case voisine si valide, null sinon
     */
    private Cell getNeighbor(Position current, Position direction, CellState opponent) {
        Position neighborPosition = getNeighborPosition(current, direction);

        if (neighborPosition == null) return null;

        Cell neighbor = this.gameBoard[neighborPosition.getX()][neighborPosition.getY()];

        if (neighbor == null || neighbor.getState() == opponent)
            return null;

        return neighbor;
    }

    /**
     * Obtient les voisins d'une case donnée
     *
     * @param current Case
     * @return Liste des cases voisines
     */
    private ArrayList<Cell> getNeighbors(Cell current, IcebergRole role) {
        Position position = current.getPosition();
        int midRow = SIZE / 2;
        ArrayList<Cell> neighbors = new ArrayList<>();
        CellState opponent = role == IcebergRole.RED ? CellState.BLACK : CellState.RED;

        if (position.getX() < midRow) {
            for (Position d : DIRECTIONS_TOP) {
                Cell neighbor = this.getNeighbor(position, d, opponent);

                if (neighbor != null)
                    neighbors.add(neighbor);
            }
        } else if (position.getX() == midRow) {
            for (Position d : DIRECTIONS_MID) {
                Cell neighbor = this.getNeighbor(position, d, opponent);

                if (neighbor != null)
                    neighbors.add(neighbor);
            }
        } else {
            for (Position d : DIRECTIONS_DOWN) {
                Cell neighbor = this.getNeighbor(position, d, opponent);

                if (neighbor != null)
                    neighbors.add(neighbor);
            }
        }

        return neighbors;
    }

    /**
     * Obtient les mouvements possibles d'un joueur
     *
     * @param role Rôle du joueur
     * @return Liste des mouvements possibles en format "LN-LN"
     */
    public Set<String> getPossibleMoves(IcebergRole role) {
        Set<String> moves = new HashSet<>();

        // Cases correspondant au joueur du rôle donné
        ArrayList<Cell> playerPawns = new ArrayList<>(role == IcebergRole.RED ? this.redPawns : this.blackPawns);

        for (Cell pawn : playerPawns) {
            // Breadth First Search
            Queue<Node> frontier = new PriorityQueue<>();
            frontier.add(new Node(pawn, 0));
            int maxDepth = 0;

            // Clé : case d'origine ; Valeur : case de destination
            Map<String, String> cameFrom = new HashMap<>();
            cameFrom.put(pawn.getPosition().toString(), null);

            ArrayList<Cell> nearestIcebergs = new ArrayList<>();

            while (!frontier.isEmpty()) {
                Node current = frontier.poll();

                if (current.getDepth() > maxDepth)
                    maxDepth = current.getDepth();

                if (current.getCell().getState() == CellState.ICEBERG) {
                    nearestIcebergs.add(current.getCell());

                    // Trouve les icebergs possibles dans la même profondeur
                    for (Node node : frontier) {
                        if (node.getDepth() > current.getDepth())
                            break;
                        if (node.getCell().getState() == CellState.ICEBERG) {
                            nearestIcebergs.add(node.getCell());
                        }
                    }

                    break;
                }

                for (Cell next : this.getNeighbors(current.getCell(), role)) {
                    if (!cameFrom.containsKey(next.getPosition().toString())) {
                        frontier.add(new Node(next, current.getDepth() + 1));
                        cameFrom.put(next.getPosition().toString(), current.getCell().getPosition().toString());
                    }
                }
            }

            // Récupère les cases voisines éligibles au mouvement
            for (Cell iceberg : nearestIcebergs) {
                String current = iceberg.getPosition().toString();
                ArrayList<String> path = new ArrayList<>();

                while (!current.equals(pawn.getPosition().toString())) {
                    path.add(current);
                    current = cameFrom.get(current);
                }

                Collections.reverse(path);
                moves.add(pawn.getPosition().toString() + '-' + path.get(0));
            }
        }

        return moves;
    }

    /**
     * Charge un fichier et le convertit en plateau
     *
     * @param fileName Chemin du fichier
     */
    public void load(String fileName) {
        try {
            this.redPawns = new ArrayList<>();
            this.blackPawns = new ArrayList<>();
            gameBoard = new Cell[SIZE][SIZE];

            File file = new File(fileName);
            InputStreamReader isr = new InputStreamReader(new FileInputStream(file));
            BufferedReader reader = new BufferedReader(isr);

            String line = reader.readLine();
            setScore(line);

            // On saute une ligne
            reader.readLine();
            int rowNumber = 0;

            while (reader.ready()) {
                line = reader.readLine();
                int index = 0;

                for (int i = 2; i < line.length(); i++) {
                    char c = line.charAt(i);
                    if (c == ' ') continue;

                    Cell cell = new Cell(CellState.fromChar(c), new Position(rowNumber, index));
                    gameBoard[rowNumber][index] = cell;

                    // Sauvegarde en mémoire des tuiles des joueurs
                    if (cell.getState() == CellState.RED)
                        this.redPawns.add(cell);
                    else if (cell.getState() == CellState.BLACK)
                        this.blackPawns.add(cell);

                    index++;
                }

                rowNumber++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setScore(String scoreLine){
        Pattern p = Pattern.compile(" Red Score : (?<red>([0-9]+)) --- Black Score : (?<black>([0-9]+))");
        Matcher m = p.matcher(scoreLine);
        if (m.find()) {
            redScore = Integer.parseInt(m.toMatchResult().group(1));
            blackScore = Integer.parseInt(m.toMatchResult().group(3));
        }
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("Red Score : " + redScore + " --- Black Score : " + blackScore + "\n\n");
        char line = 'A';
        for (int i = 0; i < SIZE; i++) {
            StringBuilder toPrint = new StringBuilder();
            int charCount = 0;

            for (int j = 0; j < gameBoard[i].length; j++) {
                if (gameBoard[i][j] != null) {
                    switch (gameBoard[i][j].getState()) {
                        case RED:
                            toPrint.append(" R  ");
                            charCount++;
                            break;
                        case BLACK:
                            toPrint.append(" B  ");
                            charCount++;
                            break;
                        case ICEBERG:
                            toPrint.append(" o  ");
                            charCount++;
                            break;
                        case EMPTY:
                            toPrint.append(" •  ");
                            charCount++;
                            break;
                    }
                }
            }

            for (int space = 0; space < SIZE - charCount; space++) {
                toPrint.insert(0, "  ");
            }

            s.append(line).append(" ").append(toPrint).append("\n");
            line++;
        }

        return s.toString();
    }
}
