package game.environment;

import game.Coordinates;
import game.entities.Octopus;
import game.entities.Warp;
import game.inventory.HealthElixir;

import javax.imageio.ImageIO;

public class CaveMap extends GameMap{
    private void load() {
        String str;

        str = "/sprites/maps/gameMap03.png";
        try {
            mapImage = ImageIO.read(getClass().getResourceAsStream(str));
        } catch(Exception e) {System.out.println("Couldn't load map image: " + "\tReason: " + e.getCause());}
    }

    public CaveMap(){
        load();
        enemies.add(new Octopus(1100,1000));
        enemies.add(new Octopus(1200,800));
        enemies.add(new Octopus(1300,900));
        enemies.add(new Octopus(1400,1100));
        enemies.add(new Octopus(1500,1200));
        enemies.add(new Octopus(1800,1700));
        items.add(new HealthElixir(false, 1, new Coordinates(1000, 1000, 32, 32)));
        warps.add(new Warp(new Coordinates(1000,500,32,32), new Coordinates(1400,600,32,32), player));
        warps.add(new Warp(new Coordinates(450,700,32,32), new Coordinates(800,1100,32,32), player));
    }
}
