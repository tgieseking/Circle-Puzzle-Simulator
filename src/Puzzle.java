import java.util.*;
import javafx.scene.paint.Color;

public class Puzzle {
    private ArrayList<CutCircle> circles;
    public ArrayList<CutCircle> getCircles() {return circles;}
    private HashSet<Piece> pieces;
    public HashSet<Piece> getPieces() {return pieces;}
    private HashSet<Position> positions;
    public HashSet<Position> getPositions() {return positions;}

    public Puzzle(ArrayList<CutCircle> cs, HashSet<Position> pos, HashSet<Piece> ps) {
        circles = cs;
        positions = pos;
        pieces = ps;
    }

    public void testFunc() {
        pieces = new HashSet<Piece>();
        System.out.println("Working?");
    }

    public void turnCW(int circleNum) {
        for(Piece piece : pieces) {
            piece.turnCW(circles.get(circleNum));
        }
    }

    public void turnCCW(int circleNum) {
        for(Piece piece : pieces) {
            piece.turnCCW(circles.get(circleNum));
        }
    }
}
