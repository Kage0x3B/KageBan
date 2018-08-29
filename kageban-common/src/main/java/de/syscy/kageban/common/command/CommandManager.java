package de.syscy.kageban.common.command;

import de.syscy.kageban.common.KageBanPlugin;
import de.syscy.kageban.common.command.exception.AccessDeniedException;
import de.syscy.kageban.common.command.exception.CommandException;
import de.syscy.kageban.common.command.exception.CommandNotFoundException;
import de.syscy.kageban.common.command.exception.InvalidUsageException;
import de.syscy.kageban.common.sender.Sender;
import de.syscy.kageban.common.util.Util;

import java.util.ArrayList;
import java.util.List;

public class CommandManager extends CommandBase {
	private static final int COMMANDS_PER_HELP_PAGE = 6;

	private List<CommandBase> commands = new ArrayList<>();


	public CommandManager(KageBanPlugin plugin, String command) {
		super(plugin, command);

		commandManager = this;
	}

	@Override
	public final void onCommand(Sender sender) {
		String[] args = arguments.getCurrentArgs();

		try {
			if(args.length == 0) {
				if(sender.hasPermission(getPermissionPrefix() + ".help")) {
					sendHelpPage(sender, 1);

					return;
				} else {
					throw new AccessDeniedException();
				}
			}

			if(args[0].equalsIgnoreCase("help")) {
				if(!sender.hasPermission(getPermissionPrefix() + ".help")) {
					throw new AccessDeniedException();
				}

				if(args.length > 2) {
					throw new InvalidUsageException(getFullCommand(), "help", "[page|command name]");
				}

				if(args.length == 1) {
					sendHelpPage(sender, 1);

					return;
				} else {
					if(Util.isNumber(args[1])) {
						int page = Integer.parseInt(args[1]);

						if(page > 0) {
							sendHelpPage(sender, page);
						} else {
							sendHelpPage(sender, 1);
						}

						return;
					}

					CommandBase command = getCommand(args[1]);

					if(command == null) {
						throw new CommandNotFoundException(args[1]);
					}

					if(command.isAuthorized(sender)) {
						sender.sendMessage("Usage: " + getFullCommand() + " " + command.getCommand() + " " + command.getUsageString());
					}
				}

				return;
			}

			CommandBase command = getCommand(args[0]);

			if(command == null) {
				throw new CommandNotFoundException(args[0]);
			}

			if(!command.isAuthorized(sender)) {
				throw new AccessDeniedException();
			}

			String[] cmdArgs = new String[args.length - 1];

			if(args.length > 1) {
				System.arraycopy(args, 1, cmdArgs, 0, cmdArgs.length);
			}

			command.getArguments().update(sender, cmdArgs);
			command.onCommand(sender);
		} catch(CommandException ex) {
			sender.sendMessage(ex.getClass().getSimpleName() + ": " + String.format(ex.getMessage(), ex.getArgs()));
		} catch(Exception ex) {
			sender.sendMessage(ex.getClass().getPackage() + "." + ex.getClass().getName() + ": " + ex.getMessage());
		}
	}

	public void addCommand(CommandBase command) {
		commands.add(command);
		command.setCommandManager(this);
	}

	public void removeCommand(String commandName) {
		CommandBase commandInstance = null;

		for(CommandBase command : commands) {
			if(command.getCommand().equalsIgnoreCase(commandName)) {
				commandInstance = command;

				break;
			}
		}

		if(commandInstance != null) {
			commands.remove(commandInstance);
		}
	}

	private void sendHelpPage(Sender sender, int page) {
		List<CommandBase> availableCommands = getAvailableCommands(sender);

		int size = availableCommands.size();
		int totalPages = size / COMMANDS_PER_HELP_PAGE;

		if(size - COMMANDS_PER_HELP_PAGE * totalPages > 0) {
			totalPages += 1;
		}

		if(page < 1 || page > totalPages) {
			page = 1;
		}

		sender.sendMessage("=== " + getFullCommand() + " Help ===");

		int startIndex = (page - 1) * COMMANDS_PER_HELP_PAGE;
		int endIndex = COMMANDS_PER_HELP_PAGE * page;

		for(int i = startIndex; i < endIndex; i++) {
			if(availableCommands.size() <= i) {
				break;
			}

			sender.sendMessage("/" + getFullCommand() + " " + availableCommands.get(i).getCommand());
		}

		sender.sendMessage("=== Page " + page + "/" + totalPages + " ===");
	}

	public CommandBase getCommand(String commandName) {
		for(CommandBase command : commands) {
			if(command.getCommand().equalsIgnoreCase(commandName)) {
				return command;
			}
		}

		return null;
	}

	@Override
	public final List<String> onTabComplete(Sender sender, String command, String[] args) {
		if(args.length == 1) {
			List<String> allCommands = new ArrayList<>();

			for(CommandBase commandBase : commands) {
				if(commandBase.getCommand().toLowerCase().startsWith(args[0].toLowerCase()) && commandBase.isAuthorized(sender)) {
					allCommands.add(commandBase.getCommand());
				}
			}

			return allCommands;
		} else if(args.length > 1) {
			CommandBase commandBase = getCommand(args[0]);

			if(commandBase != null && commandBase.isAuthorized(sender)) {
				String[] subArgs = new String[args.length - 1];
				System.arraycopy(args, 1, subArgs, 0, subArgs.length);

				return commandBase.onTabComplete(sender, command, subArgs);
			}
		}

		return null;
	}

	private List<CommandBase> getAvailableCommands(Sender sender) {
		List<CommandBase> availableCommands = new ArrayList<>();

		for(CommandBase command : commands) {
			if(command.isAuthorized(sender)) {
				availableCommands.add(command);
			}
		}

		return availableCommands;
	}

	public String getHelpDescriptionPrefix() {
		return (commandManager != null && !commandManager.equals(this) ? commandManager.getHelpDescriptionPrefix() + "." : "") + command;
	}

	public String getPermissionPrefix() {
		return getFullCommand().replaceAll(" ", ".");
	}

	public String getFullCommand() {
		return commandManager != null && !commandManager.equals(this) ? commandManager.getPermissionPrefix() + " " + command : command;
	}
}