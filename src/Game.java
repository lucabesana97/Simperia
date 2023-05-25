import gui.Frame;
import gui.GamePanel;
import gui.APanel;
import input.KeyHandler;

public class Game {
	final private Frame frame;
	final private KeyHandler keyHandler;

	private Gameplay gameplay;
	private APanel panel;

	public Game() {
		frame = new Frame("Simperia");
		keyHandler = new KeyHandler();
		frame.addKeyListener(keyHandler);
	}

	public void init() {
//		panel = new HomePanel();
//		frame.setPanel(panel);
	}

	public void start() {
		panel = new GamePanel();
		gameplay = new Gameplay(panel, keyHandler);

		frame.setPanel(panel);

		gameplay.init();
		gameplay.run();
	}
}
