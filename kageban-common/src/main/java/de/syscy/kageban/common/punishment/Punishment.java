package de.syscy.kageban.common.punishment;

import lombok.Data;

import java.util.Optional;
import java.util.UUID;

@Data
public class Punishment {
	private final UUID punishmentId;

	private final UUID affectedId;
	private final PunishmentType type;
	private final long issuedTime;
	private final long endTime;
	private final UUID issuerId;
	private final String reason;

	public Punishment(UUID affectedId, PunishmentType type, long length, UUID issuerId, String reason) {
		this(UUID.randomUUID(), affectedId, type, System.currentTimeMillis(), System.currentTimeMillis() + length, issuerId, reason);
	}

	public Punishment(UUID punishmentId, UUID affectedId, PunishmentType type, long issuedTime, long endTime, UUID issuerId, String reason) {
		this.punishmentId = punishmentId;
		this.affectedId = affectedId;
		this.type = type;
		this.issuedTime = issuedTime;
		this.endTime = endTime;
		this.issuerId = issuerId;
		this.reason = reason;
	}

	public Optional<String> getReason() {
		return Optional.ofNullable(reason);
	}

	public boolean isActive() {
		return System.currentTimeMillis() < endTime;
	}
}