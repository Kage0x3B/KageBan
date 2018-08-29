package de.syscy.kageban.common.commands;

import de.syscy.kageban.common.KageBanPlugin;
import de.syscy.kageban.common.command.CommandBase;
import de.syscy.kageban.common.command.argument.CommandArgument;
import de.syscy.kageban.common.command.argument.SenderArgument;
import de.syscy.kageban.common.punishment.PunishmentType;
import de.syscy.kageban.common.sender.Sender;

import java.util.ArrayList;
import java.util.List;

public class RemovePunishmentCommand extends CommandBase {
	private PunishmentType punishmentType;

	public RemovePunishmentCommand(KageBanPlugin plugin, String commandName, PunishmentType punishmentType) {
		super(plugin, commandName, buildArgs());

		this.punishmentType = punishmentType;
	}

	private static CommandArgument[] buildArgs() {
		List<CommandArgument> args = new ArrayList<>();

		args.add(SenderArgument.create("player").build());

		return args.toArray(new CommandArgument[0]);
	}

	@Override
	public void onCommand(Sender sender) {
		Sender receivingPlayer = arguments.getSender("player");
		plugin.getPunishmentManager().discardData(receivingPlayer.getId());

		sender.sendMessage("[WIP] Removed all punishments for " + sender.getUsername());
	}
}