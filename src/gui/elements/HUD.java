package gui.elements;

import game.Coordinates;
import game.GameObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

public class HUD extends GameObject {
    public BufferedImage image;
    URL hudImage;

    public HUD(Coordinates coordinates, String path) {
        super(coordinates);
        hudImage = getClass().getResource(path);
        try {
            assert hudImage != null;
            this.image = ImageIO.read(hudImage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void draw(Graphics graphics) {
        //graphics.drawImage(image, (int) coordinates.screenX, (int) coordinates.screenY, null);
        graphics.drawImage(image, (int) coordinates.topLeftCorner_x, (int) coordinates.topLeftCorner_y, (int) coordinates.size_X, (int) coordinates.size_Y, null);
    }
}
