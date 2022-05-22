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
    private ArrayList<Position> redBoats;
    /**
     * Cases du joueur noir
     */
    private ArrayList<Position> blackBoats;
    /**
     * Plateau du jeu
     */
    private CellState[][] gameBoard;
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

        this.redBoats = new ArrayList<>();
        this.blackBoats = new ArrayList<>();
        initializeBoard();
    }

    /**
     * Constructeur de copie
     *
     * @param iceboard Etat du plateau à copier
     */
    private Iceboard(Iceboard iceboard) {
        this.redScore = iceboard.redScore;
        this.blackScore = iceboard.blackScore;

        this.redBoats = (ArrayList<Position>) iceboard.redBoats.stream().map(Position::new).collect(toList());
        this.blackBoats = (ArrayList<Position>) iceboard.blackBoats.stream().map(Position::new).collect(toList());

        this.gameBoard = new CellState[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) { System.arraycopy(iceboard.gameBoard[i], 0, this.gameBoard[i], 0, SIZE); }
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
    public CellState getCellAt(Position p) {
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

        if (destinationCell == CellState.ICEBERG) {
            if (role == IcebergRole.RED)
                redScore++;
            else
                blackScore++;
        }

        if (role == IcebergRole.RED){
            for (var p : redBoats) {
                if(p.equals(iceMove.getOrigin())){
                    p.setX(iceMove.getDestination().getX());
                    p.setY(iceMove.getDestination().getY());
                }
            }
        }
        else {
            for (var p : blackBoats) {
                if(p.equals(iceMove.getOrigin())){
                    p.setX(iceMove.getDestination().getX());
                    p.setY(iceMove.getDestination().getY());
                }
            }
        }

        gameBoard[iceMove.getOrigin().getX()][iceMove.getOrigin().getY()] = CellState.EMPTY;
        gameBoard[iceMove.getDestination().getX()][iceMove.getDestination().getY()] = originCell;
    }

    /**
     * Obtient le nouveau état du plateau après un mouvement
     *
     * @param move Mouvement en format "L0-L0"
     * @param role Rôle du joueur qui joue actuellement
     * @return Nouveau plateau
     */
    public Iceboard emulateMove(String move, IcebergRole role) {
        Iceboard newBoard = new Iceboard(this);
        newBoard.playMove(move, role);
        return newBoard;
    }

    /**
     * Obtient les cases des bâteaux d'un joueur donné
     *
     * @param role Rôle du joueur
     * @return Liste des références des bâteaux
     */
    public ArrayList<Position> getBoats(IcebergRole role) {
        return new ArrayList<>(role == IcebergRole.RED ? this.redBoats : this.blackBoats);
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
     * @return Coordonnées de la case voisine si valide, null sinon
     */
    private Position getNeighbor(Position current, Position direction) {
        Position neighborPosition = getNeighborPosition(current, direction);

        if (neighborPosition == null) return null;

        CellState neighborState = this.gameBoard[neighborPosition.getX()][neighborPosition.getY()];

        if (neighborState == null
                || neighborState == CellState.UNDEFINED
                || neighborState == CellState.RED
                || neighborState == CellState.BLACK)
            return null;

        return neighborPosition;
    }

    /**
     * Obtient les voisins d'une case donnée
     *
     * @param current Case
     * @return Liste des cases voisines
     */
    private ArrayList<Position> getNeighbors(Position current) {
        int midRow = SIZE / 2;
        ArrayList<Position> neighbors = new ArrayList<>();

        if (current.getX() < midRow) {
            for (var d : DIRECTIONS_TOP) {
                Position neighbor = this.getNeighbor(current, d);

                if (neighbor != null)
                    neighbors.add(neighbor);
            }
        } else if (current.getX() == midRow) {
            for (var d : DIRECTIONS_MID) {
                Position neighbor = this.getNeighbor(current, d);

                if (neighbor != null)
                    neighbors.add(neighbor);
            }
        } else {
            for (var d : DIRECTIONS_DOWN) {
                Position neighbor = this.getNeighbor(current, d);

                if (neighbor != null)
                    neighbors.add(neighbor);
            }
        }

        return neighbors;
    }

    public ArrayList<Position> getNearestIcebergs(Position boat) {
        LinkedList<SimpleImmutableEntry<Position, Integer>> frontier = new LinkedList<>();
        frontier.add(new SimpleImmutableEntry<>(boat, 0));
        int maxDepth = 0;

        ArrayList<Position> nearestIcebergs = new ArrayList<>();

        while (!frontier.isEmpty()) {
            SimpleImmutableEntry<Position, Integer> current = frontier.removeFirst();

            // On passe au niveau suivant
            if (current.getValue() > maxDepth)
                maxDepth = current.getValue();

            if (this.gameBoard[current.getKey().getX()][current.getKey().getY()] == CellState.ICEBERG) {
                nearestIcebergs.add(current.getKey());

                // Trouve les icebergs possibles dans la même profondeur
                for (var node : frontier) {
                    if (node.getValue() > current.getValue())
                        break;

                    if (this.gameBoard[node.getKey().getX()][node.getKey().getY()]  == CellState.ICEBERG)
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
    public HashSet<Position> getHeap(Position iceberg) {
        // BFS
        LinkedList<Position> frontier = new LinkedList<>();
        frontier.add(iceberg);
        HashSet<Position> icebergs = new HashSet<>();
        icebergs.add(iceberg);

        while (!frontier.isEmpty()) {
            Position current = frontier.removeFirst();

            var neighbors = getNeighbors(current);

            for (var n : neighbors) {
                // Check in board directly
                if (this.gameBoard[n.getX()][n.getY()] == CellState.ICEBERG && !icebergs.contains(n)) {
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
    public int getMinEnemyDistance(HashSet<Position> heap, IcebergRole role) {
        IcebergRole r = role == IcebergRole.RED ? IcebergRole.BLACK : IcebergRole.RED;
        var boats = getBoats(r);
        int distMin = 10;

        for (var boat : boats) {
            for (var iceberg : heap) {
                int tmp = computeDistance(iceberg.getX(), iceberg.getY(), boat.getX(), boat.getY());

                if (tmp < distMin)
                    distMin = tmp;
            }
        }

        return distMin;
    }

    /**
     * Calcule la distance entre deux cases du plateau
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
        if (x1 < x2) { diagXTran = 1; }
        else if (x1 > x2) { diagXTran = -1; }
        else { diagXTran = 0; }

        if (y1 < y2) { diagYTran = 1; }
        else if (y1 > y2) { diagYTran = -1; }
        else { diagYTran = 0; }

        boolean isDiag = diagXTran != 0 && diagYTran != 0;

        while (isDiag){
            x1 += diagXTran;
            y1 += diagYTran;
            isDiag = x1 != x2 && y1 != y2;
            distance++;
        }

        distance += Math.abs(x2 - x1);
        distance += Math.abs(y2 - y1);

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
        ArrayList<Position> playerBoats = getBoats(role);

        for (var boat : playerBoats) {
            // Breadth First Search (largeur)
            LinkedList<SimpleImmutableEntry<Position, Integer>> frontier = new LinkedList<>();
            frontier.add(new SimpleImmutableEntry<>(boat, 0));
            int maxDepth = 0;

            // Clé : case d'origine ; Valeur : case de destination
            Map<String, ArrayList<Entry<String, Integer>>> cameFrom = new HashMap<>();
            cameFrom.put(boat.toString(), new ArrayList<>());

            ArrayList<Position> nearestIcebergs = new ArrayList<>();

            while (!frontier.isEmpty()) {
                SimpleImmutableEntry<Position, Integer> current = frontier.removeFirst();

                // On passe au niveau suivant
                if (current.getValue() > maxDepth)
                    maxDepth = current.getValue();

                if (this.gameBoard[current.getKey().getX()][current.getKey().getY()] == CellState.ICEBERG) {
                    nearestIcebergs.add(current.getKey());

                    // Trouve les icebergs possibles dans la même profondeur
                    for (var node : frontier) {
                        if (node.getValue() > current.getValue())
                            break;

                        if (this.gameBoard[node.getKey().getX()][node.getKey().getY()] == CellState.ICEBERG)
                            nearestIcebergs.add(node.getKey());
                    }

                    break;
                }

                for (var next : this.getNeighbors(current.getKey())) {
                    String currentKey = current.getKey().toString();
                    int newDepth = current.getValue() + 1;
                    String nextKey = next.toString();
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
                String icebergPosition = iceberg.toString();
                ArrayList<String> paths = new ArrayList<>();
                paths.add(icebergPosition);

                // Parents d'un iceberg
                for (var iceParent : cameFrom.get(icebergPosition)) {
                    HashSet<String> parents = new HashSet<>(){{ add(iceParent.getKey()); }};

                    // Déroulement des chemins les plus courts trouvés précédemment
                    while (parents.stream().noneMatch(parent -> parent.equals(boat.toString()))) {
                        paths = new ArrayList<>();
                        // Récupère les grands-parents (profondeur précédente) pour dérouler tous les chemins possibles
                        HashSet<String> grandParents = new HashSet<>();

                        for (var parent : parents) {
                            paths.add(parent);

                            grandParents.addAll(cameFrom.get(parent).stream().map(Entry::getKey).collect(toList()));
                        }
                        parents = grandParents;
                    }

                    moves.addAll(paths.stream().map(path -> boat.toString() + '-' + path).collect(toList()));
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
            this.redBoats = new ArrayList<>();
            this.blackBoats = new ArrayList<>();
            this.gameBoard = new CellState[SIZE][SIZE];

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

                    CellState state = CellState.fromChar(c);
                    gameBoard[rowNumber][index] = state;

                    // Sauvegarde en mémoire des tuiles des joueurs
                    if (state == CellState.RED)
                        this.redBoats.add(new Position(rowNumber, index));
                    else if (state == CellState.BLACK)
                        this.blackBoats.add(new Position(rowNumber, index));

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
                    switch (gameBoard[i][j]) {
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
