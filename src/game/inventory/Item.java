package game.inventory;

import game.Coordinates;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Item {

    public String name;
    public String description;
    public BufferedImage sprite;
    public int stackAmount = 1;

    // False for floating items waiting to be picked up, true for items in inventory
    public boolean inInventory;
    public static int MAX_STACK = 16;

    //
    public Coordinates coordinates;

    public Item(String name, String description, boolean inInventory, Coordinates coordinates, int stackAmount) {
        this.name = name;
        this.description = description;
        this.inInventory = inInventory;
        this.coordinates = coordinates;
        this.stackAmount = stackAmount;
    }

    public abstract void use();

    public abstract void draw(Graphics graphics);

}
