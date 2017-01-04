package circle_simulator;

import java.util.*;
import javafx.scene.paint.Color;
import javafx.scene.image.PixelWriter;

public class Border extends PixelStorer{
    private Color color = Color.BLACK;

    public void drawBorder(PixelWriter writer) {
        for(int[] line : pixels) {
            for(int y=line[1]; y<line[2]; y++) {
                writer.setColor(line[0], y, color);
            }
        }
    }

    public Border() {}
}
