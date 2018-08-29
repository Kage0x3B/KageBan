package de.syscy.kageban.common.command.argument;

import de.syscy.kageban.common.command.exception.InvalidCommandArgumentException;
import de.syscy.kageban.common.sender.Sender;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BooleanArgument extends CommandArgument<Boolean> {
	private static List<String> trueStrings = Arrays.asList("true", "yes", "1");
	private static List<String> falseStrings = Arrays.asList("false", "no", "0");

	public BooleanArgument(BooleanArgumentBuilder builder) {
		super(builder);
	}

	@Override
	public Boolean getValue(Sender sender, String[] args) {
		if(args.length <= index) {
			return defaultValue;
		}

		if(trueStrings.contains(args[index].toLowerCase())) {
			return true;
		}

		return false;
	}

	@Override
	public List<String> onTabComplete(Sender sender, String[] args) {
		String arg = args[index].toLowerCase();

		if(arg.startsWith("t")) {
			return Collections.singletonList("true");
		} else if(arg.startsWith("f")) {
			return Collections.singletonList("false");
		} else {
			return Arrays.asList("true", "false");
		}
	}

	@Override
	public void checkArg(Sender sender, String[] args) {
		if(args.length <= index) {
			return;
		}

		if(!trueStrings.contains(args[index].toLowerCase()) && !falseStrings.contains(args[index].toLowerCase())) {
			throw new InvalidCommandArgumentException(name);
		}

		if(allowedValuesFunction != null) {
			List<String> allowedValues = allowedValuesFunction.apply(sender);

			if(!allowedValues.isEmpty() && !allowedValues.contains(args[index])) {
				throw new InvalidCommandArgumentException(name);
			}
		}
	}

	public static BooleanArgumentBuilder create(String name) {
		return new BooleanArgumentBuilder(name);
	}

	public static class BooleanArgumentBuilder extends CommandArgumentBuilder<Boolean> {
		private BooleanArgumentBuilder(String name) {
			super(name);
		}

		@Override
		public CommandArgument<Boolean> build() {
			return new BooleanArgument(this);
		}
	}
}