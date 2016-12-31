import java.util.*;

public class PixelStorer {
    protected ArrayList<int[]> pixels = new ArrayList<int[]>();
    //Keeps track of which pixels need to be colored if a piece is in this position
    //Each entry keeps track of a vertical column in the form (x,first y,last y + 1)
    public ArrayList<int[]> getPixels() {return pixels;}
}
