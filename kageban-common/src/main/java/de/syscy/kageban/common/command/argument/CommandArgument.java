package de.syscy.kageban.common.command.argument;

import de.syscy.kageban.common.sender.Sender;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public abstract class CommandArgument<T> {
	protected @Getter @Setter int index;

	protected @Getter String name;
	protected @Getter T defaultValue;
	protected @Getter boolean required;
	protected Function<Sender, List<String>> allowedValuesFunction;

	public CommandArgument(CommandArgumentBuilder<T> builder) {
		name = builder.name;
		defaultValue = builder.defaultValue;
		required = builder.required;
		allowedValuesFunction = builder.allowedValuesFunction;
	}

	public abstract T getValue(Sender sender, String[] args);

	public abstract void checkArg(Sender sender, String[] args);

	public List<String> onTabComplete(Sender sender, String[] args) {
		return null;
	}

	@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
	@Accessors(fluent = true)
	public static abstract class CommandArgumentBuilder<T> {
		protected final String name;
		protected @Setter T defaultValue = null;
		protected boolean required = true;
		protected Function<Sender, List<String>> allowedValuesFunction;

		public CommandArgumentBuilder<?> allowedValues(Function<Sender, List<String>> allowedValuesFunction) {
			this.allowedValuesFunction = allowedValuesFunction;

			return this;
		}

		public CommandArgumentBuilder<?> allowedValues(final String[] allowedValuesArray) {
			allowedValuesFunction = sender -> Arrays.asList(allowedValuesArray);

			return this;
		}

		public CommandArgumentBuilder<?> allowedValues(final List<String> allowedValuesList) {
			allowedValuesFunction = sender -> allowedValuesList;

			return this;
		}

		public CommandArgumentBuilder<?> notRequired() {
			this.required = false;

			return this;
		}

		public abstract CommandArgument<T> build();
	}
}