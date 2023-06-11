package game.environment;

import game.Coordinates;
import game.entities.Enemy;
import game.entities.Octopus;
import game.entities.Warp;
import game.inventory.HealthElixir;
import game.inventory.ItemStack;

import javax.imageio.ImageIO;

public class AsteroidMap extends GameMap{
    private void load() {
        String str;

        str = "/sprites/maps/gameMap02.png";
        try {
            mapImage = ImageIO.read(getClass().getResourceAsStream(str));
        } catch(Exception e) {System.out.println("Couldn't load map image: " + "\tReason: " + e.getCause());}
    }

    public AsteroidMap(){
        load();
        enemies.add(new Octopus(1000,1000));
        //enemies.add(new Octopus(800,700));
        warps.add(new Warp(new Coordinates(1000,500,32,32), new Coordinates(1400,600,32,32), player));
        mapWarps.add(new Warp(new CaveMap(), new Coordinates(600,600,32,32), new Coordinates(1400,600,32,32), player));

        stacksOnWorld.add(new ItemStack(new HealthElixir(new Coordinates(1000, 1000, 32, 32)),200, false));
    }
}
