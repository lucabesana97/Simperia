import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.List;
import java.util.Set;

import gui.GamePanel;
import input.KeyHandler;
import input.Keys;

public class Gameplay {

	int pos_x, pos_y;

	final GamePanel panel;
	final KeyHandler keyHandler;
	private BufferedImage backBuffer;


	public Gameplay(GamePanel panel, KeyHandler keyHandler) {
		this.panel = (GamePanel) panel;
		this.keyHandler = keyHandler;
	}

	public void init() {
		backBuffer = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_ARGB);
	}

	public void run() {

		long lastTick = System.currentTimeMillis();

		while (true) {
			long currentTick = System.currentTimeMillis();
			double diffSeconds = (currentTick - lastTick) / 100.0;
			lastTick = currentTick;

			panel.clear();

			try {
				handleUserInput();
			} catch (Exception e) {
				System.out.println(e.getCause());
			}

			drawElements();

			update(diffSeconds);

			panel.redraw();
			System.out.flush();

			panel.getGraphics().drawImage(backBuffer, 0, 0, null);
		}
	}

	private void update(double diffSeconds) {

	}

	private void drawElements() {
		Graphics g = (Graphics) backBuffer.getGraphics();
		g.dispose();
	}

	private void handleUserInput() {

		final Set<Keys> pressedKeys = keyHandler.getKeys();

		for (Keys keyCode : pressedKeys) {
			switch (keyCode) {

			}
		}

	}
}
