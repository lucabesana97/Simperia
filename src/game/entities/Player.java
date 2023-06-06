package game.entities;

import game.Coordinates;
import game.Movable;
import gui.GameFrame;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import static helperFunctions.utility.resize;

public class Player extends Entity implements Movable {
    public int money;
    public int experience;
    public int experienceToNextLevel;

    public Player() {
        URL url = getClass().getResource("/sprites/player/Idle-1.png");
        BufferedImage image;
        try{
            image = ImageIO.read(url);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        sprites.current = resize(image);
        this.name = "Player";
        this.coordinates = new Coordinates(GameFrame.WIDTH / 2 - 24, GameFrame.HEIGHT / 2 - 24, 48, 48);
    }

    public void draw(Graphics graphics) {
        //TODO edit so it draws different sprites depending on the direction of the movement
        int x = (int)(coordinates.topLeftCorner_x);
        int y = (int)(coordinates.topLeftCorner_y);
        graphics.drawImage(sprites.current, x, y, null);
        // draw circle around player
        graphics.setColor(Color.RED);
        graphics.drawRect(x, y, 48, 48);
        graphics.drawLine(0, GameFrame.HEIGHT/2, GameFrame.WIDTH, GameFrame.HEIGHT/2);
        graphics.drawLine(GameFrame.WIDTH/2, 0, GameFrame.WIDTH/2, GameFrame.HEIGHT);
        graphics.drawRect(0, 0, GameFrame.WIDTH-1, GameFrame.HEIGHT-1);
//        graphics.drawRect(0, 0, Frame.WIDTH, Frame.HEIGHT);

    }

    @Override
    public void move(double diffSeconds) {
        this.invincibilityTimer += diffSeconds;
        //TODO edit so player moves with keys pressed
//        coordinates.moveX(20 * diffSeconds);
    }
}
