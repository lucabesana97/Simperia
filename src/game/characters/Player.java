package game.characters;

import game.Coordinates;

import java.awt.*;

public class Player extends Entity {
    public int money;
    public int experience;
    public int experienceToNextLevel;

    public Player() {
        coordinates = new Coordinates(400, 400, 64);
    }

    public void move(double diffSeconds) {
        while(coordinates.x > 0){
            coordinates.x -= 0.1 * diffSeconds;
        }
    }

    public void draw(Graphics g) {
        //TODO add implementation of drawing
        g.fillRoundRect((int)coordinates.x, (int)coordinates.y,coordinates.size,coordinates.size,4,4);
    }

}
