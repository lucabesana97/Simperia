package game.environment;

import game.Coordinates;
import game.entities.*;
import game.inventory.HealthElixir;
import game.inventory.ItemStack;

import javax.imageio.ImageIO;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AsteroidMap extends GameMap {
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

    public AsteroidMap() {
        load();

        enemies.add(new Octopus(1413, 1125));
        enemies.add(new Maurice(1413, 1199));
        enemies.add(new Octopus(1297, 1365));
        enemies.add(new Maurice(1186, 1130));
        enemies.add(new Octopus(1028, 1251));
        enemies.add(new Maurice(904, 1120));
        enemies.add(new Octopus(691, 1120));
        enemies.add(new Maurice(675, 990));
        enemies.add(new Octopus(506, 1193));
        enemies.add(new Maurice(482, 1259));
        enemies.add(new Octopus(625, 1082));
        enemies.add(new Maurice(696, 1362));
        enemies.add(new Octopus(678, 1225));
        enemies.add(new Octopus(901, 93));
        enemies.add(new Maurice(1213, 238));
        enemies.add(new Octopus(1459, 406));
        enemies.add(new Maurice(1480, 570));
        enemies.add(new Octopus(1758, 570));
        enemies.add(new Octopus(750,700));

        beginnerNPC = new NPC(new Coordinates(1349, 187, 37, 64));
//        warps.add(new Warp(new Coordinates(1000,500,32,32), new Coordinates(1400,600,32,32), player));
        mapWarps.add(new Warp(CaveID, new Coordinates(500, 1090, 32, 32), new Coordinates(200, 3150, 32, 32), player));

        stacksOnWorld.add(new ItemStack(new HealthElixir(new Coordinates(1000, 1000, 32, 32)), 5, false));
    }
}
