package game.entities;

import game.Coordinates;
import game.GameObject;
import game.Quest;
import game.inventory.HealthElixir;
import game.inventory.Inventory;
import game.inventory.ItemStack;
import gui.InventoryPanel;
import main.Gameplay;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;
import java.util.Objects;

import static main.Gameplay.player;

public class NPC extends GameObject {

    public BufferedImage sprite;
    public Quest quest;
    //public boolean firstItemGiven = false;
    public boolean rewardGiven = false;
    private boolean npcDone = false;
    public boolean interacting = false;

    public NPC(Coordinates coordinates) {
        super(coordinates);
//        this.coordinates = new Coordinates(coordinates.topLeftCorner_x - 24, coordinates.topLeftCorner_y - 24, 37, 64); // (x, y, width, height
        try {
            sprite = ImageIO.read(Objects.requireNonNull(getClass().getResource("/sprites/npcs/npc1_64.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        quest = new Quest(
                "Hey, you there! I'm in desperate need of your help. These wretched monsters have invaded my asteroid, " +
                        "and I can't stand their presence any longer. Coincidentally, you're searching for your missing card, " +
                        "aren't you? Well, I witnessed those scoundrels taking it. I implore you, aid me in reclaiming my beloved asteroid. " +
                        "Will you help me? Here you go, take these health elixirs to help you on your mission.",
                "Thank you! I knew I could count on you. " +
                        "I'll be sure to reward you handsomely for your efforts.",
                500,
                new HealthElixir(
                        new Coordinates(0, 0, 0, 0)
                ),
                100,
                1
        );

    }

    public String interact() {
        this.interacting = true;
        if(!player.quests.contains(quest)){
            player.quests.add(quest);
        }

        String toBeReturned = "";
        Quest playerQuest = player.getQuestById(quest.iD);

        if (playerQuest.completed) {
            if (!rewardGiven) {
                rewardGiven = true;
                player.gainXp(playerQuest.xpReward);
                player.coins += playerQuest.coinsReward;
                //Gameplay.inventory.addStack(new ItemStack(playerQuest.item, 1, true));
                toBeReturned = playerQuest.finalText;
            } else if (rewardGiven && !npcDone) {
                toBeReturned = playerQuest.finalText;
            } else if (npcDone) {
                toBeReturned = "I have nothing more to say to you.";
            }
        } else if (!playerQuest.firstItemGiven) {
            Gameplay.inventory.addStack(new ItemStack(new HealthElixir(
                    new Coordinates(0, 0, 0, 0)
            ), 5, true));
            Gameplay.inventoryPanel.updateInventoryUI();
            toBeReturned = playerQuest.initialText;
            playerQuest.firstItemGiven = true;
        } else{
            toBeReturned = "My life is in your hands, I'm counting on you!";
        }
        return toBeReturned;

    }

    public void stopInteracting() {
        this.interacting = false;
        if (quest.completed && rewardGiven) {
            npcDone = true;
        }
    }

    public void draw(Graphics graphics) {
        if (coordinates.inScreen()) {
            graphics.drawImage(sprite, (int) coordinates.screenX - 24, (int) coordinates.screenY - 24, null);
        }
        // draw circle around player
//        graphics.setColor(Color.RED);
//        graphics.drawRect((int) coordinates.topLeftCorner_x, (int) coordinates.topLeftCorner_y, (int) coordinates.size_X,(int)  coordinates.size_Y);
    }
}
