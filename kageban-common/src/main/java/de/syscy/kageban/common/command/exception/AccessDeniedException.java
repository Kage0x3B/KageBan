package de.syscy.kageban.common.command.exception;

public class AccessDeniedException extends CommandException {
	private static final long serialVersionUID = 1L;

	public AccessDeniedException() {
		super("You do not have permission to use this command");
	}
}