package game.environment;

import game.Coordinates;
import game.GameObject;
import game.entities.*;
import game.inventory.Item;
import game.inventory.ItemStack;
import gui.GameFrame;
import main.Gameplay;
import objState.MovingState;

import javax.imageio.ImageIO;
import java.awt.*;
import java.util.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class GameMap {
    protected static final int asteroidID = 0;
    protected static final int CaveID = 1;
    protected static final int asteroid2ID = 2;
    public BufferedImage mapImage;
    public static int[][] mapCollision;
    public Player player;
    public int[][] grid;
    public java.util.List<Enemy> enemies = new ArrayList<>();
    public List<Warp> warps = new ArrayList<>();
    public List<Warp> mapWarps = new ArrayList<>();
    public NPC beginnerNPC;
    public List<ItemStack> stacksOnWorld = new ArrayList<>();
    public List<GameObject> objects = new ArrayList<>();
    public List<GameObject> rocks = new ArrayList<>();

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

    public boolean mapCollision(Entity entity){
        double checkX = entity.coordinates.centerX - 24;
        double checkY = entity.coordinates.centerY - 24 + entity.coordinates.size_Y / 4;
        if(checkY < 0 || checkY > this.grid[0].length * 16 || checkX < 0 || checkX > this.grid.length * 16){
            return true;
        }
        if(grid[(int)((checkX - 12) / 16)][(int)((checkY - 12)  / 16)] == 1 ||
                grid[(int)((checkX) / 16)][(int)((checkY - 12) / 16)] == 1 ||
                grid[(int)((checkX + 12) / 16)][(int)((checkY - 12) / 16)] == 1 ||
                grid[(int)((checkX - 12) / 16)][(int)((checkY) / 16)] == 1 ||
                grid[(int)((checkX + 12) / 16)][(int)((checkY) / 16)] == 1 ||
                grid[(int)((checkX - 12) / 16)][(int)((checkY + 12) / 16)] == 1 ||
                grid[(int)((checkX) / 16)][(int)((checkY + 12) / 16)] == 1 ||
                grid[(int)((checkX + 12) / 16)][(int)((checkY + 12) / 16)] == 1
        ){
            return true;
        }
        return false;
    }


    public List<int[]> findDirection(Entity entity){
        int rows = grid.length;
        int cols = grid[0].length;

        int entityX = (int) (entity.coordinates.centerX / 16);
        int entityY = (int) (entity.coordinates.centerY / 16);
        int playerX = (int) (Gameplay.player.coordinates.centerX / 16);
        int playerY = (int) (Gameplay.player.coordinates.centerY / 16);


        // Perform BFS to find the shortest path
        int[][] directions = {
                {-1, 0},    // Top
                {1, 0},     // Bottom
                {0, -1},    // Left
                {0, 1},     // Right
                {-1, -1},   // Top Left
                {-1, 1},    // Top Right
                {1, -1},    // Bottom Left
                {1, 1}      // Bottom Right
        };

        Queue<int[]> queue = new LinkedList<>();
        boolean[][] visited = new boolean[rows][cols];
        int[][] distance = new int[rows][cols];
        int[][] prevX = new int[rows][cols];
        int[][] prevY = new int[rows][cols];

        queue.add(new int[]{entityX, entityY});
        visited[entityX][entityY] = true;

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int x = current[0];
            int y = current[1];
//            System.out.println(x + " " + y);
            if (x == playerX && y == playerY) {
                // Player found, determine direction from the entity
                return reconstructDirectionChanges(x, y, prevX, prevY);
            }

            for (int[] dir : directions) {
                int newX = x + dir[0];
                int newY = y + dir[1];

                if (newX >= 0 && newX < rows && newY >= 0 && newY < cols &&
                        !visited[newX][newY] && grid[newX][newY] == -1 && grid[newX + 2][newY] == -1  && grid[newX][newY - 2] == -1  && grid[newX][newY + 2] == -1  && grid[newX - 2][newY] == -1 &&  grid[newX - 1][newY] == -1 && grid[newX + 1][newY] == -1 && grid[newX][newY + 1] == -1 && grid[newX][newY - 1] == -1 && grid[newX + 1][newY - 1] == -1 && grid[newX + 1][newY + 1] == -1 && grid[newX - 1][newY - 1] == -1 && grid[newX - 1][newY + 1] == -1) {
                    queue.add(new int[]{newX, newY});
                    visited[newX][newY] = true;
                    distance[newX][newY] = distance[x][y] + 1;
                    prevX[newX][newY] = x;
                    prevY[newX][newY] = y;
                }
            }
        }

        // No valid path found
        return null;
    }

    List<int[]> reconstructDirectionChanges(int x, int y, int[][] prevX, int[][] prevY) {
        List<int[]> path = new ArrayList<>();
        int prevXCoord = prevX[x][y];
        int prevYCoord = prevY[x][y];
        int diffX = 0;
        int diffY = 0;

        while (prevXCoord != 0 || prevYCoord != 0) {
            if(diffX != x - prevXCoord || diffY != y - prevYCoord){
                path.add(new int[]{x, y});
                diffX = x - prevXCoord;
                diffY = y - prevYCoord;
            }
            x = prevXCoord;
            y = prevYCoord;
            prevXCoord = prevX[x][y];
            prevYCoord = prevY[x][y];
        }
        Collections.reverse(path);
        return path;
    }

}
