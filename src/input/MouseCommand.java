package input;

public class MouseCommand implements Command{
    @Override
    public void execute(Keys key, int mouseX, int mouseY) {
        System.out.println("Mouse command executed with key: " + key);
        System.out.println("Mouse X position: " + mouseX);
        System.out.println("Mouse Y position: " + mouseY);
    }

    @Override
    public void execute(Keys key, boolean pressed) {

    }
}
