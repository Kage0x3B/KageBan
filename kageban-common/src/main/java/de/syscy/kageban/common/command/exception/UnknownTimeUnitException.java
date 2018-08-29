package de.syscy.kageban.common.command.exception;

public class UnknownTimeUnitException extends CommandException {
	private static final long serialVersionUID = 1L;

	public UnknownTimeUnitException(String timeUnit) {
		super("Invalid time unit: \"%s\"", timeUnit);
	}
}