package input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class KeyHandler implements KeyListener, MouseListener {

	private Set<Keys> pressedKeys = new HashSet<>();
	private Set<Keys> pressedMouseButtons = new HashSet<>();
	public int mouseX;
	public int mouseY;
	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		Keys key = toKey(keyCode);
		if (key != null) {
			key.getCommand().execute(key, true);
			//pressedKeys.add(key);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		Keys key = toKey(keyCode);
		if (key != null) {
			key.getCommand().execute(key, false);
			//pressedKeys.remove(key);
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
	@Override
	public void mouseClicked(MouseEvent e) {
		// No action needed for mouseClicked
	}

	@Override
	public void mousePressed(MouseEvent e) {
		int button = e.getButton();
		Keys key = toMouseButton(button);
		if (key != null) {
			pressedMouseButtons.add(key);
		}
		mouseX = e.getX(); // Store mouse X position
		mouseY = e.getY(); // Store mouse Y position
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		int button = e.getButton();
		Keys key = toMouseButton(button);
		if (key != null) {
			pressedMouseButtons.remove(key);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// No action needed for mouseEntered
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// No action needed for mouseExited
	}

	private Keys toMouseButton(int button) {
		switch (button) {
			case MouseEvent.BUTTON1:
				return Keys.MOUSE_BUTTON1;
			case MouseEvent.BUTTON2:
				return Keys.MOUSE_BUTTON2;
			case MouseEvent.BUTTON3:
				return Keys.MOUSE_BUTTON3;
			default:
				return null;
		}
	}

	public Set<Keys> getMouseButtons() {
		return pressedMouseButtons;
	}
}
