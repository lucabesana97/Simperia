package input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedHashSet;
import java.util.Set;

public class KeyHandler implements KeyListener {

	private Set<Keys> pressedKeys = new LinkedHashSet<>();

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		Keys tmp = toKey(keyCode);
		if (tmp != null) {
			pressedKeys.add(tmp);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		Keys tmp = toKey(keyCode);
		if (tmp != null) {
			pressedKeys.remove(tmp);
		}
	}

	private Keys toKey(int n) {
		Keys returner = null;

		switch (n) {
			case KeyEvent.VK_W, KeyEvent.VK_UP -> returner = Keys.UP;
			case KeyEvent.VK_S, KeyEvent.VK_DOWN -> returner = Keys.DOWN;
			case KeyEvent.VK_A, KeyEvent.VK_LEFT -> returner = Keys.LEFT;
			case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> returner = Keys.RIGHT;
			case KeyEvent.VK_SPACE -> returner = Keys.ATTACK;
			case KeyEvent.VK_ENTER, KeyEvent.VK_P -> returner = Keys.PAUSE;
			case KeyEvent.VK_Q -> returner = Keys.SWITCH;
			case KeyEvent.VK_SHIFT -> returner = Keys.SPRINT;
			case KeyEvent.VK_I -> returner = Keys.INVENTORY;
			default -> {
			}
		}
		return returner;
	}

	public Set<Keys> getKeys() {
		return pressedKeys;
	}
}
