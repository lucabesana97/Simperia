package game.inventory;

import game.Coordinates;
import main.Gameplay;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class HealthElixir extends Item {

    int healthRestored = 30;

    /**
     * Constructor for HealthElixir item.
     * @param inInventory True if in inventory, false if floating in world.
     * @param stackAmount Amount of items in stack.
     * @param coordinates Coordinates of item. If in inventory, coordinates specify position to draw in inventory panel.
     *                    If floating in world, coordinates specify position to draw in world.
     */
    public HealthElixir(boolean inInventory, int stackAmount, Coordinates coordinates) {
        super("Health Elixir", "Restores 30 health.", inInventory, coordinates, stackAmount);
        initSprite();
    }

    public void initSprite() {

        try {
            if (inInventory){
                this.sprite = ImageIO.read(Objects.requireNonNull(getClass().getResource("/sprites/inventory/elixir_128.png")));
            } else {
                this.sprite = ImageIO.read(Objects.requireNonNull(getClass().getResource("/sprites/inventory/elixir_16.png")));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void use() {
        Gameplay.player.heal(healthRestored);
    }

    @Override
    public void draw(Graphics graphics) {
        graphics.drawImage(sprite, (int)coordinates.topLeftCorner_x, (int)coordinates.topLeftCorner_y, null);



    }
}
