import javax.swing.*;

public class Main {
	public static void main(String[] args) {
		Game game = new Game();
		SwingUtilities.invokeLater(() -> {
			game.init();
		});
		game.start();

//		Game game = new Game();
//		game.init();
//		game.start();

	}

}
