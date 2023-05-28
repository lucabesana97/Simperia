package game;

import java.awt.*;

public abstract class GameObject {

    public Coordinates coordinates;

    public abstract void draw(Graphics graphics);

    public boolean isColliding(GameObject other) {
        return this.coordinates.intersects(other.coordinates);
    }

}
