package main;

import gameplay.GamePlay;
import gui.GameFrame;
import gui.GamePanel;
import io.KeyHandler;

public class Game {
	final private GameFrame gameFrame;
	final private KeyHandler keyHandler;

	private GamePlay play;
	private GamePanel panel;

	public Game() {
		gameFrame = new GameFrame();
		keyHandler = new KeyHandler();
		gameFrame.addKeyListener(keyHandler);
	}

	public void init() {
		panel = new GamePanel();
		gameFrame.setPanel(panel);
	}

	public void start() {
		panel = new GamePanel();
		play = new GamePlay(panel, keyHandler);
		gameFrame.setPanel(panel);

		play.init();
		play.run();
	}
}
