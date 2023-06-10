
package main;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.swing.JButton;

import game.Coordinates;
import game.entities.*;
import game.environment.GameMap;
import game.inventory.HealthElixir;
import game.inventory.Inventory;
import gui.GamePanel;
import gui.InventoryPanel;
import gui.Panel;
import gui.PausePanel;
import input.Command;
import input.KeyHandler;
import input.Keys;
import objState.EnemyState;
import objState.MovingState;

public class Gameplay {

    int pos_x, pos_y;

    final GamePanel panel;
    final KeyHandler keyHandler;

    public static Player player;
	public static Inventory inventory;
	public static GameMap map;
	private NPC npc;
	private HealthElixir healthElixir;
	private List<Enemy> enemies = new ArrayList<>();
	private PausePanel pausePanel;
	private InventoryPanel inventoryPanel;
	private JButton pauseButton;
	private JButton inventoryButton;
	private boolean paused;
	private List<Warp> warps = new ArrayList<>();


    public Gameplay(Panel panel, KeyHandler keyHandler) {
        this.panel = (GamePanel) panel;
        this.keyHandler = keyHandler;
		paused = false;
    }



    public void init() {

		//Don't change the order
		map = new GameMap();
		player = new Player();
		map.init(player);


		inventory = new Inventory();
		healthElixir = new HealthElixir(false, 1, new Coordinates(1000, 1000, 32, 32));
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
		enemies.add(new Octopus());
		warps.add(new Warp(new Coordinates(1000,500,32,32), new Coordinates(1400,600,32,32), player));
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

		player.move(diffSeconds);
		Iterator<Enemy> enemyIter = enemies.iterator();
		while (enemyIter.hasNext()) {
			Enemy enemy = enemyIter.next();
			if (enemy != null && enemy.enemyState == EnemyState.DEAD){
				enemyIter.remove();
			}
		}
		for (Enemy enemy : enemies) {
			enemy.move(diffSeconds, player);
		}
		Iterator<Warp> warpIter = warps.iterator();
		while (warpIter.hasNext()) {
			Warp warp = warpIter.next();
			if (warp != null){
				if(player.isColliding(warp)) {
					player.teleport(warp);
				}
			}
		}
		System.gc();
	}

    private void drawElements() {

		panel.draw(map);
		for(Enemy enemy : enemies) {
			panel.draw(enemy);
		}
		for(Warp warp: warps){
			panel.draw(warp);
		}
		panel.draw(healthElixir);
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
			for (Keys keyCode : pressedKeys) {
				switch (keyCode) {
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
					case SHOOT:
						break;
					default:
						break;
				}
			}
			System.out.println(player.yState + "\t" + player.xState);
			if (horStill) {
				player.xState = MovingState.STILL;
			}
			if (verStill){
				player.yState = MovingState.STILL;
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

}
