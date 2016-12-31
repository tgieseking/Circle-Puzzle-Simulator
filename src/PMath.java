public final class PMath {
    private PMath() {}

    public static final double EPSILON = 1;

    public static double squareDistance(double x1, double y1, double x2, double y2) {
        return (x1-x2)*(x1-x2)+(y1-y2)*(y1-y2);
    }
    public static int squareDistance(int x1, int y1, int x2, int y2) {
        return (x1-x2)*(x1-x2)+(y1-y2)*(y1-y2);
    }
    public static double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(squareDistance(x1, y1, x2, y2));
    }
    public static double distance(int x1, int y1, int x2, int y2) {
        return Math.sqrt(squareDistance((double) x1, (double) y1, (double) x2, (double) y2));
    }


    public static double rotatedX(double x, double y, double centerX, double centerY, double angle) {
        return centerX+((x-centerX)*(Math.cos(angle))-(y-centerY)*(Math.sin(angle)));
    }
    public static double rotatedY(double x, double y, double centerX, double centerY, double angle) {
        return centerY+((x-centerX)*(Math.sin(angle))+(y-centerY)*(Math.cos(angle)));
    }
    public static double fracRotatedX(double x, double y, double centerX, double centerY, int numerator, int denominator) {
        return rotatedX(x, y, centerX, centerY, 2.0*Math.PI*((double) numerator)/((double) denominator));
    }
    public static double fracRotatedY(double x, double y, double centerX, double centerY, int numerator, int denominator) {
        return rotatedY(x, y, centerX, centerY, 2.0*Math.PI*((double) numerator)/((double) denominator));
    }
}
