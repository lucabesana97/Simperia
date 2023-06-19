package game.environment;

import game.Coordinates;
import game.GameObject;
import game.entities.*;
import game.inventory.HealthElixir;
import game.inventory.ItemStack;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class AsteroidMap extends GameMap {
    private void load() {
        String str;

        str = "/sprites/maps/gameMap02.png";
        try {
            mapImage = ImageIO.read(getClass().getResourceAsStream(str));
        } catch (Exception e) {
            System.out.println("Couldn't load map image: " + "\tReason: " + e.getCause());
        }



    }

    public AsteroidMap() {
        load();
        enemies.add(new Octopus(1413, 1125));
        enemies.add(new MeleeEnemy(1413, 1199));
        enemies.add(new Octopus(1297, 1365));
        enemies.add(new MeleeEnemy(1186, 1130));
        enemies.add(new Octopus(1028, 1251));
        enemies.add(new MeleeEnemy(904, 1120));
        enemies.add(new Octopus(691, 1120));
        enemies.add(new MeleeEnemy(503, 1025));
        enemies.add(new Octopus(506, 1193));
        enemies.add(new MeleeEnemy(398, 1118));
        enemies.add(new Octopus(341, 1277));
        enemies.add(new MeleeEnemy(234, 1171));
        enemies.add(new Octopus(253, 1047));
        enemies.add(new Octopus(901, 93));
        enemies.add(new MeleeEnemy(1213, 238));
        enemies.add(new Octopus(1459, 406));
        enemies.add(new MeleeEnemy(1575, 570));
        enemies.add(new Octopus(1758, 570));
        beginnerNPC = new NPC(new Coordinates(472, 150, 37, 64));
        //enemies.add(new Octopus(800,700));
//        warps.add(new Warp(new Coordinates(1000,500,32,32), new Coordinates(1400,600,32,32), player));
        mapWarps.add(new Warp(CaveID, new Coordinates(245, 1262, 32, 32), new Coordinates(200, 3150, 32, 32), player));

        stacksOnWorld.add(new ItemStack(new HealthElixir(new Coordinates(1000, 1000, 32, 32)), 200, false));
    }
}
