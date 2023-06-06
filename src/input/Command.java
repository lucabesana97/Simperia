package input;

import game.entities.Player;

public interface Command {
    void execute(Keys key, boolean pressed);
    void execute(Keys key, int mouseX, int mouseY);
}
