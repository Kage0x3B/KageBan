package de.syscy.kageban.common.command.argument;

import de.syscy.kageban.common.sender.Sender;
import lombok.Getter;

import java.util.*;
import java.util.function.Function;

public class StringListArgument extends CommandArgument<List<String>> {
	protected @Getter Function<Sender, List<String>> suggestions;

	public StringListArgument(StringListArgumentBuilder builder) {
		super(builder);

		suggestions = builder.suggestions;
	}

	@Override
	public List<String> getValue(Sender sender, String[] args) {
		if(args.length <= index) {
			return defaultValue;
		}

		List<String> stringList = new ArrayList<>();

		for(int i = index; i < args.length; i++) {
			stringList.add(args[i]);
		}

		return stringList;
	}

	@Override
	public void checkArg(Sender sender, String[] args) {
		if(args.length <= index) {
			return;
		}
	}

	@Override
	public List<String> onTabComplete(Sender sender, String[] args) {
		if(this.suggestions == null) {
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

	public static StringListArgumentBuilder create(String name) {
		return new StringListArgumentBuilder(name);
	}

	public static class StringListArgumentBuilder extends CommandArgumentBuilder<List<String>> {
		protected Function<Sender, List<String>> suggestions;

		private StringListArgumentBuilder(String name) {
			super(name);
		}

		public StringListArgumentBuilder suggestions(Function<Sender, List<String>> suggestionsFunction) {
			suggestions = suggestionsFunction;

			return this;
		}

		public StringListArgumentBuilder suggestions(final String[] suggestionArray) {
			suggestions = sender -> Arrays.asList(suggestionArray);

			return this;
		}

		public StringListArgumentBuilder suggestions(final List<String> suggestionList) {
			suggestions = sender -> suggestionList;

			return this;
		}

		@Override
		public CommandArgument<List<String>> build() {
			return new StringListArgument(this);
		}
	}
}