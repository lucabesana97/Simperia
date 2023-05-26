package game.entities;

import game.Coordinates;
import game.Movable;
import game.Sprites;
import gui.Frame;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class Player extends Entity implements Movable {
    public int money;
    public int experience;
    public int experienceToNextLevel;

    public Player() {

//        try {
////            URL url = getClass().getResource("/sprites/enemies/Octopus-mad-down-1.png");
////            sprites = new Sprites(ImageIO.read(url));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        coordinates = new Coordinates(Frame.WIDTH / 2, Frame.HEIGHT / 2, 100, 100);
    }

    public void draw(Graphics graphics) {
        URL url48 = getClass().getResource("/sprites/enemies/Test-48.png");
        URL url64 = getClass().getResource("/sprites/enemies/Test-64.png");
        URL url128 = getClass().getResource("/sprites/enemies/Test-128.png");
        URL url256 = getClass().getResource("/sprites/enemies/Test-256.png");
        BufferedImage image48;
        BufferedImage image64;
        BufferedImage image128;
        BufferedImage image256;
        try {
            image48 = ImageIO.read(url48);
            image64 = ImageIO.read(url64);
            image128 = ImageIO.read(url128);
            image256 = ImageIO.read(url256);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //TODO add implementation of drawing
        graphics.drawImage(image48, 100, 100, null);
        graphics.drawImage(image64, 200, 100, null);
        graphics.drawImage(image128, 100, 300, null);
        graphics.drawImage(image256, 200, 300, null);
//        graphics.drawImage(sprites.current, (int) coordinates.center_x, (int) coordinates.center_y, null);


    }

    @Override
    public void move(double diffSeconds) {
        coordinates.moveX(20 * diffSeconds);
    }

}
