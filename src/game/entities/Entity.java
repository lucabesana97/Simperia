package game.entities;

import game.GameObject;
import game.Coordinates;
import game.Sprites;
import main.Gameplay;
import objState.EnemyState;

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
    public int experienceOnKill = 0;

    public Entity() {
        super(new Coordinates(0, 0, 100, 100));

        this.health = 100;
        this.maxHealth = 100;
        this.attack = 10;
        this.defense = 10;
        this.level = 1;
        this.sprites = new Sprites();
    }

    public void attack(Entity entity) {
//        System.out.println(this.name + " attacked " + entity.name + " for " + this.attack + " damage.");
        entity.takeDamage(this.attack);
        // this is player, add experience
        if (this instanceof Bullet && ((Bullet) this).owner == Bullet.PLAYER && ((Enemy) entity).enemyState == EnemyState.DEAD){
            Gameplay.player.gainXp(((Enemy)entity).experienceOnKill);
            System.out.println("Player is on level " + Gameplay.player.level + " and has " + Gameplay.player.xp + " experience.");
        }
    }

    /**
     * Heal the entity by the given amount, but not more than the max health.
     * @param amount The amount to heal by.
     */
    public void heal(int amount) {
        this.health += amount;
        if (this.health > this.maxHealth) {
            this.health = this.maxHealth;
        }
    }

    public void gainXp(int amount) {
    }

    public void takeDamage(int amount) {
        if (this.invincibilityTimer < this.invincibilityCooldown) {
            return;
        }
        System.out.println(this.name + " took " + amount + " damage.");
        this.invincibilityTimer = 0;
        this.health -= amount;
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
