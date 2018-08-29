package de.syscy.kageban.common.command.exception;

public class InvalidCommandArgumentException extends CommandException {
	private static final long serialVersionUID = 1L;

	public InvalidCommandArgumentException(String argName) {
		super("The \"%s\" argument is invalid", argName);
	}
}