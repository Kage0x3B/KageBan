package de.syscy.kageban.common.commands;

import de.syscy.kageban.common.KageBanPlugin;
import de.syscy.kageban.common.command.CommandBase;
import de.syscy.kageban.common.command.argument.CommandArgument;
import de.syscy.kageban.common.command.argument.SenderArgument;
import de.syscy.kageban.common.command.argument.StringListArgument;
import de.syscy.kageban.common.command.argument.TimeArgument;
import de.syscy.kageban.common.punishment.Punishment;
import de.syscy.kageban.common.punishment.PunishmentType;
import de.syscy.kageban.common.sender.Sender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ApplyPunishmentCommand extends CommandBase {
	private PunishmentType punishmentType;

	public ApplyPunishmentCommand(KageBanPlugin plugin, String commandName, PunishmentType punishmentType, boolean hasLength) {
		super(plugin, commandName, buildArgs(hasLength));

		this.punishmentType = punishmentType;
	}

	private static CommandArgument[] buildArgs(boolean hasLength) {
		List<CommandArgument> args = new ArrayList<>();

		args.add(SenderArgument.create("player").build());
		if(hasLength) {
			args.add(TimeArgument.create("length").defaultValue(Long.MAX_VALUE).notRequired().build());
		}
		args.add(StringListArgument.create("reason").defaultValue(Collections.emptyList()).notRequired().build());

		return args.toArray(new CommandArgument[0]);
	}

	@Override
	public void onCommand(Sender sender) {
		Sender receivingPlayer = arguments.getSender("player");
		long length = arguments.getTime("length");
		String reason = String.join(" ", arguments.getStringList("reason"));
		reason = reason.equals("null") ? null : reason;

		Punishment punishment = new Punishment(receivingPlayer.getId(), punishmentType, length, sender.getId(), reason);
		plugin.getPunishmentManager().punish(receivingPlayer, punishment);

		sender.sendMessage("Punished " + receivingPlayer.getUsername() + " (" + punishmentType.name() + ")");
	}
}