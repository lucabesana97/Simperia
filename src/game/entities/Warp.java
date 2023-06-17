package game.entities;

import game.Coordinates;
import game.GameObject;
import game.environment.GameMap;
import gui.GameFrame;
import main.Gameplay;

import java.awt.*;

public class Warp extends GameObject {
    public Coordinates exit;
    private Player player;
    public double screenXIn, screenYIn, screenXOut, screenYOut;
    public int frameWidth = GameFrame.WIDTH/2;
    public int frameHeight = GameFrame.HEIGHT/2;
    public int mapID;
    public Warp(Coordinates coordinates, Coordinates exit, Player player){
        super(coordinates);
        this.exit = exit;
        this.player = player;
    }

    public Warp(int mapID, Coordinates coordinates, Coordinates exit, Player player){
        super(coordinates);
        this.mapID = mapID;
        this.exit = exit;
        this.player = player;
    }

    @Override
    public void draw(Graphics g) {
        if(coordinates.inScreen()) {
            g.setColor(Color.red);
            g.drawOval((int) coordinates.screenX, (int) coordinates.screenY, (int) coordinates.size_X, (int) coordinates.size_Y);
        }
        if(exit.inScreen()) {
            g.setColor(Color.blue);
            g.drawOval((int) exit.screenX, (int) exit.screenY, (int) exit.size_X, (int) exit.size_Y);
        }
    }

}
