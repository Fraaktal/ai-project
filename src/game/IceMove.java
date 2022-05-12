package game;

public class IceMove {

    public IceMove(String move){
        var parts = move.split("-");
        originLine = parts[0].trim().charAt(0)-'A'; //line origin
        originColumn =  parts[0].trim().charAt(1)-'1'; //column origin

        destinationLine =  parts[1].trim().charAt(0) - 'A'; //line destination
        destinationColumn =  parts[1].trim().charAt(1) - '1'; //column destination
    }

    private int originLine, originColumn, destinationLine, destinationColumn;

    public int getDestinationColumn() {
        return destinationColumn;
    }

    public int getDestinationLine() {
        return destinationLine;
    }

    public int getOriginColumn() {
        return originColumn;
    }

    public int getOriginLine() {
        return originLine;
    }
}
