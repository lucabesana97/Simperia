
package main;
import gui.GameFrame;
import gui.GamePanel;
import gui.Panel;
import input.KeyHandler;

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
	}

	public void start() {
		panel = new GamePanel();
		gameplay = new Gameplay(panel, keyHandler);

		frame.setPanel(panel);

		gameplay.init();
		gameplay.run();
	}
}
