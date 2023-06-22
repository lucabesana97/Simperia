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

    // Cut the image horizontally according to a percentage
    public void cutImageHorizontally(double percentage) {
        int width = image.getWidth();
        int height = image.getHeight();
        int newWidth = (int) (width * percentage);

        if (newWidth <= 0) {
            newWidth = 1;
        }

        BufferedImage newImage = new BufferedImage(newWidth, height, BufferedImage.TYPE_INT_ARGB);
        coordinates.size_X = newWidth;

        Graphics2D g = newImage.createGraphics();
        g.drawImage(image, 0, 0, newWidth, height, 0, 0, newWidth, height, null);
        g.dispose();

        image = newImage;
    }
}
