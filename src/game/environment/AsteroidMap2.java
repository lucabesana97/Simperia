package game.environment;

import game.Coordinates;
import game.GameObject;
import game.entities.*;
import game.inventory.HealthElixir;
import game.inventory.ItemStack;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AsteroidMap2 extends GameMap {
    private void load() {
        String str;

        str = "/sprites/maps/gameMap02.png";
        try {
            mapImage = ImageIO.read(getClass().getResourceAsStream(str));
        } catch (Exception e) {
            System.out.println("Couldn't load map image: " + "\tReason: " + e.getCause());
        }

        str = "/grids/asteroidGrid.csv";
        grid = new int[128][96];

        try {
            InputStream is = getClass().getResourceAsStream(str);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            for(int i = 0; i < 96; i++) {
                String line = br.readLine();
                String numbers[] = line.split(",");
                for(int j = 0; j < 128; j++) {
                    grid[j][i] = Integer.parseInt(numbers[j]);
                }
            }
            br.close();
        }catch(Exception e){
            System.out.println("Couldn't load map grid: " + "\tReason: " + e.getCause());
        }

    }

    public AsteroidMap2() {
        load();
        beginnerNPC = new NPC(new Coordinates(1349, 187, 37, 64));
        //enemies.add(new Octopus(800,700));
//        warps.add(new Warp(new Coordinates(1000,500,32,32), new Coordinates(1400,600,32,32), player));
        mapWarps.add(new Warp(CaveID, new Coordinates(500, 1090, 32, 32), new Coordinates(200, 3150, 32, 32), player));
    }
}
