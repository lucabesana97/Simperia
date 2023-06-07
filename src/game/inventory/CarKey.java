package game.inventory;

import game.Coordinates;

import java.awt.*;

public class CarKey extends Item {

    public CarKey(String name, String description, boolean inInventory, Coordinates coordinates, int stackAmount) {
        super(name, description, inInventory, coordinates, stackAmount);
    }

    @Override
    public void use() {
    }

    @Override
    public void draw(Graphics graphics) {

    }

}
