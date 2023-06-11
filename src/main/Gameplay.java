
package main;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.swing.JButton;

import game.environment.AsteroidMap;
import game.inventory.Item;
import game.Coordinates;
import game.entities.*;
import game.environment.GameMap;
import game.inventory.Inventory;
import game.inventory.ItemStack;
import gui.GamePanel;
import gui.InventoryPanel;
import gui.Panel;
import gui.PausePanel;
import helperFunctions.Utility;
import input.KeyHandler;
import input.Keys;
import objState.EnemyState;
import objState.FightState;
import objState.MovingState;
import output.Sound;

public class Gameplay {
    final GamePanel panel;
    final KeyHandler keyHandler;

    public static Player player;
    public static Inventory inventory;
    public static GameMap map;
    private boolean changeMap = false;
    private NPC npc;
    private List<ItemStack> stacksOnWorld = new ArrayList<>();
    private List<Enemy> enemies = new ArrayList<>();

    private List<Bullet> playerBullets = new ArrayList<>();
    private List<Warp> warps = new ArrayList<>();
    private List<Warp> mapWarps = new ArrayList<>();
    private GameMap mapDestination;
    private Warp warpDestination;
    private PausePanel pausePanel;
    private InventoryPanel inventoryPanel;
    private JButton pauseButton;
    private JButton inventoryButton;
    private boolean paused;

    private Item deletedItem;
    private Sound soundtrack = new Sound();
    Sound effects = new Sound();


    public Gameplay(Panel panel, KeyHandler keyHandler) {
        this.panel = (GamePanel) panel;
        this.keyHandler = keyHandler;
        paused = false;
    }


    public void init() {

        //Don't change the order
        map = new AsteroidMap();
        player = new Player();
        map.init(player);

        loadObjects();
        inventory = new Inventory();
        npc = new NPC(new Coordinates(100, 100, 32, 32));

        panel.addKeyListener(keyHandler);

        // Temporary buttons for testing
        initializePauseButton();
        initializeInventoryButton();

        // Pause panel
        pausePanel = new PausePanel();
        panel.add(pausePanel);

        // Inventory panel
        inventoryPanel = new InventoryPanel();
        panel.add(inventoryPanel);

        // Soundtrack
        soundtrack.stopMusic();
        soundtrack.playMusic(2);
        soundtrack.changeVolume(-20);
    }

    private void initializeInventoryButton() {
        inventoryButton = new JButton("Inventory");
        inventoryButton.setBounds(panel.getWidth() - 210, 10, 100, 50);
        inventoryButton.setBackground(new Color(0X593DB5));
        inventoryButton.addActionListener(e -> {
            inventoryPanel.setVisible(true);
        });
        panel.add(inventoryButton);
    }

    private void initializePauseButton() {
        pauseButton = new JButton("Pause");
        pauseButton.setBounds(panel.getWidth() - 90, 10, 70, 50);
        pauseButton.setBackground(new Color(0X593DB5));
        pauseButton.addActionListener(e -> {
            pausePanel.setVisible(true);
            pause(); // Pause the game
        });
        panel.add(pauseButton);
    }

    public void run() {

        long lastTick = System.currentTimeMillis();

        while (true) {
            long currentTick = System.currentTimeMillis();
            double diffSeconds = (currentTick - lastTick) / 100.0;
            lastTick = currentTick;


            try {
                handleUserInput();
            } catch (Exception e) {
                System.out.println(e.getCause());
            }

            update(diffSeconds);

            panel.clear();
            drawElements();
            panel.redraw();
            System.out.flush();

        }
    }

    private void update(double diffSeconds) {
        // Workaround to resume the game
        if (!pausePanel.isVisible()) {
            resume();
        }

        if (paused) {
            return;
        }

        // Player
        player.move(diffSeconds);

        //Enemies
        Iterator<Enemy> enemyIter = enemies.iterator();
        while (enemyIter.hasNext()) {
            Enemy enemy = enemyIter.next();
            if (enemy != null && enemy.enemyState == EnemyState.DEAD) {
                enemyIter.remove();
            }
        }

        for (Enemy enemy : enemies) {
            enemy.move(diffSeconds, player);
        }
        for (Warp warp : warps) {
            if (player.isColliding(warp)) {
                player.teleport(warp);
            }
        }
        Iterator<Bullet> bulletIterator = playerBullets.iterator();
        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            bullet.move(diffSeconds, player);
            if (bullet.enemyState == EnemyState.DEAD) {
                bulletIterator.remove();
            }
            for (Enemy enemy : enemies) {
                if (enemy.isColliding(bullet)) {
                    System.out.println(enemy.health);
                    bullet.attack(enemy);
                }
            }
        }
		/*
		for(Item item : items){
			if(player.isColliding(item)){
				player.pickUp(item);
				deletedItem = item;
			}
		}
		if(deletedItem != null){
			items.remove(deletedItem);
			deletedItem = null;
		}
		*/
        for (Warp warp : mapWarps) {
            if (player.isColliding(warp)) {
                changeMap = true;
                mapDestination = warp.map;
                warpDestination = warp;
            }
        }
        if (changeMap) {
            warpDestination.map.player = player;
            map = mapDestination;
            loadObjects();
            player.teleport(warpDestination);
            player.mapHeight = map.mapImage.getHeight();
            player.mapWidth = map.mapImage.getWidth();
            mapDestination = null;
            changeMap = false;
        }

