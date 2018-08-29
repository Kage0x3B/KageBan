package de.syscy.kageban.common.command;

import de.syscy.kageban.common.KageBanPlugin;
import de.syscy.kageban.common.command.argument.CommandArgument;
import de.syscy.kageban.common.command.argument.CommandArguments;
import de.syscy.kageban.common.sender.Sender;
import de.syscy.kageban.common.util.Joiner;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public abstract class CommandBase {
	protected final KageBanPlugin plugin;
	protected @Getter String command;

	protected @Getter CommandArguments arguments = new CommandArguments(this);

	protected @Getter @Setter(value = AccessLevel.PROTECTED) CommandManager commandManager;

	public CommandBase(KageBanPlugin plugin, String command, CommandArgument... commandArguments) {
		this.plugin = plugin;
		this.command = command;

		if(commandArguments.length > 0) {
			for(CommandArgument commandArgument : commandArguments) {
				arguments.addCommandArgument(commandArgument);
			}
		}
	}

	public abstract void onCommand(Sender sender);

	public boolean onCommand(Sender sender, String command, String[] args) {
		arguments.update(sender, args);
		onCommand(sender);

		return true;
	}

	public List<String> onTabComplete(Sender sender, String command, String[] args) {
		return arguments.onTabComplete(sender, args);
	}

	public boolean isAuthorized(Sender sender) {
		return sender.hasPermission(commandManager.getPermissionPrefix() + "." + getCommand().trim());
	}

	public boolean isExempt(Sender sender) {
		return sender.hasPermission(commandManager.getPermissionPrefix() + "." + getCommand().trim() + ".exempt");
	}

	protected boolean isValidPlayer(Sender player) {
		return player != null && player.isValid();
	}

	public String getUsageString() {
		List<String> usageArguments = new ArrayList<>();

		for(CommandArgument<?> arg : arguments.getCommandArguments()) {
			usageArguments.add(arg.getName());
		}

		return Joiner.on(", ").join(usageArguments);
	}
}