package game;

public class Coordinates {
    public double topLeftCorner_x;
    public double topLeftCorner_y;
    public double bottomRightCorner_x;
    public double bottomRightCorner_y;

    public final double size_X;
    public final double size_Y;

    public Coordinates(double x, double y, double objectSize_x, double objectSize_y) {
        topLeftCorner_x = x;
        topLeftCorner_y = y;

        size_X = objectSize_x;
        size_Y = objectSize_y;

        adjustCoordinates();
    }

    private void adjustCoordinates(){
        bottomRightCorner_x = topLeftCorner_x + size_X;
        bottomRightCorner_y = topLeftCorner_y + size_Y;
    }

    public void moveX(double amount) {
        topLeftCorner_x += amount;
        adjustCoordinates();
    }

    public void moveY(double amount) {
        topLeftCorner_y += amount;
        adjustCoordinates();
    }

    public boolean intersects(Coordinates other) {
        return this.topLeftCorner_x < other.bottomRightCorner_x &&
                this.bottomRightCorner_x > other.topLeftCorner_x &&
                this.topLeftCorner_y < other.bottomRightCorner_y &&
                this.bottomRightCorner_y > other.topLeftCorner_y;
    }
}
