import gui.GameFrame;
import gui.GamePanel;
import input.KeyHandler;

public class Game {
	final private GameFrame gameFrame;
	final private KeyHandler keyHandler;

	private Gameplay play;
	private GamePanel panel;

	public Game() {
		gameFrame = new GameFrame();
		keyHandler = new KeyHandler();
		gameFrame.addKeyListener(keyHandler);
	}

	public void init() {
		panel = new GamePanel();
		gameFrame.setPanel(panel);
		try {
			Thread.sleep(1000);
		}catch(Exception e){

		}
	}

	public void start() {
		panel = new GamePanel();
		play = new Gameplay(panel, keyHandler);
		gameFrame.setPanel(panel);

		play.init();
		play.run();
	}
}
