package de.syscy.kageban.common.command.argument;

import de.syscy.kageban.common.command.CommandBase;
import de.syscy.kageban.common.command.exception.InvalidUsageException;
import de.syscy.kageban.common.sender.Sender;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class CommandArguments {
	private final CommandBase commandBase;

	private @Getter List<CommandArgument> commandArguments = new ArrayList<>();
	private Map<String, Integer> nameToIndex = new HashMap<>();
	private int requiredArgAmount = 0;

	private Sender currentSender;
	private @Getter String[] currentArgs;

	public List<String> onTabComplete(Sender sender, String[] args) {
		int currentArgIndex = args.length - 1;

		if(commandArguments.size() > currentArgIndex) {
			return commandArguments.get(currentArgIndex).onTabComplete(sender, args);
		}

		return null;
	}

	public void update(Sender sender, String[] args) {
		currentSender = sender;

		if(args.length < requiredArgAmount) {
			throw new InvalidUsageException(commandBase);
		}

		currentArgs = args;
	}

	public void addCommandArgument(CommandArgument<?> commandArgument) {
		if(commandArgument == null) {
			return;
		}

		int index = commandArguments.size();

		commandArgument.setIndex(index);
		commandArguments.add(commandArgument);
		nameToIndex.put(commandArgument.getName().toLowerCase(), index);

		if(commandArguments.size() > 1 && commandArgument.isRequired() && !commandArguments.get(commandArguments.size() - 2).isRequired()) {
			throw new IllegalStateException("There can't be a required command argument (" + commandArgument.getName() + ") after a not required one (" + commandArguments.get(commandArguments.size() - 2).getName() + ")!");
		}

		if(commandArgument.isRequired()) {
			requiredArgAmount = index + 1;
		}
	}

	public <T> CommandArgument<T> getCommandArgument(String argumentName) {
		if(!nameToIndex.containsKey(argumentName.toLowerCase())) {
			return null;
		}

		return getCommandArgument(nameToIndex.get(argumentName.toLowerCase()));
	}

	@SuppressWarnings("unchecked")
	public <T> CommandArgument<T> getCommandArgument(int index) {
		return (CommandArgument<T>) commandArguments.get(index);
	}

	public <T> T getArg(String argumentName) {
		return getArg(argumentName, null);
	}

	public <T> T getArg(int index) {
		return getArg(index, null);
	}

	public <T> T getArg(String argumentName, String defaultValue) {
		if(!nameToIndex.containsKey(argumentName.toLowerCase())) {
			return null;
		}

		return getArg(nameToIndex.get(argumentName.toLowerCase()), defaultValue);
	}

	@SuppressWarnings("unchecked")
	public <T> T getArg(int index, String defaultValue) {
		commandArguments.get(index).checkArg(currentSender, currentArgs);

		return ((CommandArgument<T>) commandArguments.get(index)).getValue(currentSender, currentArgs);
	}

	// ===== String =====

	public String getString(String argumentName) {
		return getString(argumentName, null);
	}

	public String getString(int index) {
		return getString(index, null);
	}

	public String getString(String argumentName, String defaultValue) {
		if(!nameToIndex.containsKey(argumentName.toLowerCase())) {
			return null;
		}

		return getString(nameToIndex.get(argumentName.toLowerCase()), defaultValue);
	}

	public String getString(int index, String defaultValue) {
		commandArguments.get(index).checkArg(currentSender, currentArgs);

		return ((StringArgument) commandArguments.get(index)).getValue(currentSender, currentArgs);
	}

	// ===== String list =====

	public List<String> getStringList(String argumentName) {
		return getStringList(argumentName, 0);
	}

	public List<String> getStringList(int index) {
		return getStringList(index, 0);
	}

	public List<String> getStringList(String argumentName, double defaultValue) {
		if(!nameToIndex.containsKey(argumentName.toLowerCase())) {
			return new ArrayList<>();
		}

		return getStringList(nameToIndex.get(argumentName.toLowerCase()), defaultValue);
	}

	public List<String> getStringList(int index, double defaultValue) {
		commandArguments.get(index).checkArg(currentSender, currentArgs);

		return ((StringListArgument) commandArguments.get(index)).getValue(currentSender, currentArgs);
	}

	// ===== Integer =====

	public int getInt(String argumentName) {
		return getInt(argumentName, 0);
	}

	public int getInt(int index) {
		return getInt(index, 0);
	}

	public int getInt(String argumentName, int defaultValue) {
		if(!nameToIndex.containsKey(argumentName.toLowerCase())) {
			return 0;
		}

		return getInt(nameToIndex.get(argumentName.toLowerCase()), defaultValue);
	}

	public int getInt(int index, int defaultValue) {
		commandArguments.get(index).checkArg(currentSender, currentArgs);

		return ((IntegerArgument) commandArguments.get(index)).getValue(currentSender, currentArgs);
	}

	// ===== Double =====

	public double getDouble(String argumentName) {
		return getDouble(argumentName, 0);
	}

	public double getDouble(int index) {
		return getDouble(index, 0);
	}

	public double getDouble(String argumentName, double defaultValue) {
		if(!nameToIndex.containsKey(argumentName.toLowerCase())) {
			return 0;
		}

		return getDouble(nameToIndex.get(argumentName.toLowerCase()), defaultValue);
	}

	public double getDouble(int index, double defaultValue) {
		commandArguments.get(index).checkArg(currentSender, currentArgs);

		return ((DoubleArgument) commandArguments.get(index)).getValue(currentSender, currentArgs);
	}

	// ===== Boolean =====

	public boolean getBoolean(String argumentName) {
		return getBoolean(argumentName, false);
	}

	public boolean getBoolean(int index) {
		return getBoolean(index, false);
	}

	public boolean getBoolean(String argumentName, boolean defaultValue) {
		if(!nameToIndex.containsKey(argumentName.toLowerCase())) {
			return false;
		}

		return getBoolean(nameToIndex.get(argumentName.toLowerCase()), defaultValue);
	}

	public boolean getBoolean(int index, boolean defaultValue) {
		commandArguments.get(index).checkArg(currentSender, currentArgs);

		return ((BooleanArgument) commandArguments.get(index)).getValue(currentSender, currentArgs);
	}

	// ===== Sender =====

	public Sender getSender(String argumentName) {
		return getSender(argumentName, null);
	}

	public Sender getSender(int index) {
		return getSender(index, null);
	}

	public Sender getSender(String argumentName, Sender defaultValue) {
		if(!nameToIndex.containsKey(argumentName.toLowerCase())) {
			return null;
		}

		return getSender(nameToIndex.get(argumentName.toLowerCase()), defaultValue);
	}

	public Sender getSender(int index, Sender defaultValue) {
		commandArguments.get(index).checkArg(currentSender, currentArgs);

		return ((SenderArgument) commandArguments.get(index)).getValue(currentSender, currentArgs);
	}

	// ===== Time =====

	public long getTime(String argumentName) {
		return getTime(argumentName, 0);
	}

	public long getTime(int index) {
		return getTime(index, 0);
	}

	public long getTime(String argumentName, long defaultValue) {
		if(!nameToIndex.containsKey(argumentName.toLowerCase())) {
			return 0;
		}

		return getTime(nameToIndex.get(argumentName.toLowerCase()), defaultValue);
	}

	public long getTime(int index, long defaultValue) {
		commandArguments.get(index).checkArg(currentSender, currentArgs);

		return ((TimeArgument) commandArguments.get(index)).getValue(currentSender, currentArgs);
	}
}