package game.environment;

import game.GameObject;
import game.entities.*;
import game.inventory.Item;
import game.inventory.ItemStack;
import gui.GameFrame;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class GameMap {
    protected static final int asteroidID = 0;
    protected static final int CaveID = 1;
    protected static final int asteroid2ID = 2;
    public BufferedImage mapImage;
    public static int[][] mapCollision;
    public Player player;
    public java.util.List<Enemy> enemies = new ArrayList<>();
    public List<Warp> warps = new ArrayList<>();
    public List<Warp> mapWarps = new ArrayList<>();
    public NPC beginnerNPC;
    public List<ItemStack> stacksOnWorld = new ArrayList<>();
    public List<GameObject> objects = new ArrayList<>();

    public GameMap() {
        load();
    }

    public void init(Player player) {
        this.player = player;
    }

    private void load() {
    }

    public void draw(Graphics g) {

        int x = (int) player.coordinates.topLeftCorner_x;
        int y = (int) player.coordinates.topLeftCorner_y;

        int regionWidth = GameFrame.WIDTH;
        int regionHeight = GameFrame.HEIGHT;

        int startX = Math.max(x - regionWidth / 2, 0);
        int startY = Math.max(y - regionHeight / 2, 0);
        int endX = Math.min(x + regionWidth / 2, mapImage.getWidth());
        int endY = Math.min(y + regionHeight / 2, mapImage.getHeight());
        if (startX == 0) {
            endX = regionWidth;
        } else if (startX >= mapImage.getWidth() - regionWidth) {
            startX = mapImage.getWidth() - regionWidth;
            endX = mapImage.getWidth();
        }
        if (startY == 0) {
            endY = regionHeight;
        } else if (startY >= mapImage.getHeight() - regionHeight) {
            startY = mapImage.getHeight() - regionHeight;
            endY = mapImage.getHeight();
        }

        //g.drawImage(map, 0, 0, null); // Draw the entire map
        g.drawImage(mapImage.getSubimage(startX, startY, endX - startX, endY - startY), 0, 0, null);
    }

    public static GameMap warpTo(int i) {
        switch (i) {
            case 0:
                return new AsteroidMap();
            case 1:
                return new CaveMap();
            case 2:
                return new AsteroidMap2();
        }
        return null;
    }
}
