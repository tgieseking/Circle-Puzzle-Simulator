import java.util.*;

public class CutCircle {
    private double centerX;
    public double getCenterX() {return centerX;}
    public void setCenterX(double x) {centerX = x;}
    private double centerY;
    public double getCenterY() {return centerY;}
    public void setCenterY(double y) {centerY = y;}
    private double radius;
    public double getRadius() {return radius;}
    public void setRadius(double r) {radius = r;}
    private double borderWidth;
    public double getBorderWidth() {return borderWidth;}
    public void setBorderWidth(double bw) {borderWidth = bw;}
    private boolean isTurningCircle;
    public boolean isTurningCircle() {return isTurningCircle;}
    public void setIsTurningCircle(boolean tc) {isTurningCircle = tc;}

    public CutCircle(double x, double y, double r, double bw, boolean tc) {
        centerX = x;
        centerY = y;
        radius = r;
        borderWidth = bw;
        isTurningCircle = tc;
    }

    public int circlePosition(double x, double y) {
        //For a given pixel, returns 0 if it is in the circle, 1 if it is in the border, and 2 if it is outside the circle
        double d2 = PMath.squareDistance(x, y, centerX, centerY);
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
            nextClockwise.put(cycle.get(i-1), cycle.get(i));
            nextCounterClockwise.put(cycle.get(i), cycle.get(i-1));
        }
        nextClockwise.put(cycle.get(cycle.size()-1),cycle.get(0));
        nextCounterClockwise.put(cycle.get(0),cycle.get(cycle.size()-1));
    }

    public void addCycles(List<ArrayList<CutCircle>> cycles) {
        for(ArrayList<CutCircle> cycle : cycles){
            addCycle(cycle);
        }
    }

    public String toString() {
        return "CutCircle:" + centerX + " " + centerY + " " + radius + " " + borderWidth + " " + isTurningCircle;
    }
}
