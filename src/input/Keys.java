package input;

public enum Keys {
	DOWN(new MoveCommand()),
	LEFT(new MoveCommand()),
	RIGHT(new MoveCommand()),
	UP(new MoveCommand()),
	PAUSE(new MenuCommand()),
	MOUSE_BUTTON1(new MouseCommand()),
	MOUSE_BUTTON2(new MouseCommand()),
	MOUSE_BUTTON3(new MouseCommand()),
	NONE(new MenuCommand())
	;
	// Add other keys and corresponding commands

	private Command command;

	Keys(Command command) {
		this.command = command;
	}

	public Command getCommand() {
		return command;
	}
}