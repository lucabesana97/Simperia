package game.characters;

import game.Movable;
import game.inventory.InventoryItem;

import java.awt.*;

public class Enemy extends Entity implements Movable {
    InventoryItem loot;
    int experienceOnKill;
    int moneyOnKill;

    @Override
    public void draw(Graphics graphics) {

    }

    @Override
    public void move(double diffSeconds) {

    }

//    @Override
//    public void stopMovement() {
//
//    }
//
//    @Override
//    public void continueMovement() {
//
//    }
}
