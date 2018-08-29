package de.syscy.kageban.common.command.exception;

public class CommandNotFoundException extends CommandException {
	private static final long serialVersionUID = 1L;

	public CommandNotFoundException(String commandName) {
		super("Command not found: \"%s\"", commandName);
	}
}