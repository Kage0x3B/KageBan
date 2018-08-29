package de.syscy.kageban.common.data;

import de.syscy.kageban.common.punishment.Punishment;
import de.syscy.kageban.common.punishment.PunishmentType;
import lombok.Data;

import java.util.Collection;
import java.util.UUID;

@Data
public class UserData {
	private UUID uniqueId;

	private Collection<Punishment> punishments;

	public boolean isBanned() {
		return punishments.stream().filter(Punishment::isActive).anyMatch(p -> p.getType() == PunishmentType.BAN);
	}

	public boolean isMuted() {
		return punishments.stream().filter(Punishment::isActive).anyMatch(p -> p.getType() == PunishmentType.MUTE);
	}

	public long getWarningAmount() {
		return punishments.stream().filter(Punishment::isActive).filter(p -> p.getType() == PunishmentType.WARNING).count();
	}
}