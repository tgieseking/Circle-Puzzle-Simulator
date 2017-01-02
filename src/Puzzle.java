import java.util.*;
import javafx.scene.paint.Color;

public class Puzzle {
    private ArrayList<CutCircle> circles;
    public ArrayList<CutCircle> getCircles() {return circles;}
    private ArrayList<CutCircle> turningCircles;
    public ArrayList<CutCircle> getTurningCircles() {return turningCircles;}
    private HashSet<Piece> pieces;
    public HashSet<Piece> getPieces() {return pieces;}
    private HashSet<Position> positions;
    public HashSet<Position> getPositions() {return positions;}
    private Border border;
    public Border getBorder() {return border;}
    private ArrayList<Color> defaultColors;

    public Puzzle(ArrayList<CutCircle> cs, HashSet<Position> pos, HashSet<Piece> ps, ArrayList<Color> cols) {
        circles = cs;
        positions = pos;
        pieces = ps;
        turningCircles = new ArrayList<CutCircle>();
        for(CutCircle c : circles) {
            if(c.isTurningCircle()) turningCircles.add(c);
        }
        border = new Border();
        defaultColors = cols;
    }
    public Puzzle(ArrayList<CutCircle> cs, ArrayList<CutCircle> tc, HashSet<Position> pos, HashSet<Piece> ps, ArrayList<Color> cols) {
        circles = cs;
        positions = pos;
        pieces = ps;
        turningCircles = tc;
        border = new Border();
        defaultColors = cols;
    }

    public void testFunc() {
        pieces = new HashSet<Piece>();
        System.out.println("Working?");
    }

    public void turnCW(CutCircle turningCircle) {
        for(Piece piece : pieces) {
            piece.turnCW(turningCircle);
        }
    }
    public void turnCW(int circleNum) {
        for(Piece piece : pieces) {
            piece.turnCW(turningCircles.get(circleNum));
        }
    }
    public void turnCCW(CutCircle turningCircle) {
        for(Piece piece : pieces) {
            piece.turnCCW(turningCircle);
        }
    }
    public void turnCCW(int circleNum) {
        for(Piece piece : pieces) {
            piece.turnCCW(turningCircles.get(circleNum));
        }
    }

    public void reset() {
        Boolean colored;
        for(Piece piece : pieces) {
            colored = false;
            for(int i=0; i<turningCircles.size(); i++) {
                if(!colored && piece.getBoundingCircles().contains(turningCircles.get(i))) {
                    colored = true;
                    if(defaultColors.size() > i) piece.setColor(defaultColors.get(i));
                    else piece.setColor(Color.GRAY);
                }
            }
        }
    }

    public void removeIncompleteCycles() {
        HashSet<Position> removedPositions;
        HashSet<Position> newRemovedPositions = new HashSet<Position>();
        HashSet<Piece> piecesToRemove = new HashSet<Piece>();
        Boolean done;
        for(Piece piece : pieces) {
            done = false;
            for(CutCircle boundingCircle : piece.getPosition().getNextClockwise().keySet()) {
                if(!done && boundingCircle.isTurningCircle() && (piece.getPosition().getNextClockwise().get(boundingCircle)==null||piece.getPosition().getNextCounterClockwise().get(boundingCircle)==null)) {
                    done = true;
                    newRemovedPositions.add(piece.getPosition());
                    piecesToRemove.add(piece);
                }
            }
        }
        for(Piece piece : piecesToRemove) pieces.remove(piece);
        while(newRemovedPositions.size()>0) {
            removedPositions = newRemovedPositions;
            newRemovedPositions = new HashSet<Position>();
            piecesToRemove = new HashSet<Piece>();
            for(Position badPosition : removedPositions) {
                for(Piece piece : pieces) {
                    done = false;
                    for(CutCircle boundingCircle : piece.getPosition().getNextClockwise().keySet()) {
                        if(!done && boundingCircle.isTurningCircle() && (piece.getPosition().getNextClockwise().get(boundingCircle)==badPosition||piece.getPosition().getNextCounterClockwise().get(boundingCircle)==badPosition)) {
                            done = true;
                            newRemovedPositions.add(piece.getPosition());
                            piecesToRemove.add(piece);
                        }
                    }
                }
            }
            for(Piece piece : piecesToRemove) pieces.remove(piece);
        }
    }
}
