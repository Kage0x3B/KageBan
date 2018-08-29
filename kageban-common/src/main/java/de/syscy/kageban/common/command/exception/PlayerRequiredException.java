package de.syscy.kageban.common.command.exception;

public class PlayerRequiredException extends CommandException {
	private static final long serialVersionUID = 1L;

	public PlayerRequiredException() {
		super("You need to be a player do use this command");
	}
}