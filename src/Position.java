import java.util.*;

public class Position extends PixelStorer{

    private HashMap<CutCircle,Position> nextClockwise = new HashMap<CutCircle,Position>();
    public HashMap<CutCircle,Position> getNextClockwise() {return nextClockwise;}
    private HashMap<CutCircle,Position> nextCounterClockwise = new HashMap<CutCircle,Position>();
    public HashMap<CutCircle,Position> getNextCounterClockwise() {return nextCounterClockwise;}

    public Set<CutCircle> getBoundingCircles() {return nextClockwise.keySet();}

    public Position() {}
}
