
package main;

import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.swing.*;

import game.GameObject;
import game.environment.AsteroidMap;
import game.inventory.Item;
import game.Coordinates;
import game.GameState;
import game.entities.*;
import game.environment.GameMap;
import game.inventory.Inventory;
import game.inventory.ItemStack;
import gui.*;
import helperFunctions.Utility;
import input.KeyHandler;
import input.Keys;
import objState.EnemyState;
import objState.FightState;
import objState.MovingState;
import output.Sound;

public class Gameplay {
    private GamePanel panel;
    final KeyHandler keyHandler;

    public static Player player;
    public static Inventory inventory;
    public static GameMap map;
    private boolean changeMap = false;
    private NPC beginnerNPC;
    private List<ItemStack> stacksOnWorld = new ArrayList<>();
    private List<Enemy> enemies = new ArrayList<>();
    private List<Bullet> playerBullets = new ArrayList<>();
    private List<Warp> warps = new ArrayList<>();
    private List<Warp> mapWarps = new ArrayList<>();
    private List<GameObject> objects = new ArrayList<>();

    private GameMap mapDestination;
    private Warp warpDestination;
    private PausePanel pausePanel;
    private InventoryPanel inventoryPanel;
    private HomePanel homePanel;
    private GameState gameState;
    private boolean paused;

    private Item deletedItem;
    private Sound soundtrack = new Sound();
    Sound effects = new Sound();


    public Gameplay(Panel panel, KeyHandler keyHandler, GameFrame frame) {
        this.panel = (GamePanel) panel;
        this.pausePanel = frame.getPausePanel();
        this.inventoryPanel = frame.getInventoryPanel();

        this.keyHandler = keyHandler;
		gameState = GameState.PLAYING;
    }


    public void init() {

        // Don't change the order
        map = new AsteroidMap();
        player = new Player();
        map.init(player);

        loadObjects();
        inventory = new Inventory();

        panel.addKeyListener(keyHandler);

        // Soundtrack
        soundtrack.stopMusic();
        soundtrack.playMusic(2);
        soundtrack.changeVolume(-20);


//        JButton newGameButton = homePanel.getNewGameButton();
//        newGameButton.addActionListener(e -> {
//            this.panel = (GamePanel) panel;
//            gameState = GameState.PLAYING;
//            run();
//        });

        // Button actions of pause and inventory panels
        JButton resumeButton = pausePanel.getResumeButton();
        resumeButton.addActionListener(e -> resume());

        JButton muteButton = pausePanel.getMuteButton();
        muteButton.addActionListener(e -> { muteGame(muteButton); });

        JButton quitGameButton = pausePanel.getQuitButton();
        quitGameButton.addActionListener(e -> quitGame());

        JButton closeInventoryButton = inventoryPanel.getCloseButton();
        closeInventoryButton.addActionListener(e -> closeInventory());

        JButton throwItemButton = inventoryPanel.getThrowItemButton();
        throwItemButton.addActionListener(e -> throwItem());

        JButton useItemButton = inventoryPanel.getUseItemButton();
        useItemButton.addActionListener(e -> useItem());
    }

	public void run() {

        long lastTick = System.currentTimeMillis();

        while (true) {
            long currentTick = System.currentTimeMillis();
            double diffSeconds = (currentTick - lastTick) / 100.0;
            lastTick = currentTick;

            this.panel.requestFocusInWindow();
            if (gameState == GameState.PAUSED){
                pausePanel.setVisible(true);
                pausePanel.repaint();
            } else if (gameState == GameState.INVENTORY) {
                inventoryPanel.setVisible(true);
                inventoryPanel.repaint();
            } else {
                pausePanel.setVisible(false);
                inventoryPanel.setVisible(false);

                update(diffSeconds);

                panel.clear();
                drawElements();
                panel.redraw();

                try {
                    handleUserInput();
                } catch (Exception e) {
                    System.out.println("Error: " + e.getCause());
                }
            }
            System.out.flush();
        }
    }

