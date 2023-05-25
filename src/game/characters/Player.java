package game.characters;

import game.Movable;

import java.awt.*;

public class Player extends Entity implements Movable {
    public int money;
    public int experience;
    public int experienceToNextLevel;

    public Player() {}

    @Override
    public void draw(Graphics graphics) {
        //TODO add implementation of drawing
        graphics.setColor(Color.WHITE);

        graphics.fillOval(20, 30, 100, 100);

    }

    @Override
    public void move(double diffSeconds) {

    }
}
