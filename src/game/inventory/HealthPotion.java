package game.inventory;

public class HealthPotion extends InventoryItem{

    int healthRestored;

    public HealthPotion(String name, String description) {
        super(name, description);
    }

    @Override
    public void use() {
    }

    @Override
    public void draw() {

    }
}
