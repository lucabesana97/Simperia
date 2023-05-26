package game;

public class Coordinates {
    //TODO edit the class to adjust it to our needs
    /**
     * Right now it has points that may be necessary or not. We have to include de
     * size and convert it into a rectangular calculation since our colliders
     * will be rectangular.
     */


    //coordinates with respect to the whole world (not the panel)
    public double topLeftCorner_x;
    public double topLeftCorner_y;
    public double bottomRightCorner_x;
    public double bottomRightCorner_y;
    public double center_x;
    public double center_y;

    private final double radius_X;
    private final double radius_Y;

    public Coordinates(double x, double y, double objectSize_x, double objectSize_y) {
        topLeftCorner_x = x;
        topLeftCorner_y = y;

        radius_X = objectSize_x / 2;
        radius_Y = objectSize_y / 2;

        adjustCoordinates();
    }

    private void adjustCoordinates(){
        bottomRightCorner_x = topLeftCorner_x + radius_X * 2;
        bottomRightCorner_y = topLeftCorner_y + radius_Y * 2;

        center_x = topLeftCorner_x + radius_X;
        center_y = topLeftCorner_y + radius_Y;
    }

    public void moveX(double amount) {
        topLeftCorner_x += amount;
        adjustCoordinates();
    }

    public void moveY(double amount) {
        topLeftCorner_y += amount;
        adjustCoordinates();
    }
}
