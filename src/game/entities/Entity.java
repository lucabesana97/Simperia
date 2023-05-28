package game.entities;

import game.GameObject;
import game.Coordinates;
import game.Sprites;

import java.awt.Graphics;

public abstract class Entity extends GameObject {
    public Sprites sprites;
    public String name;
    public double speed;
    public int health;
    public int maxHealth;
    public int attack;
    public int defense;
    public int level;
    public double invincibilityCooldown = 5;
    public double invincibilityTimer = 0;

    public Entity() {
        super();

        this.coordinates = new Coordinates(0, 0, 100, 100);

        this.health = 100;
        this.maxHealth = 100;
        this.attack = 10;
        this.defense = 10;
        this.level = 1;
        this.sprites = new Sprites();
    }

    public void attack(Entity entity) {
        System.out.println(this.name + " attacked " + entity.name + " for " + this.attack + " damage.");
        entity.takeDamage(this.attack);
    }

    public void takeDamage(int damage) {
        if (this.invincibilityTimer < this.invincibilityCooldown) {
            return;
        }
        this.invincibilityTimer = 0;
        this.health -= damage;
        if (this.health <= 0) {
            this.die();
        }
    }

    public void die() {
        System.out.println(this.name + " died.");
    }

    public void move(double diffSeconds) {
        //TODO add implementation of movement
    }

    public abstract void draw(Graphics graphics);
}
