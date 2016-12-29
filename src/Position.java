import java.util.*;

public class Position {
    private ArrayList<int[]> pixels = new ArrayList<int[]>();
    //Keeps track of which pixels need to be colored if a piece is in this position
    //Each entry keeps track of a vertical column in the form (x,first y,last y + 1)
    public ArrayList<int[]> getPixels() {return pixels;}

    private HashMap<CutCircle,Position> nextClockwise = new HashMap<CutCircle,Position>();
    public HashMap<CutCircle,Position> getNextClockwise() {return nextClockwise;}
    private HashMap<CutCircle,Position> nextCounterClockwise = new HashMap<CutCircle,Position>();
    public HashMap<CutCircle,Position> getNextCounterClockwise() {return nextCounterClockwise;}

    public Set<CutCircle> getBoundingCircles() {return nextClockwise.keySet();}

    public Position() {}
}
