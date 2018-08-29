package de.syscy.kageban.common.command.argument;

import de.syscy.kageban.common.command.exception.InvalidCommandArgumentException;
import de.syscy.kageban.common.sender.Sender;
import de.syscy.kageban.common.util.Util;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

public class DoubleArgument extends CommandArgument<Double> {
	private double min;
	private double max;

	public DoubleArgument(DoubleArgumentBuilder builder) {
		super(builder);

		min = builder.min;
		max = builder.max;
	}

	@Override
	public Double getValue(Sender sender, String[] args) {
		return args.length <= index ? defaultValue : Double.parseDouble(args[index]);
	}

	@Override
	public void checkArg(Sender sender, String[] args) {
		if(args.length <= index) {
			return;
		}

		if(!Util.isNumber(args[index])) {
			throw new InvalidCommandArgumentException(name);
		}

		if(allowedValuesFunction != null) {
			List<String> allowedValues = allowedValuesFunction.apply(sender);

			if(!allowedValues.isEmpty() && !allowedValues.contains(args[index])) {
				throw new InvalidCommandArgumentException(name);
			}
		}

		double value = getValue(sender, args);

		if(value < min || value > max) {
			throw new InvalidCommandArgumentException(name);
		}
	}

	public static DoubleArgumentBuilder create(String name) {
		return new DoubleArgumentBuilder(name);
	}

	@Accessors(fluent = true)
	public static class DoubleArgumentBuilder extends CommandArgumentBuilder<Double> {
		private @Setter double min = Double.MIN_VALUE;
		private @Setter double max = Double.MAX_VALUE;

		private DoubleArgumentBuilder(String name) {
			super(name);
		}

		public DoubleArgumentBuilder range(double min, double max) {
			return min(min).max(max);
		}

		@Override
		public CommandArgument<Double> build() {
			return new DoubleArgument(this);
		}
	}
}