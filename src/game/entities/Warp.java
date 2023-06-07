package game.entities;

import game.Coordinates;
import gui.GameFrame;
import main.Gameplay;

import java.awt.*;

public class Warp extends Entity{
    public Coordinates entrance;
    public Coordinates exit;
    private Player player;
    public double screenXIn, screenYIn, screenXOut, screenYOut;
    public int frameWidth = GameFrame.WIDTH/2;
    public int frameHeight = GameFrame.HEIGHT/2;

    public Warp(Coordinates coordinates, Coordinates exit, Player player){
        this.coordinates = coordinates;
        this.exit = exit;
        this.player = player;
    }

    @Override
    public void draw(Graphics g) {
        screenXIn = frameWidth - (player.coordinates.topLeftCorner_x - coordinates.topLeftCorner_x);
        screenYIn = frameHeight - (player.coordinates.topLeftCorner_y - coordinates.topLeftCorner_y);
        screenXOut = frameWidth - (player.coordinates.topLeftCorner_x - exit.topLeftCorner_x);
        screenYOut = frameHeight - (player.coordinates.topLeftCorner_y - exit.topLeftCorner_y);
        if(screenXIn >= -coordinates.size_X &&
                screenXIn <= GameFrame.WIDTH + coordinates.size_X &&
                screenYIn >= -coordinates.size_Y &&
                screenYIn <= GameFrame.HEIGHT + coordinates.size_Y
        ) {
            g.setColor(Color.red);
            g.drawOval((int) screenXIn, (int) screenYIn, (int) coordinates.size_X, (int) coordinates.size_Y);
        }
        if(screenXOut >= -coordinates.size_X &&
                screenXOut <= GameFrame.WIDTH + coordinates.size_X &&
                screenYOut >= -coordinates.size_Y &&
                screenYOut <= GameFrame.HEIGHT + coordinates.size_Y
        ) {
            g.setColor(Color.blue);
            g.drawOval((int) screenXOut, (int) screenYOut, (int) exit.size_X, (int) exit.size_Y);
        }
    }

}
