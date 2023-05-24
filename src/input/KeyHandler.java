package input;

import input.Keys;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class KeyHandler implements KeyListener {

	private Set<Keys> pressedKeys = new HashSet<>();

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		Keys key = toKey(keyCode);
		if (key != null) {
			pressedKeys.add(key);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		Keys key = toKey(keyCode);
		if (key != null) {
			pressedKeys.remove(key);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// No action needed for keyTyped
	}

	private Keys toKey(int keyCode) {
		switch (keyCode) {
			case KeyEvent.VK_W:
			case KeyEvent.VK_UP:
				return Keys.UP;
			case KeyEvent.VK_S:
			case KeyEvent.VK_DOWN:
				return Keys.DOWN;
			case KeyEvent.VK_A:
			case KeyEvent.VK_LEFT:
				return Keys.LEFT;
			case KeyEvent.VK_D:
			case KeyEvent.VK_RIGHT:
				return Keys.RIGHT;
			case KeyEvent.VK_SPACE:
				return Keys.UP;
			case KeyEvent.VK_ENTER:
				return Keys.PAUSE;
			case KeyEvent.VK_P:
				return Keys.PAUSE;
			default:
				return null;
		}
	}

	public Set<Keys> getKeys() {
		return pressedKeys;
	}
}