        //Items
        for (int i = 0; i < stacksOnWorld.size(); i++) {
            if (stacksOnWorld.get(i) != null) {
                if (player.isColliding(stacksOnWorld.get(i))) {
                    ItemStack remainder = inventory.addStack(stacksOnWorld.get(i));

                    if (remainder == null) { //The item was added, it has to be deleted from the world
                        stacksOnWorld.remove(i);
                    } else { //There are items that didn't fit int he inventory.
                        stacksOnWorld.get(i).amount = remainder.amount;
                    }

                }
            }
        }

        for (int i = 0; i < Inventory.NUMBER_OF_SLOTS; i++) {
            if (inventory.slots[i] == null) {
                System.out.println("null");
            } else {
                System.out.println(inventory.slots[i].item.name + inventory.slots[i].amount);
            }
        }

        System.gc();
    }

    private void drawElements() {

        panel.draw(map);
        for (Enemy enemy : enemies) {
            panel.draw(enemy);
        }
        for (Bullet bullet : playerBullets) {
            panel.draw(bullet);
        }
        for (Warp warp : warps) {
            panel.draw(warp);
        }
        for (Warp warp : mapWarps) {
            panel.draw(warp);
        }
        for (ItemStack item : stacksOnWorld) {
            panel.draw(item.item);
        }
        panel.draw(npc);
        panel.draw(player);
        panel.draw(pauseButton);
        panel.draw(inventoryButton);
        //pauseButton.repaint();
        //inventoryButton.repaint();
        pausePanel.repaint();
        inventoryPanel.repaint();

        /////////
    }

    private void handleUserInput() {


        final Set<Keys> pressedKeys = keyHandler.getKeys();
        boolean horStill = true;
        boolean verStill = true;
        boolean sprintStill = true;
        for (Keys keyCode : pressedKeys) {
            switch (keyCode) {
                case ATTACK:
                    if (player.currentWeapon == player.GUN) {
                        if (player.shootState == FightState.READY) {
                            int angle = Utility.getAimAngle(player);
                            playerBullets.add(new Bullet(angle, player.coordinates, Bullet.PLAYER, 10, 15));
                            player.shootState = FightState.RELOADING;
                        }
                    } else if (player.currentWeapon == player.SWORD) {
                        for (Enemy enemy : enemies) {
                            if (player.inSlashRange(enemy)) {
                                player.attack(enemy);
                            }
                        }
                    }
                    break;
                case PAUSE:
                    break;
                case LEFT:
                    player.xState = MovingState.LEFT;
                    horStill = false;
                    break;
                case RIGHT:
                    player.xState = MovingState.RIGHT;
                    horStill = false;
                    break;
                case UP:
                    player.yState = MovingState.UP;
                    verStill = false;
                    break;
                case DOWN:
                    player.yState = MovingState.DOWN;
                    verStill = false;
                    break;
                case SWITCH:
                    player.switchWeapon();
                    break;
                case SPRINT:
                    player.sprint();
                    sprintStill = false;
                    break;
                default:
                    break;
            }
        }
        //System.out.println(player.yState + "\t" + player.xState);
        if (horStill) {
            player.xState = MovingState.STILL;
        }
        if (verStill) {
            player.yState = MovingState.STILL;
        }
        if (sprintStill) {
            player.slowDown();
        }

    }

    public void pause() {
        paused = true;
    }

    public void resume() {
        paused = false;
    }

    public boolean isPaused() {
        return paused;
    }

    public void loadObjects() {
        enemies.removeAll(enemies);
        stacksOnWorld.removeAll(stacksOnWorld);
        warps.removeAll(warps);
        mapWarps.removeAll(mapWarps);
        enemies.addAll(map.enemies);
        stacksOnWorld.addAll(map.stacksOnWorld);
        warps.addAll(map.warps);
        mapWarps.addAll(map.mapWarps);
    }

}
