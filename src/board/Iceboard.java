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

    private Iceboard copy(Iceboard iceboard) {
        Iceboard result = new Iceboard();
        result.redScore = iceboard.redScore;
        result.blackScore = iceboard.blackScore;
        result.redPawns = iceboard.redPawns;
        result.blackPawns = iceboard.blackPawns;
        result.gameBoard = new Cell[SIZE][SIZE];

        for(int i=0;i<SIZE;i++){
            System.arraycopy(iceboard.gameBoard[i], 0, result.gameBoard[i], 0, SIZE);
        }

        return result;
    }

    /**
     * Initialise le plateau avec le fichier par défaut
     */
    private void initializeBoard() {
        load(getClass().getResourceAsStream("/resource/plateau_init.txt"));
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

    public Iceboard emulateMove(String move, IcebergRole role){
        Iceboard newBoard = copy(this);
        newBoard.playMove(move, role);
        return newBoard;
    }

    /**
     * Obtient les cases des bâteaux d'un joueur donné
     *
     * @param role Rôle du joueur
     * @return Liste des références des bâteaux
     */
    public ArrayList<Cell> getPawns(IcebergRole role) {
        return new ArrayList<>(role == IcebergRole.RED ? this.redPawns : this.blackPawns);
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

    public ArrayList<Cell> getNearestIcebergs(Cell pawn) {
        LinkedList<SimpleImmutableEntry<Cell, Integer>> frontier = new LinkedList<>();
        frontier.add(new SimpleImmutableEntry<>(pawn, 0));
        int maxDepth = 0;

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
        }

        return nearestIcebergs;
    }

    /**
     * Récupère les icebergs autour d'un iceberg donné
     * qui forme un ensemble
     * pour le calcul de l'héuristique
     *
     * @param iceberg Case contenant un iceberg
     * @return Liste des icebergs proches
     */
    public HashSet<Cell> getHeap(Cell iceberg) {
        // BFS
        LinkedList<Cell> frontier = new LinkedList<>();
        frontier.add(iceberg);
        HashSet<Cell> icebergs = new HashSet<>();
        icebergs.add(iceberg);

        while (!frontier.isEmpty()) {
            Cell current = frontier.removeFirst();

            var neighbors = getNeighbors(current);

            for (var n : neighbors) {
                if (n.getState() == CellState.ICEBERG && !icebergs.contains(n)) {
                    frontier.add(n);
                    icebergs.add(n);
                }
            }
        }

        return icebergs;
    }

    /**
     * Donne la distance minimale de l'ennemi de role vers un amas donné.
     *
     * @param heap Amas d'icebergs
     * @param role Rôle du joueur
     * @return
     */
    public int getMinEnnemiDistance(HashSet<Cell> heap, IcebergRole role) {
        IcebergRole r = role == IcebergRole.RED ? IcebergRole.BLACK : IcebergRole.RED;
        var pawns = getPawns(r);
        int distMin = 10;

        for (var pawn : pawns) {
            for (var iceberg : heap) {
                int tmp = computeDistance(iceberg.getPosition().getX(),iceberg.getPosition().getY(), pawn.getPosition().getX(), pawn.getPosition().getY());

                if (tmp < distMin) {
                    distMin = tmp;
                }
            }
        }

        return distMin;
    }

    /**
     * Calcule la distance entre deux cases du plateau
     * TODO: Use Position (copy constructor)
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public int computeDistance(int x1, int y1, int x2, int y2) {
        int diagXTran = 0;
        int diagYTran = 0;
        int distance = 0;
        if(x1 < x2){diagXTran = 1;}
        else if (x1 > x2){diagXTran = -1;}
        else{diagXTran = 0;}

        if(y1 < y2){diagYTran = 1;}
        else if (y1 > y2){diagYTran = -1;}
        else{diagYTran = 0;}

        boolean isDiag = diagXTran != 0 && diagYTran != 0;

        while(isDiag){
            x1+=diagXTran;
            y1+=diagYTran;
            isDiag = x1 != x2 && y1 != y2;
            distance++;
        }

        distance += Math.abs(x2-x1);
        distance += Math.abs(y2-y1);

        return distance;
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
     * @param stream stream du fichier
     */
    public void load(InputStream stream) {
        try {
            this.redPawns = new ArrayList<>();
            this.blackPawns = new ArrayList<>();
            gameBoard = new Cell[SIZE][SIZE];

            InputStreamReader isr = new InputStreamReader(stream);
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

    public boolean isGameOver() {
        return redScore >= 28 || blackScore >= 28;
    }
}
