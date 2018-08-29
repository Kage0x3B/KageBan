package de.syscy.kageban.common.command.exception;

import de.syscy.kageban.common.command.CommandBase;

public class InvalidUsageException extends CommandException {
	private static final long serialVersionUID = 1L;

	public InvalidUsageException(CommandBase command) {
		this(command.getCommandManager().getCommand(), command.getCommand(), command.getUsageString());
	}

	public InvalidUsageException(String mainCommandName, String commandName, String usage) {
		super("Invalid usage! Use: %s %s %s", mainCommandName, commandName, usage);
	}
}