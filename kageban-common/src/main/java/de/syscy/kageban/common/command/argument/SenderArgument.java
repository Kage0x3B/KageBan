package de.syscy.kageban.common.command.argument;

import de.syscy.kageban.common.KageBanPlugin;
import de.syscy.kageban.common.command.exception.InvalidCommandArgumentException;
import de.syscy.kageban.common.sender.Sender;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.stream.Collectors;

public class SenderArgument extends CommandArgument<Sender> {
	public SenderArgument(PlayerArgumentBuilder builder) {
		super(builder);
	}

	@Override
	public Sender getValue(Sender sender, String[] args) {
		if(args.length <= index) {
			return defaultValue;
		}

		Sender senderArg = KageBanPlugin.getInstance().getBootstrap().getSenderFactory().getSender(args[index]);

		return sender == null ? defaultValue : senderArg;
	}

	@Override
	public void checkArg(Sender sender, String[] args) {
		if(args.length <= index) {
			return;
		}

		boolean exists = KageBanPlugin.getInstance().getBootstrap().getPlayerNameStream().anyMatch(s -> s.equalsIgnoreCase(args[index]));

		if(!exists) {
			throw new InvalidCommandArgumentException(name);
		}

		if(allowedValuesFunction != null) {
			List<String> allowedValues = allowedValuesFunction.apply(sender);

			if(!allowedValues.isEmpty() && !allowedValues.contains(args[index])) {
				throw new InvalidCommandArgumentException(name);
			}
		}
	}

	@Override
	public List<String> onTabComplete(Sender sender, String[] args) {
		return KageBanPlugin.getInstance().getBootstrap().getPlayerNameStream().filter(s -> args[index].toLowerCase().startsWith(s.toLowerCase())).collect(Collectors.toList());
	}

	public static PlayerArgumentBuilder create(String name) {
		return new PlayerArgumentBuilder(name);
	}

	@Accessors(fluent = true)
	public static class PlayerArgumentBuilder extends CommandArgumentBuilder<Sender> {
		private PlayerArgumentBuilder(String name) {
			super(name);
		}

		@Override
		public SenderArgument build() {
			return new SenderArgument(this);
		}
	}
}