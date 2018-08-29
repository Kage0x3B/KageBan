package de.syscy.kageban.common.commands;

import de.syscy.kageban.common.KageBanPlugin;
import de.syscy.kageban.common.command.CommandManager;
import de.syscy.kageban.common.punishment.PunishmentType;

public class Commands {
	public static CommandManager createCommandManager(KageBanPlugin plugin) {
		CommandManager commandManager = new CommandManager(plugin, "kb");
		registerCommands(commandManager, plugin);

		return commandManager;
	}

	private static void registerCommands(CommandManager commandManager, KageBanPlugin plugin) {
		commandManager.addCommand(new ApplyPunishmentCommand(plugin, "ban", PunishmentType.BAN, true));
		commandManager.addCommand(new ApplyPunishmentCommand(plugin, "mute", PunishmentType.MUTE, true));
		commandManager.addCommand(new ApplyPunishmentCommand(plugin, "warn", PunishmentType.WARNING, false));
		commandManager.addCommand(new ApplyPunishmentCommand(plugin, "kick", PunishmentType.KICK, false));

		commandManager.addCommand(new RemovePunishmentCommand(plugin, "unban", PunishmentType.BAN));
		commandManager.addCommand(new RemovePunishmentCommand(plugin, "unmute", PunishmentType.MUTE));
	}
}