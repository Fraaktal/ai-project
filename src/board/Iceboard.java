package board;

import game.IceMove;
import game.IcebergRole;

import java.io.*;
import java.util.*;
import java.util.Set;
import java.util.Map.Entry;
import java.util.AbstractMap.SimpleEntry;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toList;

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
     * @return Case voisine si valide, null sinon
     */
    private Cell getNeighbor(Position current, Position direction) {
        Position neighborPosition = getNeighborPosition(current, direction);

        if (neighborPosition == null) return null;

        Cell neighbor = this.gameBoard[neighborPosition.getX()][neighborPosition.getY()];

        if (neighbor == null
                || neighbor.getState() == CellState.UNDEFINED
                || neighbor.getState() == CellState.RED
                || neighbor.getState() == CellState.BLACK)
            return null;

        return neighbor;
    }

    /**
     * Obtient les voisins d'une case donnée
     *
     * @param current Case
     * @return Liste des cases voisines
     */
    private ArrayList<Cell> getNeighbors(Cell current) {
        Position position = current.getPosition();
        int midRow = SIZE / 2;
        ArrayList<Cell> neighbors = new ArrayList<>();

        if (position.getX() < midRow) {
            for (var d : DIRECTIONS_TOP) {
                Cell neighbor = this.getNeighbor(position, d);

                if (neighbor != null)
                    neighbors.add(neighbor);
            }
        } else if (position.getX() == midRow) {
            for (var d : DIRECTIONS_MID) {
                Cell neighbor = this.getNeighbor(position, d);

                if (neighbor != null)
                    neighbors.add(neighbor);
            }
        } else {
            for (var d : DIRECTIONS_DOWN) {
                Cell neighbor = this.getNeighbor(position, d);

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

        for (var pawn : playerPawns) {
            // Breadth First Search (largeur)
            LinkedList<SimpleImmutableEntry<Cell, Integer>> frontier = new LinkedList<>();
            frontier.add(new SimpleImmutableEntry<>(pawn, 0));
            int maxDepth = 0;

            // Clé : case d'origine ; Valeur : case de destination
            Map<String, ArrayList<Entry<String, Integer>>> cameFrom = new HashMap<>();
            cameFrom.put(pawn.getPosition().toString(), new ArrayList<>());

            ArrayList<Cell> nearestIcebergs = new ArrayList<>();

            while (!frontier.isEmpty()) {
                SimpleImmutableEntry<Cell, Integer> current = frontier.removeFirst();

                // On passe au niveau suivant
                if (current.getValue() > maxDepth)
                    maxDepth = current.getValue();

                if (current.getKey().getState() == CellState.ICEBERG) {
                    nearestIcebergs.add(current.getKey());

                    // Trouve les icebergs possibles dans la même profondeur
                    for (var node : frontier) {
                        if (node.getValue() > current.getValue())
                            break;

                        if (node.getKey().getState() == CellState.ICEBERG)
                            nearestIcebergs.add(node.getKey());
                    }

                    break;
                }

                for (var next : this.getNeighbors(current.getKey())) {
                    String currentKey = current.getKey().getPosition().toString();
                    int newDepth = current.getValue() + 1;
                    String nextKey = next.getPosition().toString();
                    if (!cameFrom.containsKey(nextKey)) {
                        frontier.offer(new SimpleImmutableEntry<>(next, newDepth));
                        cameFrom.put(nextKey, new ArrayList<>(){{ add(new SimpleEntry<>(currentKey, newDepth)); }});
                    } else if (cameFrom.get(nextKey).stream().allMatch(parent -> parent.getValue() == newDepth)) {
                        cameFrom.get(nextKey).add(new SimpleEntry<>(currentKey, newDepth));
                    }
                }
            }

            // Récupère les cases voisines éligibles au mouvement
            for (var iceberg : nearestIcebergs) {
                String icebergPosition = iceberg.getPosition().toString();
                ArrayList<String> paths = new ArrayList<>();
                paths.add(icebergPosition);

                // Parents d'un iceberg
                for (var iceParent : cameFrom.get(icebergPosition)) {
                    HashSet<String> parents = new HashSet<>(){{ add(iceParent.getKey()); }};

                    // Déroulement des chemins les plus courts trouvés précédemment
                    while (parents.stream().noneMatch(parent -> parent.equals(pawn.getPosition().toString()))) {
                        paths = new ArrayList<>();
                        // Récupère les grands-parents (profondeur précédente) pour dérouler tous les chemins possibles
                        HashSet<String> grandParents = new HashSet<>();

                        for (var parent : parents) {
                            paths.add(parent);

                            grandParents.addAll(cameFrom.get(parent).stream().map(Entry::getKey).collect(toList()));
                        }
                        parents = grandParents;
                    }

                    moves.addAll(paths.stream().map(path -> pawn.getPosition().toString() + '-' + path).collect(toList()));
                }
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

    /**
     * Modifie le score actuel des deux joueurs à partir d'un fichier
     *
     * @param scoreLine Ligne du fichier contenant les scores
     */
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
