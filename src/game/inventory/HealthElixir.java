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
     *
     * @param coordinates Coordinates of item. If in inventory, coordinates specify position to draw in inventory panel.
     *                    If floating in world, coordinates specify position to draw in world.
     */
    public HealthElixir(Coordinates coordinates) {
        super("Health Elixir", "Restores 30 health.", coordinates);
        initSprite();
    }

    public void initSprite() {

        try {
            this.sprite = ImageIO.read(Objects.requireNonNull(getClass().getResource("/sprites/inventory/elixir_48.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public boolean use() {
        Gameplay.player.heal(healthRestored);
        return true;
    }

    @Override
    public void draw(Graphics graphics) {
        if (coordinates.inScreen()) {
            graphics.drawImage(sprite, (int) coordinates.screenX - 24, (int) coordinates.screenY - 24, null);
        }
    }
}
