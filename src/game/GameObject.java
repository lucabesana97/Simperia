package game;

import java.awt.*;

public abstract class GameObject {

    Coordinates coordinates;

    public abstract void draw(Graphics graphics);

    public boolean isColliding(GameObject other) {
        return false;
    }

}
