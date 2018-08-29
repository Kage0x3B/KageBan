package de.syscy.kageban.common.command.argument;

import de.syscy.kageban.common.command.exception.InvalidCommandArgumentException;
import de.syscy.kageban.common.sender.Sender;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;

public class StringArgument extends CommandArgument<String> {
	protected @Getter Function<Sender, List<String>> suggestions;
	private Pattern regexPattern = null;

	public StringArgument(StringArgumentBuilder builder) {
		super(builder);

		suggestions = builder.suggestions;

		if(builder.regexPattern != null && !builder.regexPattern.isEmpty()) {
			regexPattern = Pattern.compile(builder.regexPattern);
		}
	}

	@Override
	public String getValue(Sender sender, String[] args) {
		return args.length <= index ? defaultValue : args[index];
	}

	@Override
	public void checkArg(Sender sender, String[] args) {
		if(args.length <= index) {
			return;
		}

		if(regexPattern != null) {
			if(!regexPattern.matcher(args[index]).matches()) {
				throw new InvalidCommandArgumentException(name);
			}
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
		if(suggestions == null) {
			return Collections.emptyList();
		}

		List<String> suggestions = new ArrayList<>();

		for(String possibleSuggestion : this.suggestions.apply(sender)) {
			if(possibleSuggestion.toLowerCase().startsWith(args[index].toLowerCase())) {
				suggestions.add(possibleSuggestion);
			}
		}

		return suggestions;
	}

	public static StringArgumentBuilder create(String name) {
		return new StringArgumentBuilder(name);
	}

	@Accessors(fluent = true)
	public static class StringArgumentBuilder extends CommandArgumentBuilder<String> {
		protected Function<Sender, List<String>> suggestions;
		private @Setter String regexPattern = null;

		private StringArgumentBuilder(String name) {
			super(name);
		}

		public StringArgumentBuilder values(String... values) {
			suggestions(values);
			allowedValues(values);

			return this;
		}

		public StringArgumentBuilder values(Function<Sender, List<String>> valuesFunction) {
			suggestions(valuesFunction);
			allowedValues(valuesFunction);

			return this;
		}

		public StringArgumentBuilder suggestions(Function<Sender, List<String>> suggestionsFunction) {
			suggestions = suggestionsFunction;

			return this;
		}

		public StringArgumentBuilder suggestions(final String[] suggestionArray) {
			suggestions = sender -> Arrays.asList(suggestionArray);

			return this;
		}

		public StringArgumentBuilder suggestions(final List<String> suggestionList) {
			suggestions = sender -> suggestionList;

			return this;
		}

		@Override
		public StringArgument build() {
			return new StringArgument(this);
		}
	}
}