package game.characters;

import game.Coordinates;
import game.Sprites;

public abstract class Character {
    public Sprites sprites;
    public String name;
    public double speed;
    public Coordinates coordinates;
    public int health;
    public int maxHealth;
    public int attack;
    public int defense;
    public int level;


    public Character() {}

    public void attack(Character character) {
        //TODO add implementation of attack
    }

    public void takeDamage(int damage) {
        //TODO add implementation of taking damage
    }

    public void die() {
        //TODO add implementation of dying
    }

    public void move() {
        //TODO add implementation of movement
    }

    public abstract void draw();
}
