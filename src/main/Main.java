package main;
import javax.swing.*;

public class Main {
	public static void main(String[] args) {
		Game game = new Game();
		game.init();
//		SwingUtilities.invokeLater(() -> {
//
//		});
		game.start();
	}
}
