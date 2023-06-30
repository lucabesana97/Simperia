package game.environment;

import game.Coordinates;
import game.entities.*;
import game.inventory.HealthElixir;
import game.inventory.ItemStack;
import main.Gameplay;

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

        if(Gameplay.asteroidEnemies[0])
        enemies.add(new Octopus(1413, 1125,0));
        if(Gameplay.asteroidEnemies[1])
        enemies.add(new Maurice(1413, 1199,1));
        if(Gameplay.asteroidEnemies[2])
        enemies.add(new Octopus(1297, 1365,2));
        if(Gameplay.asteroidEnemies[3])
        enemies.add(new Maurice(1186, 1130,3));
        if(Gameplay.asteroidEnemies[4])
        enemies.add(new Octopus(1028, 1251,4));
        if(Gameplay.asteroidEnemies[5])
        enemies.add(new Maurice(904, 1120,5));
        if(Gameplay.asteroidEnemies[6])
        enemies.add(new Octopus(691, 1120,6));
        if(Gameplay.asteroidEnemies[7])
        enemies.add(new Maurice(675, 990,7));
        if(Gameplay.asteroidEnemies[8])
        enemies.add(new Octopus(506, 1193,8));
        if(Gameplay.asteroidEnemies[9])
        enemies.add(new Maurice(482, 1259,9));
        if(Gameplay.asteroidEnemies[10])
        enemies.add(new Octopus(625, 1082,10));
        if(Gameplay.asteroidEnemies[11])
        enemies.add(new Maurice(696, 1362,11));
        if(Gameplay.asteroidEnemies[12])
        enemies.add(new Octopus(678, 1225,12));
        if(Gameplay.asteroidEnemies[13])
        enemies.add(new Octopus(901, 93,13));
        if(Gameplay.asteroidEnemies[14])
        enemies.add(new Maurice(1213, 238,14));
        if(Gameplay.asteroidEnemies[15])
        enemies.add(new Octopus(1459, 406,15));
        if(Gameplay.asteroidEnemies[16])
        enemies.add(new Maurice(1480, 570,16));
        if(Gameplay.asteroidEnemies[17])
        enemies.add(new Octopus(1758, 570,17));
        if(Gameplay.asteroidEnemies[18])
        enemies.add(new Octopus(750,700,18));

        beginnerNPC = new NPC(new Coordinates(1349, 187, 37, 64));
//        warps.add(new Warp(new Coordinates(1000,500,32,32), new Coordinates(1400,600,32,32), player));
        mapWarps.add(new Warp(CaveID, new Coordinates(500, 1090, 32, 32), new Coordinates(200, 3150, 32, 32), player));

    }
}
