package game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class GameObject {

    public Coordinates coordinates;
    public BufferedImage image;

    public GameObject(Coordinates coordinates, BufferedImage image) {
        this.coordinates = coordinates;
        this.image = image;
    }
    public GameObject(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public void draw(Graphics graphics) {
        if (coordinates.inScreen()) {
            graphics.drawImage(image, (int) coordinates.screenX - 24, (int) coordinates.screenY - 24, null);
        }
    }

    public boolean isColliding(GameObject other) {
        return this.coordinates.intersects(other.coordinates);
    }

}
