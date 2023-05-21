package io;

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
		case KeyEvent.VK_W:
		case KeyEvent.VK_UP:
			returner = Keys.JUMP;
			break;
		case KeyEvent.VK_S:
		case KeyEvent.VK_DOWN:
			returner = Keys.DOWN;
			break;
		case KeyEvent.VK_A:
		case KeyEvent.VK_LEFT:
			returner = Keys.LEFT;
			break;
		case KeyEvent.VK_D:
		case KeyEvent.VK_RIGHT:
			returner = Keys.RIGHT;
			break;
		case KeyEvent.VK_SPACE:
			returner = Keys.JUMP;
			break;
		case KeyEvent.VK_ENTER:
			returner = Keys.PAUSE;
			break;
		case KeyEvent.VK_P:
			returner = Keys.SHOOT;
			break;
		default:
			break;
		}

		return returner;
	}

	public Set<Keys> getKeys() {
		return pressedKeys;
	}
}
