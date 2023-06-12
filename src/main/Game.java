
package main;
import gui.*;
import input.KeyHandler;

import javax.swing.*;

public class Game {
	final private GameFrame frame;
	final private KeyHandler keyHandler;

	private Gameplay gameplay;
	private Panel panel;

	public Game() {
		frame = new GameFrame("Simperia");
		keyHandler = new KeyHandler();
		frame.addKeyListener(keyHandler);
	}

	public void init() {
		//panel = new HomePanel();
//		frame.setPanel(panel);

		//JButton newGameButton = ((HomePanel) panel).getNewGameButton();
		//newGameButton.addActionListener(e -> { start();	});
	}

	public void start() {
		panel = new GamePanel();
		panel.requestFocusInWindow();
		frame.setPanel(panel);

		gameplay = new Gameplay(panel, keyHandler, frame);

		gameplay.init();
		gameplay.run();
	}
}
