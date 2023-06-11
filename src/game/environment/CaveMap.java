package game.environment;

import game.Coordinates;
import game.GameObject;
import game.entities.Octopus;
import game.entities.Warp;
import game.inventory.HealthElixir;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class CaveMap extends GameMap {
    private void load() {
        String str;

        str = "/sprites/maps/cavernMap.png";
        try {
            mapImage = ImageIO.read(getClass().getResourceAsStream(str));
        } catch (Exception e) {
            System.out.println("Couldn't load map image: " + "\tReason: " + e.getCause());
        }

        try {
            BufferedImage floorSymbol = ImageIO.read(getClass().getResourceAsStream("/sprites/maps/cavern_floor_inactive.png"));

            objects.add(new GameObject(new Coordinates(280, 240, 455, 453), floorSymbol));
        } catch (Exception e) {
            System.out.println("Couldn't load floor symbol: " + "\tReason: " + e.getCause());

        }
    }

    public CaveMap() {
        load();
//        enemies.add(new Octopus(100, 1000));
//        enemies.add(new Octopus(200, 800));
//        enemies.add(new Octopus(300, 900));
//        enemies.add(new Octopus(400, 1100));
//        enemies.add(new Octopus(500, 1200));
//        enemies.add(new Octopus(800, 1700));
//        items.add(new HealthElixir(new Coordinates(1000, 1000, 32, 32)));
//        mapWarps.add(new Warp(new CaveMap(), new Coordinates(600,930,32,32), new Coordinates(1400,600,32,32), player));

    }
}
