package input;

public class MoveCommand implements Command{

    @Override
    public void execute(Keys key) {
        System.out.println("Move command executed with key: " + key);
    }

    @Override
    public void execute(Keys key, int mouseX, int mouseY) {

    }
}
