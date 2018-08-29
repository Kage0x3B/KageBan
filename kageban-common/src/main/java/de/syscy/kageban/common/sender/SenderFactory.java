package de.syscy.kageban.common.sender;

import java.util.Objects;
import java.util.UUID;

public abstract class SenderFactory<T> {
	public Sender wrapSender(Object sender) {
		Objects.requireNonNull(sender);

		return new AbstractSender<>(this, (T) sender);
	}

	public abstract Sender getSender(UUID senderId);

	public abstract Sender getSender(String senderName);

	public abstract UUID getId(T sender);

	public abstract String getUsername(T sender);

	public abstract boolean isConsole(T sender);

	public abstract void sendMessage(T sender, String message);

	public abstract boolean hasPermission(T sender, String permission);

	public abstract void disconnect(T sender, String message);
}