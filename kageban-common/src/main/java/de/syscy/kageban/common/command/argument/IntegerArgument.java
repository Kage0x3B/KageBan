package de.syscy.kageban.common.command.argument;

import de.syscy.kageban.common.command.exception.InvalidCommandArgumentException;
import de.syscy.kageban.common.sender.Sender;
import de.syscy.kageban.common.util.Util;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.regex.Pattern;

public class IntegerArgument extends CommandArgument<Integer> {
	private static Pattern integerPattern = Pattern.compile("(\\+|-)?[\\d]+");

	private int min;
	private int max;

	public IntegerArgument(IntegerArgumentBuilder builder) {
		super(builder);

		min = builder.min;
		max = builder.max;
	}

	@Override
	public Integer getValue(Sender sender, String[] args) {
		return args.length <= index ? defaultValue : Integer.parseInt(args[index]);
	}

	@Override
	public void checkArg(Sender sender, String[] args) {
		if(args.length <= index) {
			return;
		}

		if(!Util.isNumber(args[index]) || !integerPattern.matcher(args[index]).matches()) {
			throw new InvalidCommandArgumentException(name);
		}

		if(allowedValuesFunction != null) {
			List<String> allowedValues = allowedValuesFunction.apply(sender);

			if(!allowedValues.isEmpty() && !allowedValues.contains(args[index])) {
				throw new InvalidCommandArgumentException(name);
			}
		}

		int value = getValue(sender, args);

		if(value < min || value > max) {
			throw new InvalidCommandArgumentException(name);
		}
	}

	public static IntegerArgumentBuilder create(String name) {
		return new IntegerArgumentBuilder(name);
	}

	@Accessors(fluent = true)
	public static class IntegerArgumentBuilder extends CommandArgumentBuilder<Integer> {
		private @Setter int min = Integer.MIN_VALUE;
		private @Setter int max = Integer.MAX_VALUE;

		private IntegerArgumentBuilder(String name) {
			super(name);
		}

		public IntegerArgumentBuilder range(int min, int max) {
			return min(min).max(max);
		}

		@Override
		public CommandArgument<Integer> build() {
			return new IntegerArgument(this);
		}
	}
}