package game.inventory;

import java.awt.image.BufferedImage;

public abstract class InventoryItem {
    public String name;
    public String description;
    public BufferedImage sprite;

    public InventoryItem(String name, String description) {}

    public abstract void use();

    public abstract void draw();




}