	private void update(double diffSeconds) {

        // Pauses the game
		if (gameState == GameState.PAUSED || gameState == GameState.INVENTORY) {
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

                if (enemies.isEmpty()) {

                    try {
                        BufferedImage floorSymbol = ImageIO.read(getClass().getResourceAsStream("/sprites/maps/cavern_floor_win.png"));
                        objects.get(0).image = floorSymbol;
                    } catch (Exception e) {
                        System.out.println("Couldn't load floor symbol: " + "\tReason: " + e.getCause());

                    }
                }

            }
        }

        for (Enemy enemy : enemies) {
            enemy.move(diffSeconds, player);
        }
        if (beginnerNPC != null) {
            if (player.isColliding(beginnerNPC)) {
                // Player is talking to the NPC
                String text = beginnerNPC.interact();
                System.out.println("Text: " + text);
            } else {
                // Player is not talking to the NPC
                beginnerNPC.stopInteracting();
            }
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
                    System.out.println("Enemy health: " + enemy.health);
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
                    } else { //There are items that didn't fit in the inventory.
                        stacksOnWorld.get(i).amount = remainder.amount;
                    }

                }
            }
        }

        //Objects



        System.gc();
    }

    private void drawElements() {

        panel.draw(map);
        if (beginnerNPC != null){
            panel.draw(beginnerNPC);
        }
        int killedEnemies = 0;
        for (Enemy enemy : enemies) {
            panel.draw(enemy);
            if (enemy.enemyState == EnemyState.DEAD) {
                killedEnemies++;
            }
        }
        if (beginnerNPC != null) {
            if (killedEnemies == 1) {
                beginnerNPC.quest.completed = true;
            }
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
        for (GameObject object : objects) {
            panel.draw(object);
        }
        panel.draw(player);
    }

    private void handleUserInput() {

        final Set<Keys> pressedKeys = keyHandler.getKeys();
        boolean horStill = true;
        boolean verStill = true;
        boolean sprintStill = true;
        for (Keys keyCode : pressedKeys) {
            //System.out.println("Key pressed: " + keyCode);
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
                case PAUSE:
                    if (!pausePanel.isVisible()) {
                        pause();
                    } else {
                        resume();
                    }
                    break;
                case INVENTORY:
                    if (!inventoryPanel.isVisible()) {
                        openInventory();
                    } else {
                        closeInventory();
                    }
                    break;
                case SPRINT:
                    player.sprint();
                    sprintStill = false;
                    break;
                default:
                    break;
            }
        }
//        System.out.println(player.yState + "\t" + player.xState);
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

    private void openInventory() {
        inventoryPanel.setVisible(true);
        gameState = GameState.INVENTORY;
    }

    private void closeInventory() {
        inventoryPanel.setVisible(false);
        gameState = GameState.PLAYING;
    }

    public void pause() {
        pausePanel.setVisible(true);
        gameState = GameState.PAUSED;
    }

	public void resume() {
        pausePanel.setVisible(false);
        gameState = GameState.PLAYING;
    }

	public boolean isPaused() {	return gameState == GameState.PAUSED; }

    public void muteGame (JButton muteButton) {
        if (soundtrack.isMusicPlaying()) {
            soundtrack.stopMusic();
            muteButton.setText("Unmute");
        } else {
            soundtrack.playMusic(2);
            soundtrack.changeVolume(-20);
            muteButton.setText("Mute");
        }
    }

    public void quitGame() {
        // TODO: go back to home screen
    }

    public void throwItem() {
        // TODO
    }

    public void useItem() {
        // TODO
    }

    public void loadObjects() {
        enemies.removeAll(enemies);
        stacksOnWorld.removeAll(stacksOnWorld);
        warps.removeAll(warps);
        mapWarps.removeAll(mapWarps);
        objects.removeAll(objects);
        beginnerNPC = null;

        enemies.addAll(map.enemies);
        stacksOnWorld.addAll(map.stacksOnWorld);
        warps.addAll(map.warps);
        mapWarps.addAll(map.mapWarps);
        objects.addAll(map.objects);
        beginnerNPC = map.beginnerNPC;
    }

}
