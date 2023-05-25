package game;

import java.awt.*;

public abstract class AGameObject {

    Coordinates coordinates;

    public abstract void draw(Graphics graphics);

    public boolean isColliding(AGameObject other) {
        return false;
    }

}
