package game.inventory;

public class Inventory {
    public static int SLOTS = 12;
    public InventoryStack[] stacks = new InventoryStack[SLOTS];

    public Inventory() {}

    public void consumeItem() {}
    public void removeItem() {}
    public void addItem() {}
}
