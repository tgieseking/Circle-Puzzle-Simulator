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

    public Puzzle(ArrayList<CutCircle> cs, HashSet<Position> pos, HashSet<Piece> ps) {
        circles = cs;
        positions = pos;
        pieces = ps;
        turningCircles = new ArrayList<CutCircle>();
        for(CutCircle c : circles) {
            if(c.isTurningCircle()) turningCircles.add(c);
        }
        border = new Border();
    }
    public Puzzle(ArrayList<CutCircle> cs, ArrayList<CutCircle> tc, HashSet<Position> pos, HashSet<Piece> ps) {
        circles = cs;
        positions = pos;
        pieces = ps;
        turningCircles = tc;
        border = new Border();
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
        System.out.println(turningCircles.get(circleNum).getCenterX());
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
}
