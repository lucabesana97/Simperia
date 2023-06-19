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
        enemies.add(new Octopus(150, 950));
        enemies.add(new Octopus(130, 1050));
        enemies.add(new Octopus(200, 1150));
        enemies.add(new Octopus(320, 2790));
        enemies.add(new Octopus(830, 2860));
        enemies.add(new Octopus(780, 3020));
        enemies.add(new Octopus(730, 3060));
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
        enemies.add(new Octopus(480, 1540));
        enemies.add(new Octopus(730, 1540));
        enemies.add(new Octopus(900, 1390));
        enemies.add(new Octopus(920, 1200));
        enemies.add(new Octopus(820, 1160));
        enemies.add(new Octopus(860, 1120));
        enemies.add(new Octopus(790, 870));
        enemies.add(new Octopus(820, 840));
        enemies.add(new Octopus(420, 840));

//        items.add(new HealthElixir(new Coordinates(1000, 1000, 32, 32)));
        mapWarps.add(new Warp(asteroid2ID, new Coordinates(240,220,32,32), new Coordinates(202,342,32,32), player));

    }
}
