
package main;
import gui.GameFrame;
import gui.GamePanel;
import gui.PausePanel;
import input.KeyHandler;

import javax.swing.*;

public class Game {
	final private GameFrame frame;
	final private KeyHandler keyHandler;

	private Gameplay gameplay;
	private GamePanel panel;
	PausePanel pausePanel;

	public Game() {
		frame = new GameFrame("Simperia");
		keyHandler = new KeyHandler();
		frame.addKeyListener(keyHandler);
	}

	public void init() {
	}

	public void start() {
		panel = new GamePanel();
		gameplay = new Gameplay(panel, keyHandler, frame);

//		pausePanel = new PausePanel();
//		panel.add(pausePanel, JLayeredPane.PALETTE_LAYER);

		frame.setPanel(panel);

		gameplay.init();
		gameplay.run();
	}
}
