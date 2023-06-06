package game.entities;

import game.Coordinates;
import game.Movable;
import gui.GameFrame;
import main.Gameplay;
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
    public int mapHeight;
    public int mapWidth;
    public Player() {
        URL url = getClass().getResource("/sprites/player/Idle-1.png");
        BufferedImage image;
        try{
            image = ImageIO.read(url);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        speed = 30;
        xState = MovingState.STILL;
        yState = MovingState.STILL;
        sprites.current = resize(image);
        this.name = "Player";
        mapHeight = Gameplay.map.mapImage.getHeight();
        mapWidth = Gameplay.map.mapImage.getWidth();
        this.coordinates = new Coordinates((int)(mapWidth/2), (int)(mapHeight/2), 48, 48);
    }

    public void draw(Graphics graphics) {
        //TODO edit so it draws different sprites depending on the direction of the movement
        int x = (int)(coordinates.topLeftCorner_x);
        int y = (int)(coordinates.topLeftCorner_y);
        graphics.drawImage(sprites.current, screenX, screenY, null);
        // draw circle around player
        graphics.setColor(Color.RED);
        graphics.drawRect(screenX, screenY, 48, 48);
        graphics.drawLine(0, GameFrame.HEIGHT/2, GameFrame.WIDTH, GameFrame.HEIGHT/2);
        graphics.drawLine(GameFrame.WIDTH/2, 0, GameFrame.WIDTH/2, GameFrame.HEIGHT);
        graphics.drawRect(0, 0, GameFrame.WIDTH-1, GameFrame.HEIGHT-1);
//        graphics.drawRect(0, 0, Frame.WIDTH, Frame.HEIGHT);

    }

    @Override
    public void move(double diffSeconds) {
        System.out.println(coordinates.topLeftCorner_x + "\t" + (int)(mapWidth - GameFrame.WIDTH/2));
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

        if (coordinates.topLeftCorner_x < (int)(GameFrame.WIDTH/2)) {
            coordinates.topLeftCorner_x = (int)(GameFrame.WIDTH/2);
        }
        if (coordinates.topLeftCorner_y < (int)(GameFrame.HEIGHT/2)) {
            coordinates.topLeftCorner_y = (int)(GameFrame.HEIGHT/2);
        }
        if (coordinates.topLeftCorner_x > (int)(mapWidth - GameFrame.WIDTH/2)) {
            coordinates.topLeftCorner_x = (int)(mapWidth - GameFrame.WIDTH/2);
        }
        if (coordinates.topLeftCorner_y > mapHeight - (int)(GameFrame.HEIGHT/2)) {
            coordinates.topLeftCorner_y = mapHeight - (int)(GameFrame.HEIGHT/2);
        }
    }

    public void teleport(Warp warp){
        coordinates.topLeftCorner_x = warp.exit.topLeftCorner_x;
        coordinates.topLeftCorner_y = warp.exit.topLeftCorner_y;
    }
}
