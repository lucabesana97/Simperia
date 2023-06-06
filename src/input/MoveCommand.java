package input;

import main.Gameplay;
import objState.MovingState;

public class MoveCommand implements Command{

    @Override
    public void execute(Keys key, boolean pressed) {
        if (pressed) {
            System.out.println("Move command executed with key: " + key);
            switch (key) {
                case UP:
                    Gameplay.player.yState = MovingState.UP;
                    break;
                case DOWN:
                    Gameplay.player.yState = MovingState.DOWN;
                    break;
                case LEFT:
                    Gameplay.player.xState = MovingState.LEFT;
                    break;
                case RIGHT:
                    Gameplay.player.xState = MovingState.RIGHT;
                    break;
                default:
                    break;
            }
            System.out.println(Gameplay.player.yState);
        } else {
            switch (key) {
                case UP:
                case DOWN:
                    Gameplay.player.yState = MovingState.STILL;
                    break;
                case LEFT:
                case RIGHT:
                    Gameplay.player.xState = MovingState.STILL;
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void execute(Keys key, int mouseX, int mouseY) {

    }
}
