package de.syscy.kageban.common.command.exception;

public class ExemptException extends CommandException {
	private static final long serialVersionUID = 1L;

	public ExemptException(String playerName) {
		super("%s is exempt from this command", playerName);
	}
}