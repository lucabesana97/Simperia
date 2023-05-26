package input;

public class MenuCommand implements Command{
    @Override
    public void execute(Keys key) {
        System.out.println("menu command executed with key: " + key);
    }

    @Override
    public void execute(Keys key, int mouseX, int mouseY) {

    }
}
