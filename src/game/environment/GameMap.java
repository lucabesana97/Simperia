package game.environment;

import game.entities.Entity;
import game.entities.Player;
import gui.GameFrame;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GameMap {
    public BufferedImage mapImage;
    public static int[][] mapCollision;
    public Player player;

    public GameMap(){
        load();
    }

    public void init(Player player){
        this.player = player;
    }
    private void load() {
        String str;

        str = "/sprites/maps/gameMap02.png";
        try {
            mapImage = ImageIO.read(getClass().getResourceAsStream(str));
        } catch(Exception e) {System.out.println("Couldn't load map image: " + "\tReason: " + e.getCause());}
    }

    public void draw(Graphics g){

        int x = (int) player.coordinates.topLeftCorner_x;
        int y = (int) player.coordinates.topLeftCorner_y;

        int regionWidth = GameFrame.WIDTH;
        int regionHeight = GameFrame.HEIGHT;

        int startX = Math.max(x - regionWidth / 2, 0);
        int startY = Math.max(y - regionHeight / 2, 0);
        int endX = Math.min(x + regionWidth / 2, mapImage.getWidth());
        int endY = Math.min(y + regionHeight / 2, mapImage.getHeight());

        //g.drawImage(map, 0, 0, null); // Draw the entire map
        g.drawImage(mapImage.getSubimage(startX, startY, endX - startX, endY - startY), 0, 0, null);
    }
}
