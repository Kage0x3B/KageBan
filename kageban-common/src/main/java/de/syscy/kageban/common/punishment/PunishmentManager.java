package de.syscy.kageban.common.punishment;

import de.syscy.kageban.common.KageBanPlugin;
import de.syscy.kageban.common.sender.Sender;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class PunishmentManager {
	private final KageBanPlugin plugin;

	private final Map<UUID, List<Punishment>> punishmentCache = new WeakHashMap<>();

	public List<Punishment> getPunishments(UUID playerId) {
		return punishmentCache.get(playerId);
	}

	public Stream<Punishment> streamPunishments(UUID playerId) {
		List<Punishment> punishments = punishmentCache.get(playerId);
		return punishments == null ? Stream.empty() : punishments.stream().filter(Punishment::isActive);
	}

	public void punish(Sender player, Punishment punishment) {
		List<Punishment> punishments = punishmentCache.get(player.getId());

		if(punishments == null) {
			punishments = new ArrayList<>();
		}

		punishments.add(punishment);
		punishmentCache.put(player.getId(), punishments);
		plugin.getStorage().savePunishment(punishment);

		String reasonMessage = punishment.getReason().isPresent() ? "Reason: " + punishment.getReason().get() : "";

		if(punishment.getType() == PunishmentType.BAN) {
			player.disconnect("You have been banned! " + reasonMessage);
		} else if(punishment.getType() == PunishmentType.KICK) {
			player.disconnect("You have been kicked! " + reasonMessage);
		} else if(punishment.getType() == PunishmentType.MUTE) {
			player.sendMessage("You have been muted! " + reasonMessage);
		} else if(punishment.getType() == PunishmentType.WARNING) {
			player.sendMessage("You have received a warning! " + reasonMessage);
		}
	}

	public boolean isBanned(UUID playerId) {
		return streamPunishments(playerId).anyMatch(p -> p.getType() == PunishmentType.BAN);
	}

	public boolean isMuted(UUID playerId) {
		return streamPunishments(playerId).anyMatch(p -> p.getType() == PunishmentType.MUTE);
	}

	public long getWarnings(UUID playerId) {
		return streamPunishments(playerId).filter(p -> p.getType() == PunishmentType.WARNING).count();
	}

	public CompletableFuture<Void> loadPunishments(UUID playerId) {
		CompletableFuture<List<Punishment>> future = plugin.getStorage().loadPunishments(playerId);
		return future.thenAccept(p -> punishmentCache.put(playerId, p));
	}

	public void discardData(UUID playerId) {
		List<Punishment> punishments = punishmentCache.remove(playerId);
		punishments.forEach(plugin.getStorage()::removePunishment);
	}
}