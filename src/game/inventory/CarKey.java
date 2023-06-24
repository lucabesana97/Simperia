package game.inventory;

import game.Coordinates;
import game.GameObject;
import game.environment.AsteroidMap;
import game.environment.AsteroidMap2;
import main.Gameplay;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class CarKey extends Item {

    GameObject ship;

    public CarKey(Coordinates coordinates, GameObject ship) {
        super("Ship card", "A card that allows you to drive your ship.", coordinates);
        this.ship = ship;
        initSprite();
    }

    public void initSprite() {

        try {
            this.sprite = ImageIO.read(Objects.requireNonNull(getClass().getResource("/sprites/inventory/car-key.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public boolean use() {
        if ((Gameplay.map instanceof AsteroidMap || Gameplay.map instanceof AsteroidMap2) && Gameplay.player.isColliding(ship) ) {
            Gameplay.victory();
            System.out.println("You win!");
            return true;
        }
        return false;
    }

    @Override
    public void draw(Graphics graphics) {
        if (coordinates.inScreen()) {
            graphics.drawImage(sprite, (int) coordinates.screenX, (int) coordinates.screenY, null);
        }
    }
}
