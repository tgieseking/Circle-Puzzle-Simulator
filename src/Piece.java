import java.util.*;
import javafx.scene.paint.Color;
import javafx.scene.image.PixelWriter;


public class Piece {
    private Color color;
    public Color getColor() {return color;}
    public void setColor(Color c) {color = c;}

    // private HashSet<CutCircle> boundingCircles = new HashSet<CutCircle>();
    public Set<CutCircle> getBoundingCircles() {return position.getNextClockwise().keySet();}
    // public void setBoudingCircles(HashSet<CutCircle> bc) {boundingCircles = bc;}

    private Position position;
    public Position getPosition() {return position;}

    public Piece(Position p, Color c) {
        position = p;
        setColor(c);
    }

    public void drawPiece(PixelWriter writer) {
        for(int[] line : position.getPixels()) {
            for(int y=line[1]; y<line[2]; y++) {
                writer.setColor(line[0], y, color);
            }
        }
    }

    public void turnCW(CutCircle turningCircle) {
        if(position.getNextClockwise().containsKey(turningCircle)) {
            position = position.getNextClockwise().get(turningCircle);
        }
    }

    public void turnCCW(CutCircle turningCircle) {
        if(position.getNextCounterClockwise().containsKey(turningCircle)) {
            position = position.getNextCounterClockwise().get(turningCircle);
        }
    }
}
