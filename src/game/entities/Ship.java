package game.entities;

import game.Coordinates;
import game.GameObject;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;

public class Ship extends GameObject {


    public Ship(Coordinates coordinates) {
        super(coordinates);
        initSprite();
    }

    public void initSprite() {

        try {
            this.image = ImageIO.read(Objects.requireNonNull(getClass().getResource("/sprites/maps/ship.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
