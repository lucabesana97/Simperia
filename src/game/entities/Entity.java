package game.entities;

import game.GameObject;
import game.Coordinates;
import game.Sprites;

import java.awt.Graphics;

public abstract class Entity extends GameObject {
    public Sprites sprites;
    public String name;
    public double speed;
    public Coordinates coordinates;
    public int health;
    public int maxHealth;
    public int attack;
    public int defense;
    public int level;

    public Entity() {}

    public void attack(Entity entity) {
        //TODO add implementation of attack
    }

    public void takeDamage(int damage) {
        //TODO add implementation of taking damage
    }

    public void die() {
        //TODO add implementation of dying
    }

    public void move(double diffSeconds) {
        //TODO add implementation of movement
    }

    public abstract void draw(Graphics graphics);
}
