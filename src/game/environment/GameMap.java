package game.environment;

import game.entities.Entity;
import game.entities.Player;
import gui.GameFrame;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GameMap {
    public BufferedImage map;
    public static int[][] mapCollision;
    public Player player;

    public GameMap(Player player){
        this.player = player;
        load();
    }

    private void load() {
        String str;

        str = "/sprites/maps/gameMap02.png";
        try {
            map = ImageIO.read(getClass().getResourceAsStream(str));
        } catch(Exception e) {System.out.println("Couldn't load player image: " + "\tReason: " + e.getCause());}
    }

    public void draw(Graphics g){

        int x = (int) player.coordinates.topLeftCorner_x;
        int y = (int) player.coordinates.topLeftCorner_y;

        int regionWidth = GameFrame.WIDTH;
        int regionHeight = GameFrame.HEIGHT;

        int startX = Math.max(x - regionWidth / 2, 0);
        int startY = Math.max(y - regionHeight / 2, 0);
        int endX = Math.min(x + regionWidth / 2, map.getWidth());
        int endY = Math.min(y + regionHeight / 2, map.getHeight());

        //g.drawImage(map, 0, 0, null); // Draw the entire map
        g.drawImage(map.getSubimage(startX, startY, endX - startX, endY - startY), 0, 0, null);
    }
}
