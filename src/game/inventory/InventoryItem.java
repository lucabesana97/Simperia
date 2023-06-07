package game.inventory;

import java.awt.image.BufferedImage;

public abstract class InventoryItem {

    public String name;
    public String description;
    public BufferedImage sprite; 
    public int maxStack = 16;

    public InventoryItem(String name, String description) {}

    public abstract void use();

    public abstract void draw(double x, double y);

}
