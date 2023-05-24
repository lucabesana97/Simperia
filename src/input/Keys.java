package input;

public enum Keys {
	DOWN(new MoveCommand()),
	LEFT(new MoveCommand()),
	RIGHT(new MoveCommand()),
	UP(new MoveCommand()),
	PAUSE(new MenuCommand()),
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