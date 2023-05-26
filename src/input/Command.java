package input;

public interface Command {
    void execute(Keys key);
    void execute(Keys key, int mouseX, int mouseY);
}
