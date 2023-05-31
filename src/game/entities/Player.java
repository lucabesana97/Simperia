package game.entities;

import game.Coordinates;
import game.Movable;
import gui.GameFrame;
import objState.MovingState;

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
    public MovingState xState;
    public MovingState yState;
    public int screenX = GameFrame.WIDTH / 2 - 24;
    public int screenY = GameFrame.HEIGHT / 2 - 24;
    public Player() {
        URL url = getClass().getResource("/sprites/player/Idle-1.png");
        BufferedImage image;
        try{
            image = ImageIO.read(url);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        speed = 10;
        xState = MovingState.STILL;
        yState = MovingState.STILL;
        sprites.current = resize(image);
        this.name = "Player";
        this.coordinates = new Coordinates(1100, 1100, 48, 48);
    }

    public void draw(Graphics graphics) {
        //TODO edit so it draws different sprites depending on the direction of the movement
        int x = (int)(coordinates.topLeftCorner_x);
        int y = (int)(coordinates.topLeftCorner_y);
        graphics.drawImage(sprites.current, screenX, screenY, null);
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

        double moveBy = diffSeconds * speed;

        //TODO edit so player moves with keys pressed
        if(xState != MovingState.STILL && yState != MovingState.STILL){
            moveBy *= Math.sqrt(2) / 2;
            coordinates.moveX((xState == MovingState.RIGHT) ? moveBy : -moveBy);
            coordinates.moveY((yState == MovingState.DOWN) ? moveBy : -moveBy);
        } else if (xState != MovingState.STILL){
            coordinates.moveX((xState == MovingState.RIGHT) ? moveBy : -moveBy);
        } else if (yState != MovingState.STILL){
            coordinates.moveY((yState == MovingState.DOWN) ? moveBy : -moveBy);
        }
    }
}
