
package main;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Timer;

import game.Coordinates;
import game.GameObject;
import game.Quest;
import game.environment.AsteroidMap;
import game.environment.AsteroidMap2;
import game.environment.CaveMap;
import game.inventory.*;
import game.GameState;
import game.entities.*;
import game.environment.GameMap;
import gui.*;
import gui.Panel;
import gui.elements.HUD;
import helperFunctions.Utility;
import input.KeyHandler;
import input.Keys;
import objState.EnemyState;
import objState.FightState;
import objState.MovingState;
import output.Sound;

public class Gameplay {
    private final GamePanel panel;
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
    public List<GameObject> objects = new ArrayList<>();
    private List<GameObject> rocks = new ArrayList<>();
    public static GameObject ship;

    private GameMap mapDestination;
    private Warp warpDestination;
    private final PausePanel pausePanel;
    public static InventoryPanel inventoryPanel;
    private final HomePanel homePanel;
    private final DialogPanel dialogPanel;
    private final InfoPanel infoPanel;
    private static VictoryFailPanel victoryFailPanel;
    private static GameState gameState;
    private Item deletedItem;
    private final Sound soundtrack = new Sound();
    Sound effects = new Sound();
    private HUD hudBackground;
    public static HUD healthBar;
    private HUD xpBar;
    private HUD levelLabel;
    private boolean firstLoop;
    private final boolean[] rocksDropped = new boolean[6];
    public static boolean isMuted = false;
    private final int soundtrackVolume = -40;
    private boolean bossSpawned = false;

    private GameObject rock;
    private BufferedImage rockImage = null;

