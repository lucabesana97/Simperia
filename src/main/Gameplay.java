
package main;

import java.awt.*;
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
import game.GameState;
import game.entities.*;
import game.environment.GameMap;
import game.inventory.Inventory;
import game.inventory.ItemStack;
import gui.*;
import gui.Panel;
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
    public static List<Bullet> enemyBullets = new ArrayList<>();
    private List<Warp> warps = new ArrayList<>();
    private List<Warp> mapWarps = new ArrayList<>();
    private List<GameObject> objects = new ArrayList<>();

    private GameMap mapDestination;
    private Warp warpDestination;
    private final PausePanel pausePanel;
    private final InventoryPanel inventoryPanel;
    private final HomePanel homePanel;
    private DialogPanel dialogPanel;
    private GameState gameState;
    private Item deletedItem;
    private final Sound soundtrack = new Sound();
    Sound effects = new Sound();
    private JButton newGameButton;

    public Gameplay(Panel panel, KeyHandler keyHandler, GameFrame frame) {
        this.panel = (GamePanel) panel;
        this.pausePanel = frame.getPausePanel();
        this.inventoryPanel = frame.getInventoryPanel();
        this.dialogPanel = frame.getDialogPanel();
        this.homePanel = frame.getHomePanel();
        this.keyHandler = keyHandler;
		this.gameState = GameState.PLAYING;

        newGameButton = homePanel.getNewGameButton();
        newGameButton.addActionListener(e -> {
            homePanel.setVisible(false);
            gameState = GameState.PLAYING;

            init();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    run_game();
                }
            }).start();
        });
    }


    public void init() {

        // Don't change the order
        map = new AsteroidMap();
        player = new Player();
        map.init(player);

        loadObjects();
        inventory = new Inventory();
        inventoryPanel.init(inventory);

        panel.addKeyListener(keyHandler);

        // Soundtrack
        soundtrack.stopMusic();
        soundtrack.playMusic(2);
        soundtrack.changeVolume(-20);

        // Button actions of pause and inventory panels
        JButton resumeButton = pausePanel.getResumeButton();
        resumeButton.addActionListener(e -> resume());

        JButton muteButton = pausePanel.getMuteButton();
        muteButton.addActionListener(e -> { muteGame(muteButton); });

        JButton quitGameButton = pausePanel.getQuitButton();
        quitGameButton.addActionListener(e -> quitGame());

        JButton closeInventoryButton = inventoryPanel.getCloseButton();
        closeInventoryButton.addActionListener(e -> closeInventory());

        JButton closeDialogButton = dialogPanel.getCloseDialogButton();
        closeDialogButton.addActionListener(e -> closeNPCDialog());
    }

	public void run_game() {

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
            } else if (gameState == GameState.DIALOG) {
                dialogPanel.setVisible(true);
                dialogPanel.repaint();
            } else {
                pausePanel.setVisible(false);
                inventoryPanel.setVisible(false);
                dialogPanel.setVisible(false);

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
            if (player.isColliding(beginnerNPC) && !(beginnerNPC.interacting)) {
                // Player is talking to the NPC
                String text = beginnerNPC.interact();
                displayNPCDialog(text);
                //System.out.println("Text: " + text);
            } else if(!player.isColliding(beginnerNPC)){
                // Player is not talking to the NPC
                beginnerNPC.stopInteracting();
                closeNPCDialog();
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
                if (enemy.isColliding(bullet)){
//                    System.out.println("Enemy health: " + enemy.health);
                    bullet.attack(enemy);
                    if (enemy.enemyState != EnemyState.DEAD) {
                        enemy.enemyState = EnemyState.DAMAGED;
                    }
                }
            }
        }

        Iterator<Bullet> bulletIteratorEnemy = enemyBullets.iterator();
        while (bulletIteratorEnemy.hasNext()) {
            Bullet bullet = bulletIteratorEnemy.next();
            bullet.move(diffSeconds, player);
            if (bullet.enemyState == EnemyState.DEAD) {
                bulletIteratorEnemy.remove();
            }
            if (player.isColliding(bullet)){
//                System.out.println("Player health: " + player.health);
                bullet.attack(player);
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
                mapDestination = GameMap.warpTo(warp.mapID);
                warpDestination = warp;
            }
        }
        if (changeMap) {
            mapDestination.player = player;
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
                    inventoryPanel.updateInventoryUI();
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
        for (Bullet bullet : enemyBullets) {
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
        boolean npcTalk = false;
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
                    if (!pausePanel.isVisible()) { pause(); }
                    else { resume(); }
                    break;
                case INVENTORY:
                    if (!inventoryPanel.isVisible()) { openInventory(); }
                    else { closeInventory(); }
                    break;
                case SPRINT:
                    player.sprint();
                    sprintStill = false;
                    break;
                case NPC_TALK:
                    // Leave it empty for now
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
        inventoryPanel.clearSelectedSlot();
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

    public void muteGame (JButton muteButton) {
        if (soundtrack.isMusicPlaying()) {
            soundtrack.stopMusic();
            muteButton.setIcon(new ImageIcon("resources/sprites/pause/music_muted.png"));
        } else {
            soundtrack.playMusic(2);
            soundtrack.changeVolume(-20);
            muteButton.setIcon(new ImageIcon("resources/sprites/pause/music_playing.png"));
        }
    }

    public void quitGame() {
        // TODO: go back to home screen
    }

    public void displayNPCDialog(String text) {
        dialogPanel.setDialogText(text);
        dialogPanel.setVisible(true);
        gameState = GameState.DIALOG;
    }

    public void closeNPCDialog() {
        dialogPanel.setVisible(false);
        gameState = GameState.PLAYING;
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

