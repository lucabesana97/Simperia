package game.inventory;

import game.entities.Entity;
import main.Gameplay;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;

public class HealthElixir extends InventoryItem{

    int healthRestored;

    public HealthElixir(String name, String description) {
        super(name, description);
        healthRestored = 30;
        initSprite();
    }

    public HealthElixir(String name, String description, int healthRestored) {
        super(name, description);
        this.healthRestored = healthRestored;
        initSprite();
    }

    public void initSprite() {
        try {
            this.sprite = ImageIO.read(Objects.requireNonNull(getClass().getResource("/sprites/inventory/elixir.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void use() {
        Gameplay.player.heal(healthRestored);
    }

    @Override
    public void draw(double x, double y) {



    }
}
