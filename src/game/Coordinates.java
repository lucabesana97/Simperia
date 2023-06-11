package game;

import gui.GameFrame;
import main.Gameplay;

public class Coordinates {
    public double topLeftCorner_x;
    public double topLeftCorner_y;
    public double bottomRightCorner_x;
    public double bottomRightCorner_y;
    public double centerX,centerY;
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
        centerX = topLeftCorner_x + size_X/2;
        centerY = topLeftCorner_y + size_Y/2;
        //adjustCoordinates();
    }

    public void adjustCoordinates(){
        bottomRightCorner_x = topLeftCorner_x + size_X;
        bottomRightCorner_y = topLeftCorner_y + size_Y;
        centerX = topLeftCorner_x + size_X/2;
        centerY = topLeftCorner_y + size_Y/2;
        if(Gameplay.player.closeToDownWall()){
            screenY = topLeftCorner_y + GameFrame.HEIGHT - Gameplay.map.mapImage.getHeight();
        } else if(Gameplay.player.closeToUpWall()){
            screenY = topLeftCorner_y;
        } else {
            screenY = (double) GameFrame.HEIGHT /2 - (Gameplay.player.coordinates.topLeftCorner_y - topLeftCorner_y);
        }
        if(Gameplay.player.closeToRightWall()){
            screenX = topLeftCorner_x + GameFrame.WIDTH - Gameplay.map.mapImage.getWidth();
        } else if(Gameplay.player.closeToLeftWall()){
            screenX = topLeftCorner_x;
        } else {
            screenX = (double) GameFrame.WIDTH /2 - (Gameplay.player.coordinates.topLeftCorner_x - topLeftCorner_x);
        }
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
        adjustCoordinates();
        if(screenX >= -size_X &&
                screenX <= GameFrame.WIDTH + size_X &&
                screenY >= -size_Y &&
                screenY <= GameFrame.HEIGHT + size_Y){
            return true;
        }
        return false;
    }
}
