package game;

import gui.GameFrame;
import main.Gameplay;

public class Coordinates {
    public double topLeftCorner_x;
    public double topLeftCorner_y;
    public double bottomRightCorner_x;
    public double bottomRightCorner_y;
    public double worldX;
    public double worldY;

    public final double size_X;
    public final double size_Y;
    public double screenX, screenY;
    public Coordinates(double x, double y, double objectSize_x, double objectSize_y) {
        topLeftCorner_x = x;
        topLeftCorner_y = y;

        size_X = objectSize_x;
        size_Y = objectSize_y;
        bottomRightCorner_x = topLeftCorner_x + size_X;
        bottomRightCorner_y = topLeftCorner_y + size_Y;
        //adjustCoordinates();
    }

    private void adjustCoordinates(){
        bottomRightCorner_x = topLeftCorner_x + size_X;
        bottomRightCorner_y = topLeftCorner_y + size_Y;
        screenX = GameFrame.WIDTH/2 - (Gameplay.player.coordinates.topLeftCorner_x - topLeftCorner_x);
        screenY = GameFrame.HEIGHT/2 - (Gameplay.player.coordinates.topLeftCorner_y - topLeftCorner_y);
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

    public boolean inScreen(){
        if(screenX >= -size_X &&
                screenX <= GameFrame.WIDTH + size_X &&
                screenY >= -size_Y &&
                screenY <= GameFrame.HEIGHT + size_Y){
            return true;
        }
        return false;
    }
}
