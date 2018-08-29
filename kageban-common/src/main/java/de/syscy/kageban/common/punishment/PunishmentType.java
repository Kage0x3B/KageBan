package de.syscy.kageban.common.punishment;

public enum PunishmentType {
	BAN, MUTE, WARNING, KICK;

	public static PunishmentType fromString(String typeString) {
		for(PunishmentType type : values()) {
			if(type.name().equalsIgnoreCase(typeString)) {
				return type;
			}
		}

		return null;
	}
}