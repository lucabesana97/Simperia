package game.environment;

import game.Coordinates;
import game.GameObject;
import game.entities.*;
import game.inventory.CarKey;
import game.inventory.HealthElixir;
import game.inventory.ItemStack;
import main.Gameplay;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CaveMap extends GameMap {

    boolean isFightingBoss = false;

    BufferedImage floorSymbol;
    private void load() {
        String str;

        str = "/sprites/maps/cavernMap.png";
        try {
            mapImage = ImageIO.read(getClass().getResourceAsStream(str));
        } catch (Exception e) {
            System.out.println("Couldn't load map image: " + "\tReason: " + e.getCause());
        }

        try {
            floorSymbol = ImageIO.read(getClass().getResourceAsStream("/sprites/maps/cavern_floor_inactive.png"));

            objects.add(new GameObject(new Coordinates(300, 250, 455, 453), floorSymbol));
        } catch (Exception e) {
            System.out.println("Couldn't load floor symbol: " + "\tReason: " + e.getCause());

        }

        str = "/grids/caveGrid.csv";
        grid = new int[64][219];

        try {
            InputStream is = getClass().getResourceAsStream(str);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            for(int i = 0; i < 219; i++) {
                String line = br.readLine();
                String numbers[] = line.split(",");
                for(int j = 0; j < 64; j++) {
                    grid[j][i] = Integer.parseInt(numbers[j]);
                }
            }
            br.close();
        }catch(Exception e){
            System.out.println("Couldn't load cave map grid: " + "\tReason: " + e.getCause());
        }
    }

    public CaveMap() {
        load();

        enemies.add(new Octopus(150, 950));
        enemies.add(new Octopus(130, 1050));
        enemies.add(new Octopus(200, 1150));
        enemies.add(new Octopus(320, 2790));
        enemies.add(new Octopus(815, 2840));
        enemies.add(new Octopus(750, 2980));
        enemies.add(new Octopus(730, 3030));
        enemies.add(new Octopus(330, 2650));
        enemies.add(new Octopus(870, 2690));
        enemies.add(new Octopus(280, 2450));
        enemies.add(new Octopus(230, 2200));
        enemies.add(new Octopus(300, 2050));
        enemies.add(new Octopus(180, 1900));
        enemies.add(new Octopus(290, 1850));
        enemies.add(new Octopus(820, 2490));
        enemies.add(new Octopus(920, 2190));
        enemies.add(new Octopus(780, 1940));
        enemies.add(new Octopus(460, 1540));
        enemies.add(new Octopus(730, 1540));
        enemies.add(new Octopus(900, 1390));
        enemies.add(new Octopus(920, 1200));
        enemies.add(new Octopus(820, 1160));
        enemies.add(new Octopus(860, 1120));
        enemies.add(new Octopus(790, 870));
        enemies.add(new Octopus(820, 840));
        enemies.add(new Maurice(420, 840));

//        items.add(new HealthElixir(new Coordinates(1000, 1000, 32, 32)));
//        mapWarps.add(new Warp(asteroid2ID, new Coordinates(240,220,32,32), new Coordinates(202,342,32,32), player));
        mapWarps.add(new Warp(asteroidID, new Coordinates(190, 3100, 32, 32),new Coordinates(550, 1130, 32, 32), player));

    }

}
