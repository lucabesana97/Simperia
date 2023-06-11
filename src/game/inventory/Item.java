package game.inventory;

import game.Coordinates;
import game.GameObject;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Item extends GameObject {

    public String name;
    public String description;
    public BufferedImage sprite;



    public Item(String name, String description, Coordinates coordinates) {
        this.name = name;
        this.description = description;
        this.coordinates = coordinates;
    }

    public abstract void use();

    public void draw(Graphics graphics){
    }


}
