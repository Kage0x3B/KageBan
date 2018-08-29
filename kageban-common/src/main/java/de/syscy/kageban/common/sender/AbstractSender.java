package de.syscy.kageban.common.sender;

import lombok.Getter;

import java.lang.ref.WeakReference;
import java.util.Optional;
import java.util.UUID;

public class AbstractSender<T> implements Sender {
	private final SenderFactory<T> senderFactory;
	private final WeakReference<T> senderHandle;

	private final @Getter UUID id;
	private final @Getter String username;

	public AbstractSender(SenderFactory<T> senderFactory, T senderHandle) {
		this.senderFactory = senderFactory;
		this.senderHandle = new WeakReference<>(senderHandle);

		this.id = senderFactory.getId(senderHandle);
		this.username = senderFactory.getUsername(senderHandle);
	}

	@Override
	public boolean isConsole() {
		if(!isValid()) {
			return false;
		}

		return senderFactory.isConsole(senderHandle.get());
	}

	@Override
	public void sendMessage(String message) {
		if(!isValid()) {
			return;
		}

		senderFactory.sendMessage(senderHandle.get(), message);
	}

	@Override
	public boolean hasPermission(String permission) {
		if(!isValid()) {
			return false;
		}

		return senderFactory.hasPermission(senderHandle.get(), permission);
	}

	@Override
	public void disconnect(String message) {
		if(!isValid()) {
			return;
		}

		senderFactory.disconnect(senderHandle.get(), message);
	}

	@Override
	public boolean isValid() {
		return senderHandle.get() != null;
	}

	@Override
	public Optional<Object> getHandle() {
		return Optional.ofNullable(senderHandle.get());
	}

	@Override
	public boolean equals(Object o) {
		if(o == this) {
			return true;
		}
		if(!(o instanceof AbstractSender)) {
			return false;
		}

		final AbstractSender that = (AbstractSender) o;
		return getId().equals(that.getId());
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}
}