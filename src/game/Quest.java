package game;

import game.inventory.Item;
import game.inventory.ItemStack;
import main.Gameplay;

public class Quest {
    public String initialText;
    public String finalText;
    public int coinsReward;
    public int xpReward;
    public Item itemReward;
    public boolean completed = false;

    public Quest(String initialText, String finalText, int coinsReward, Item itemReward, int xpReward) {
        this.initialText = initialText;
        this.finalText = finalText;
        this.coinsReward = coinsReward;
        this.xpReward = xpReward;
        this.itemReward = itemReward;
    }

    public void complete() {
        completed = true;

        //Reward is given to the player instantly (doesnt wait till the player talks
        //to the NPC again)
        Gameplay.inventory.addStack(new ItemStack(itemReward, 1, true));
        Gameplay.player.coins += coinsReward;
        Gameplay.player.gainXp(xpReward);
    }

}
