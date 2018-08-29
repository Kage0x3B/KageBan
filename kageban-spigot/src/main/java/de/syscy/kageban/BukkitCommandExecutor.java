package de.syscy.kageban;

import de.syscy.kageban.common.command.CommandManager;
import de.syscy.kageban.common.sender.Sender;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.List;

@RequiredArgsConstructor
public class BukkitCommandExecutor implements CommandExecutor, TabExecutor {
	private final KageBanSpigot bootstrap;
	private final CommandManager commandManager;

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Sender kbSender = bootstrap.getSenderFactory().wrapSender(sender);

		return commandManager.onCommand(kbSender, command.getName(), args);
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		Sender kbSender = bootstrap.getSenderFactory().wrapSender(sender);

		return commandManager.onTabComplete(kbSender, command.getName(), args);
	}
}