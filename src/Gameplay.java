import java.util.Set;

import game.entities.Player;
import gui.GamePanel;
import gui.Panel;
import input.Command;
import input.KeyHandler;
import input.Keys;

public class Gameplay {

    int pos_x, pos_y;

    final GamePanel panel;
    final KeyHandler keyHandler;

    private Player player;


    public Gameplay(Panel panel, KeyHandler keyHandler) {
        this.panel = (GamePanel) panel;
        this.keyHandler = keyHandler;
    }

    public void init() {
        player = new Player();
    }

    public void run() {

        long lastTick = System.currentTimeMillis();

        while (true) {
            long currentTick = System.currentTimeMillis();
            double diffSeconds = (currentTick - lastTick) / 100.0;
            lastTick = currentTick;


//			try {
//				handleUserInput();
//			} catch (Exception e) {
//				System.out.println(e.getCause());
//			}
//
			update(diffSeconds);

			panel.clear();
			drawElements();
			panel.redraw();
			System.out.flush();

        }
    }

	private void update(double diffSeconds) {
		player.move(diffSeconds);
	}

    private void drawElements() {
		panel.draw(player);

    }

    private void handleUserInput() {
        final Set<Keys> pressedKeys = keyHandler.getKeys();
		final Set<Keys> pressedMouseButtons = keyHandler.getMouseButtons();

		for (Keys keyCode : pressedKeys) {
			Command command = keyCode.getCommand();
			if (command != null) {
				command.execute(keyCode);
			}
		}

		for (Keys mouseCode : pressedMouseButtons) {
			Command command = mouseCode.getCommand();
			if (command != null) {
				command.execute(mouseCode, keyHandler.mouseX, keyHandler.mouseY);
			}
		}
	}
}
