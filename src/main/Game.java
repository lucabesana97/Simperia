package main;

import gui.*;
import input.KeyHandler;

public class Game {
	final private GameFrame frame;
	final private KeyHandler keyHandler;
	private HomePanel homePanel;
	private Gameplay gameplay;
	private Panel panel;

	public Game() {
		this.frame = new GameFrame("Simperia");
		this.keyHandler = new KeyHandler();
		this.frame.addKeyListener(keyHandler);
	}

	public void init() {
	}

	public void start() {
		this.panel = new GamePanel();
		this.panel.setFocusable(true);

		frame.setPanel(panel);
		this.panel.requestFocusInWindow();

		gameplay = new Gameplay(panel, keyHandler, frame);
		//gameplay.init();
		//gameplay.run();
	}
}