    public Gameplay(Panel panel, KeyHandler keyHandler, GameFrame frame) {
        this.panel = (GamePanel) panel;
        this.pausePanel = frame.getPausePanel();
        this.inventoryPanel = frame.getInventoryPanel();
        this.dialogPanel = frame.getDialogPanel();
        this.homePanel = frame.getHomePanel();
        this.infoPanel = frame.getInfoPanel();
        victoryFailPanel = frame.getVictoryFailPanel();
        this.keyHandler = keyHandler;
        gameState = GameState.HOME;

        JButton newGameButton = homePanel.getNewGameButton();
        newGameButton.addActionListener(e -> {
            if (!isMuted) {
                effects.playSoundEffect(9);
            }
            gameState = GameState.PLAYING;
            homePanel.setVisible(false);
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
        hudBackground = new HUD(new Coordinates(10, 10, 361, 110), "/sprites/hud/hud.png");
        healthBar = new HUD(new Coordinates(121, 72, 230, 23), "/sprites/hud/healthbar.png");
        xpBar = new HUD(new Coordinates(112, 96, 95, 15), "/sprites/hud/xpbar.png");
        levelLabel = new HUD(new Coordinates(125, 63, 80, 30));
        ship = new Ship(new Coordinates(300, 500, 164, 91));

        panel.addKeyListener(keyHandler);

//        inventory.addStack(new ItemStack(new CarKey(new Coordinates(100, 100, 32, 32), ship), 1, true));
//        inventoryPanel.updateInventoryUI();

        // Soundtrack
        soundtrack.stopMusic();
        soundtrack.playMusic(2);
        soundtrack.changeVolume(soundtrackVolume);

        createButtons();
        firstLoop = true;
        isMuted = false;

        resetRocks();

        try {
            rockImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/sprites/maps/cavern_rock.png")));
        } catch (Exception e) {
            System.out.println("Couldn't load rock image: " + "\tReason: " + e.getCause());
        }
    }


    public void run_game() {

        long lastTick = System.currentTimeMillis();

        while (true) {
            long currentTick = System.currentTimeMillis();
            double diffSeconds = (currentTick - lastTick) / 100.0;
            lastTick = currentTick;

            this.panel.requestFocusInWindow();
            if (gameState == GameState.INFO) {
                infoPanel.setVisible(true);
                infoPanel.repaint();
            } else if (gameState == GameState.PAUSED) {
                pausePanel.setVisible(true);
                pausePanel.repaint();
            } else if (gameState == GameState.INVENTORY) {
                inventoryPanel.setVisible(true);
                inventoryPanel.repaint();
            } else if (gameState == GameState.DIALOG) {
                dialogPanel.setVisible(true);
                dialogPanel.repaint();
            } else if (gameState == GameState.WIN || gameState == GameState.GAMEOVER) {
                victoryFailPanel.setVisible(true);
                victoryFailPanel.repaint();
            } else if (gameState == GameState.PLAYING) {
                pausePanel.setVisible(false);
                inventoryPanel.setVisible(false);
                dialogPanel.setVisible(false);
                infoPanel.setVisible(false);
                victoryFailPanel.setVisible(false);

                update(diffSeconds);

                panel.clear();
                drawElements();
                panel.redraw();

                try {
                    handleUserInput();
                } catch (Exception e) {
                    System.out.println("Error: " + e.getCause());
                }

                if (firstLoop) {
                    displayInfo(getInitialText());
                    firstLoop = false;
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

                //boss killed
                if (map instanceof CaveMap && enemies.isEmpty() && this.bossSpawned) {

                    bossKilled();

                }

            }
        }


        for (Enemy enemy : enemies) {
            enemy.move(diffSeconds, player);
        }
        if (beginnerNPC != null) {
            if (player.isColliding(beginnerNPC) && !(beginnerNPC.interacting)) {
                String text = beginnerNPC.interact(); // Player is talking to the NPC
                displayNPCDialog(text);
            } else if (!player.isColliding(beginnerNPC)) {
                beginnerNPC.stopInteracting(); // Player is not talking to the NPC
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
            if (bullet.enemyState == EnemyState.DEAD || bullet.isTraveledMax()) {
                bulletIterator.remove();
                continue;
//                continue;
            }
            for (Enemy enemy : enemies) {
                if (enemy.isColliding(bullet)) {
//                    System.out.println("Enemy health: " + enemy.health);
                    bullet.attack(enemy);
                    System.out.println("Enemy health: " + enemy.health);
                    if (enemy.enemyState != EnemyState.DEAD) {
                        enemy.enemyState = EnemyState.DAMAGED;

                    }else{
                        // In 10% of the cases, the health potion will drop
                        Random random = new Random();
                        int randomInt = random.nextInt(10);
                        if (randomInt < 3) {
                            HealthElixir healthPotion = new HealthElixir(new Coordinates(enemy.coordinates.topLeftCorner_x, enemy.coordinates.topLeftCorner_y, 32, 32));
                            ItemStack healthPotionStack = new ItemStack(healthPotion, 1, false);
                            this.stacksOnWorld.add(healthPotionStack);
//                    healthPotions.add(healthPotion);
                        }
                    }

                    bulletIterator.remove();
                    break;
                }
            }
            if (bulletIterator != null) {
                if ((this.map.mapCollision(bullet) && map instanceof CaveMap) || bullet.coordinates.topLeftCorner_y < -100 || bullet.coordinates.topLeftCorner_y > 4000 || bullet.coordinates.topLeftCorner_x < -100 || bullet.coordinates.topLeftCorner_x > 3000) {
                    bulletIterator.remove();
                }
            }
        }

        Iterator<Bullet> bulletIteratorEnemy = enemyBullets.iterator();
        while (bulletIteratorEnemy.hasNext()) {
            Bullet bullet = bulletIteratorEnemy.next();
            bullet.move(diffSeconds, player);
            if (bullet.enemyState == EnemyState.DEAD || bullet.isTraveledMax()) {
                bulletIteratorEnemy.remove();
                continue;
            }
            if (player.isColliding(bullet)) {
//                System.out.println("Player health: " + player.health);
                bullet.attack(player);
                bulletIteratorEnemy.remove();
            } else if ((map.mapCollision(bullet) && map instanceof CaveMap) || bullet.coordinates.topLeftCorner_y < -100 || bullet.coordinates.topLeftCorner_y > 4000 || bullet.coordinates.topLeftCorner_x < -100 || bullet.coordinates.topLeftCorner_x > 3000) {
                try {
                    bulletIteratorEnemy.remove();
                } catch (Exception e) {
                    System.out.println("Error: " + e.getCause());
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
            resetRocks();
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

        //Map
        if (map instanceof CaveMap) {
            updateCaveMap(diffSeconds);
        }

        healthBar.updateHealthBar();
        xpBar.updateXpBar();
        levelLabel.updateLevelLabel();

        // Don't delete this
//        if (player.health <= 0) {
//            fail();
//        } TODO

        System.gc();
    }

    private void drawElements() {

        panel.draw(map);
        for (GameObject object : objects) {
            panel.draw(object);
        }
        if (beginnerNPC != null) {
            panel.draw(beginnerNPC);
        }
        for (Enemy enemy : enemies) {
            panel.draw(enemy);
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
        if ((map instanceof AsteroidMap) || (map instanceof AsteroidMap2)) {
            panel.draw(ship);
        }

        panel.draw(player);

        for (GameObject rock : rocks) {
            panel.draw(rock);
        }
        panel.draw(hudBackground);
        panel.draw(healthBar);
        panel.draw(xpBar);
        panel.draw(levelLabel);
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
                            playerBullets.add(new Bullet(angle, player.coordinates, Bullet.PLAYER, 10, 15, "/sprites/player/Player-bullet.png", 200));
                            if (!isMuted) {
                                effects.playSoundEffect(7);
                            }
                            player.shootState = FightState.RELOADING;
                        }
                    } else if (player.currentWeapon == player.SWORD) {
                        if (player.shootState == FightState.READY) {
                            player.angle = Utility.getAimAngle(player);
                            for (Enemy enemy : enemies) {
                                if (player.inSlashRange(enemy)) {
                                    player.attack(enemy);
                                    System.out.println("Enemy health: " + enemy.health);
                                }
                            }
                            if (!isMuted) {
                                effects.playSoundEffect(8);
                            }
                        }
                        player.shootState = FightState.RELOADING;
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
        boolean vicVisible = victoryFailPanel.isVisible();
        inventoryPanel.clearSelectedSlot();
        inventoryPanel.setVisible(false);
        gameState = GameState.PLAYING;
        if (vicVisible) {
            victoryFailPanel.setVisible(true);
            gameState = GameState.WIN;
        }
    }

    public void pause() {
        pausePanel.setVisible(true);
        gameState = GameState.PAUSED;
    }

    public void resume() {
        pausePanel.setVisible(false);
        gameState = GameState.PLAYING;
    }

    public void muteGame(JButton muteButton) {
        if (!isMuted) {
            soundtrack.stopMusic();
            muteButton.setIcon(new ImageIcon("resources/sprites/pause/music_muted.png"));
            isMuted = true;
        } else {
            soundtrack.playMusic(2);
            soundtrack.changeVolume(soundtrackVolume);
            muteButton.setIcon(new ImageIcon("resources/sprites/pause/music_playing.png"));
            isMuted = false;
        }
    }

    public void restartGame() { // TODO: fix
//        gameState = GameState.HOME;
//        if (pausePanel.isVisible()) {
//            pausePanel.setVisible
//            (false);
//        } else if (victoryFailPanel.isVisible()) {
//            victoryFailPanel.setVisible(false);
//        }
//        homePanel.setVisible(true);
    }

    public void quitGame() {
        System.exit(0);
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

    public void displayInfo(String text) {
        infoPanel.setInfoText(text);
        infoPanel.setVisible(true);
        gameState = GameState.INFO;
    }

    public void closeInfo() {
        infoPanel.setVisible(false);
        gameState = GameState.PLAYING;
    }

    public void loadObjects() {
        enemies.removeAll(enemies);
        stacksOnWorld.removeAll(stacksOnWorld);
        warps.removeAll(warps);
        mapWarps.removeAll(mapWarps);
        objects.removeAll(objects);
        rocks.removeAll(rocks);
        enemyBullets.removeAll(enemyBullets);
        playerBullets.removeAll(playerBullets);
        beginnerNPC = null;

        enemies.addAll(map.enemies);
        stacksOnWorld.addAll(map.stacksOnWorld);
        warps.addAll(map.warps);
        mapWarps.addAll(map.mapWarps);
        objects.addAll(map.objects);
        rocks.addAll(map.rocks);
        beginnerNPC = map.beginnerNPC;
    }

    private void createButtons() {
        // Button actions of pause/inventory/dialog/info panels
        JButton resumeButton = pausePanel.getResumeButton();
        resumeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isMuted) {
                    effects.playSoundEffect(9);
                }
                resume();
            }
        });


        JButton muteButton = pausePanel.getMuteButton();
        muteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isMuted) {
                    effects.playSoundEffect(9);
                }
                muteGame(muteButton);
            }
        });

        JButton quitGameButton = pausePanel.getQuitButton();
        quitGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isMuted) {
                    effects.playSoundEffect(9);
                }
                quitGame();
            }
        });


        JButton closeInventoryButton = inventoryPanel.getCloseButton();
        closeInventoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isMuted) {
                    effects.playSoundEffect(9);
                }
                closeInventory();
            }
        });

        JButton closeDialogButton = dialogPanel.getCloseDialogButton();
        closeDialogButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isMuted) {
                    effects.playSoundEffect(9);
                }
                closeNPCDialog();
            }
        });

        JButton closeInfoButton = infoPanel.getCloseInfoButton();
        closeInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isMuted) {
                    effects.playSoundEffect(9);
                }
                closeInfo();
            }
        });

        JButton playAgainButton = victoryFailPanel.getPlayButton();
        playAgainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isMuted) {
                    effects.playSoundEffect(9);
                }
                restartGame();
            }
        });
    }

    private void updateCaveMap(double diffseconds) {

        // DROP ROCKS


        //down left section
        if (!rocksDropped[0] && !rocksDropped[1] && player.coordinates.topLeftCorner_y < 2490 && player.coordinates.topLeftCorner_x < 650) {
            rocks = new ArrayList<>();
            rock = new GameObject(new Coordinates(330, 2530, 104, 106), rockImage);
            //if (rocks.size() == 0) {
                rocks.add(rock);
                dropRock(rock, 2000, diffseconds);
            //}
            rock = new GameObject(new Coordinates(430, 2530, 104, 106), rockImage);
            //if (rocks.size() == 1) {
                rocks.add(rock);
                dropRock(rock, 2200, diffseconds);
            //}
            for(int i = 20; i < 36; i++) {
                for(int j = 0; j < 3; j++) {
                    map.grid[i][158 + j] = 1;
                }
            }
            rocksDropped[0] = true;
        }
        //down right section
        else if (!rocksDropped[1] && !rocksDropped[0] && player.coordinates.topLeftCorner_y < 2490 && player.coordinates.topLeftCorner_x > 650) {
            rocks = new ArrayList<>();
            rock = new GameObject(new Coordinates(735, 2530, 104, 106), rockImage);
            //if (rocks.size() == 0) {
                rocks.add(rock);
                dropRock(rock, 2000, diffseconds);
            //}
            rock = new GameObject(new Coordinates(840, 2530, 104, 106), rockImage);
            //if (rocks.size() == 1) {
                rocks.add(rock);
                dropRock(rock, 2100, diffseconds);
            //}
            for(int i = 45; i < 61; i++) {
                for(int j = 0; j < 3; j++) {
                    map.grid[i][158 + j] = 1;
                }
            }
            rocksDropped[1] = true;
        }
        //center section
        else if (!rocksDropped[2] && player.coordinates.topLeftCorner_y < 1800) {
            rock = new GameObject(new Coordinates(180, 1849, 104, 106), rockImage);
            //if (rocks.size() == 2) {
                rocks.add(rock);
                dropRock(rock, 1300, diffseconds);
            //}
            rock = new GameObject(new Coordinates(280, 1840, 104, 106), rockImage);
            //if (rocks.size() == 3) {
                rocks.add(rock);
                dropRock(rock, 1200, diffseconds);
            //}
            rock = new GameObject(new Coordinates(376, 1839, 104, 106), rockImage);
            //if (rocks.size() == 4) {
                rocks.add(rock);
                dropRock(rock, 1220, diffseconds);
            //}
            rock = new GameObject(new Coordinates(580, 1839, 104, 106), rockImage);
            //if (rocks.size() == 5) {
                rocks.add(rock);
                dropRock(rock, 1280, diffseconds);
            //}
            rock = new GameObject(new Coordinates(681, 1839, 104, 106), rockImage);
            //if (rocks.size() == 6) {
                rocks.add(rock);
                dropRock(rock, 1305, diffseconds);
            //}
            rock = new GameObject(new Coordinates(781, 1839, 104, 106), rockImage);
            //if (rocks.size() == 7) {
                rocks.add(rock);
                dropRock(rock, 1212, diffseconds);
            //}
            for(int i = 12; i < 61; i++) {
                for(int j = 0; j < 3; j++) {
                    map.grid[i][115 + j] = 1;
                }
            }
            rocksDropped[2] = true;
        }
        //top left section
        else if (!rocksDropped[3] && !rocksDropped[4] && player.coordinates.topLeftCorner_y < 1580 && player.coordinates.topLeftCorner_x < 650) {
            rock = new GameObject(new Coordinates(350, 1620, 104, 106), rockImage);
            //if (rocks.size() == 8) {
                rocks.add(rock);
                dropRock(rock, 1100, diffseconds);
            //}
            rock = new GameObject(new Coordinates(430, 1620, 104, 106), rockImage);
            //if (rocks.size() == 9) {
                rocks.add(rock);
                dropRock(rock, 1300, diffseconds);
            //}
            for(int i = 17; i < 30; i++) {
                for(int j = 0; j < 3; j++) {
                    map.grid[i][101 + j] = 1;
                }
            }
            rocksDropped[3] = true;
        }
        //top right section
        else if (!rocksDropped[4] && !rocksDropped[3] && player.coordinates.topLeftCorner_y < 1580 && player.coordinates.topLeftCorner_x > 650) {
            rock = new GameObject(new Coordinates(690, 1620, 104, 106), rockImage);
            rocks.add(rock);
            dropRock(rock, 1010, diffseconds);

            for(int i = 30; i < 60; i++) {
                for(int j = 0; j < 3; j++) {
                    map.grid[i][101 + j] = 1;
                }
            }
            rocksDropped[4] = true;
        }
        //boss section
        else if (!rocksDropped[5] && player.coordinates.topLeftCorner_y < 680) {
            rock = new GameObject(new Coordinates(365, 730, 104, 106), rockImage);
            //if (rocks.size() == 10) {
                rocks.add(rock);
                dropRock(rock, 200, diffseconds);
            //}
            rock = new GameObject(new Coordinates(470, 730, 104, 106), rockImage);
            //if (rocks.size() == 11) {
                rocks.add(rock);
                dropRock(rock, 300, diffseconds);
            //}
            rock = new GameObject(new Coordinates(697, 730, 104, 106), rockImage);
            //if (rocks.size() == 12) {
                rocks.add(rock);
                dropRock(rock, 220, diffseconds);
            //}
            rock = new GameObject(new Coordinates(789, 730, 104, 106), rockImage);
            //if (rocks.size() == 13) {
                rocks.add(rock);
                dropRock(rock, 280, diffseconds);
            //}
            for(int i = 10; i < 60; i++) {
                for(int j = 0; j < 3; j++) {
                    map.grid[i][46 + j] = 1;
                }
            }
            rocksDropped[5] = true;
        }

        // ACTIVATE FINAL BOSS
        if (player.coordinates.topLeftCorner_y < 680 && player.coordinates.topLeftCorner_y > 630 && this.bossSpawned == false) {
            try {
                this.bossSpawned = true;
                BufferedImage floorSymbol = ImageIO.read(getClass().getResourceAsStream("/sprites/maps/cavern_floor_fight.png"));
                objects.get(0).image = floorSymbol;

            } catch (Exception e) {
                System.out.println("Couldn't load floor symbol: " + "\tReason: " + e.getCause());
            }
            try {
                enemies.removeAll(enemies);
                this.enemies.add(new BossOctopus(400, 200));

            } catch (Exception e) {
                System.out.println("Couldn't spawn boss: " + "\tReason: " + e.getCause());
            }
        }

//        for (Bullet playerBullet : playerBullets) {
//            if (playerBullet.coordinates.intersects()) {
//                enemy.health -= playerBullet.damage;
//                playerBullet.coordinates = new Coordinates(0, 0, 0, 0);
//            }
//        }
    }

    private void dropRock(GameObject rock, double startY, double diffseconds) {

//        final GameObject[] rockToDrop = new GameObject[1];
        final double x = rock.coordinates.topLeftCorner_x;
        final double finalY = rock.coordinates.topLeftCorner_y;
        rock.coordinates.topLeftCorner_y = startY;
        Timer timer = new Timer(1, new ActionListener() {
            double dy = startY;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (dy < finalY) {
                    rock.coordinates.moveY(1);
//                    rockToDrop[0] = new GameObject(new Coordinates(x, dy, 104, 106), rock.image);
                    dy += diffseconds * 100;
                    rock.coordinates.topLeftCorner_y = dy;
//                    panel.draw(rockToDrop[0]);
                } else if (dy >= finalY) {
                    rock.coordinates.topLeftCorner_y = finalY;
                    ((Timer) e.getSource()).stop();
                    return;
                }
            }
        });

        timer.start();

        if (!timer.isRunning())
            rocks.add(rock);

    }


    private void bossKilled() {

        System.out.println("Boss killed");

        //drops car key
        //if (stacksOnWorld.size() == 0)
            stacksOnWorld.add(new ItemStack(
                    new CarKey(
                            new Coordinates(475, 440, 48, 29),
                            Gameplay.ship
                    ), 1, false));

        try {
            BufferedImage floorSymbol = ImageIO.read(getClass().getResourceAsStream("/sprites/maps/cavern_floor_win.png"));
            objects.get(0).image = floorSymbol;
            Iterator<Quest> questIter = player.quests.iterator();
            while (questIter.hasNext()) {
                Quest quest = questIter.next();
                if (quest != null && quest.iD == 1) {
                    quest.completed = true;
                }
            }

        } catch (Exception e) {
            System.out.println("Couldn't load floor symbol: " + "\tReason: " + e.getCause());
        }

    }

    public static void victory() {
        gameState = GameState.WIN;

        String victoryTextUp = "Congratulations!";
        String victoryTextDown = "You have made it! You have found the car key and can now escape this planet! " +
                "You have won the game!";

        victoryFailPanel.setTopText(victoryTextUp);
        victoryFailPanel.setBottomText(victoryTextDown);
        victoryFailPanel.setVisible(true);
    }

    public void fail() {
        gameState = GameState.GAMEOVER;

        String failTextUp = "Oh no!";
        String failTextDown = "You have failed to complete the game. Try again!";

        victoryFailPanel.setTopText(failTextUp);
        victoryFailPanel.setBottomText(failTextDown);
        victoryFailPanel.setVisible(true);
    }

    private String getInitialText() {
        return "\n My peaceful interstellar journey has been abruptly interrupted by a sudden ambush, " +
                "throwing me off course and leaving me on this deserted asteroid. The ship seems to have " +
                "remained intact, but there is no sign of the access card. How will I get out of this place?" + "\n" + "\n" +
                "Instructions: " + "\n" + "\n" +
                "You can move around using the arrow or the WASD keys and shoot using the space bar. " + "\n" + "\n" +
                "You can change your weapon by pressing the Q key, open your inventory with I, pause the game " +
                "using P or Enter and also interact with the NPC, by getting close to it." + "\n" + "\n" +
                "Good luck!";
    }

    private void resetRocks(){
        for(int i = 0; i< 6; i++){
            rocksDropped[i] = false;
        }
    }
}

