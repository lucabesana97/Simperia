package helperFunctions;
import java.awt.geom.AffineTransform;
import java.lang.Math;

import game.entities.Player;
import game.Coordinates;
import game.entities.Entity;
import game.environment.GameMap;
import gui.GameFrame;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Utility {
    public static BufferedImage resize(BufferedImage img) {
        int newW = 48;
        int newH = 48;
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }

    public static int distanceBetweenCoordinates(Coordinates a, Coordinates b) {
        if (a.intersects(b)){
            return 0;
        }
        double topLeft = Math.sqrt(Math.pow(a.topLeftCorner_x - b.topLeftCorner_x, 2) + Math.pow(a.topLeftCorner_y - b.topLeftCorner_y, 2));
        double topRight = Math.sqrt(Math.pow(a.topLeftCorner_x - b.bottomRightCorner_x, 2) + Math.pow(a.topLeftCorner_y - b.bottomRightCorner_y, 2));
        double bottomLeft = Math.sqrt(Math.pow(a.bottomRightCorner_x - b.topLeftCorner_x, 2) + Math.pow(a.bottomRightCorner_y - b.topLeftCorner_y, 2));
        double bottomRight = Math.sqrt(Math.pow(a.bottomRightCorner_x - b.bottomRightCorner_x, 2) + Math.pow(a.bottomRightCorner_y - b.bottomRightCorner_y, 2));

        double min = Math.min(Math.min(topLeft, topRight), Math.min(bottomLeft, bottomRight));
        return (int) min;
    }

    // Used to flip the image horizontally
    private static BufferedImage createTransformed(
            BufferedImage image, AffineTransform at)
    {
        BufferedImage newImage = new BufferedImage(
                image.getWidth(), image.getHeight(),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = newImage.createGraphics();
        g.transform(at);
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return newImage;
    }

    // Flip the image horizontally
    public static BufferedImage createFlipped(BufferedImage image)
    {
        AffineTransform at = new AffineTransform();
        at.concatenate(AffineTransform.getScaleInstance(-1, 1));
        at.concatenate(AffineTransform.getTranslateInstance(-image.getWidth(), 0));
        return createTransformed(image, at);
    }

    public static BufferedImage rotateImageByDegrees(BufferedImage img, double angle) {
        double rads = Math.toRadians(angle);
        double sin = Math.abs(Math.sin(rads)), cos = Math.abs(Math.cos(rads));
        int w = img.getWidth();
        int h = img.getHeight();
        int newWidth = (int) Math.floor(w * cos + h * sin);
        int newHeight = (int) Math.floor(h * cos + w * sin);

        BufferedImage rotated = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = rotated.createGraphics();
        AffineTransform at = new AffineTransform();
        at.translate((newWidth - w) / 2, (newHeight - h) / 2);

        int x = w / 2;
        int y = h / 2;

        at.rotate(rads, x, y);
        g2d.setTransform(at);
        g2d.drawImage((Image) img, 0, 0, null);
        g2d.dispose();

        return rotated;
    }

    public boolean WallCollision(Entity entity){
        int xLeft = (int)(entity.coordinates.topLeftCorner_x / GameFrame.TILE_SIZE);
        int yLeft = (int)(entity.coordinates.topLeftCorner_y / GameFrame.TILE_SIZE);
        int xRight = (int)(entity.coordinates.topLeftCorner_x / GameFrame.TILE_SIZE);
        int yRight = (int)(entity.coordinates.topLeftCorner_y / GameFrame.TILE_SIZE);


        if(GameMap.mapCollision[xLeft][yLeft] == 1 ||
                GameMap.mapCollision[xLeft][yRight] == 1 ||
                GameMap.mapCollision[xRight][yLeft] == 1 ||
                GameMap.mapCollision[xRight][yRight] == 1){
            return true;
        }
        return false;
    }

    public static int getAimAngle(Player player){
        int angle = 90;
        switch (player.previousStateX) {
            case RIGHT -> {
                switch (player.previousStateY) {
                    case UP:
                        angle = 315;
                        break;
                    case DOWN:
                        angle = 45;
                        break;
                    case STILL:
                        angle = 0;
                        break;
                }
            }
            case LEFT -> {
                switch (player.previousStateY) {
                    case UP:
                        angle = 225;
                        break;
                    case DOWN:
                        angle = 135;
                        break;
                    case STILL:
                        angle = 180;
                        break;
                }
            }
            case STILL -> {
                switch (player.previousStateY) {
                    case UP:
                        angle = 270;
                        break;
                    case DOWN:
                    case STILL:
                        break;
                }
            }
        }
    return angle;
    }
}
