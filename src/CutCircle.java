import java.util.*;

public class CutCircle {
    private int centerX;
    public int getCenterX() {return centerX;}
    public void setCenterX(int x) {centerX = x;}
    private int centerY;
    public int getCenterY() {return centerY;}
    public void setCenterY(int y) {centerY = y;}
    private int radius;
    public int getRadius() {return radius;}
    public void setRadius(int r) {radius = r;}
    private int borderWidth;
    public int getBorderWidth() {return borderWidth;}
    public void setBorderWidth(int bw) {borderWidth = bw;}
    private boolean isTurningCircle;
    public boolean isTurningCircle() {return isTurningCircle;}
    public void setIsTurningCircle(boolean tc) {isTurningCircle = tc;}

    public CutCircle(int x, int y, int r, int bw, boolean tc) {
        centerX = x;
        centerY = y;
        radius = r;
        borderWidth = bw;
        isTurningCircle = tc;
    }

    public int circlePosition(int x, int y) {
        //For a given pixel, returns 0 if it is in the circle, 1 if it is in the border, and 2 if it is outside the circle
        int d2 = PMath.squareDistance(x, y, centerX, centerY);
        if(d2 <= radius*radius) return 0;
        else if(d2 <= (radius+borderWidth)*(radius+borderWidth)) return 1;
        else return 2;
    }

    private HashMap<CutCircle,CutCircle> nextClockwise = new HashMap<CutCircle,CutCircle>();
    public HashMap<CutCircle,CutCircle> getNextClockwise() {return nextClockwise;}
    public void setNextClockwise(HashMap<CutCircle,CutCircle> nc) {nextClockwise = nc;}
    private HashMap<CutCircle,CutCircle> nextCounterClockwise = new HashMap<CutCircle,CutCircle>();
    public HashMap<CutCircle,CutCircle> getNextCounterClockwise() {return nextCounterClockwise;}

    public void addCycle(ArrayList<CutCircle> cycle) {
        //Sets the nextClockwise and nextCounterClockwise functions given a piece cycle going clockwise
        for(int i=1; i<cycle.size(); i++) {
            nextCounterClockwise.put(cycle.get(i-1), cycle.get(i));
            nextClockwise.put(cycle.get(i), cycle.get(i-1));
        }
        nextCounterClockwise.put(cycle.get(cycle.size()-1),cycle.get(0));
        nextClockwise.put(cycle.get(0),cycle.get(cycle.size()-1));
    }

    public void addCycles(List<ArrayList<CutCircle>> cycles) {
        for(ArrayList<CutCircle> cycle : cycles){
            addCycle(cycle);
        }
    }
}
