package de.syscy.kageban.common.sender;

import java.util.Optional;
import java.util.UUID;

public interface Sender {
	UUID CONSOLE_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
	String CONSOLE_NAME = "Console";

	UUID getId();

	String getUsername();

	boolean isConsole();

	void sendMessage(String message);

	boolean hasPermission(String permission);

	void disconnect(String message);

	boolean isValid();

	Optional<Object> getHandle();
}