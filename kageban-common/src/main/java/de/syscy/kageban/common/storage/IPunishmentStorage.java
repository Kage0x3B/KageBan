package de.syscy.kageban.common.storage;

import de.syscy.kageban.common.punishment.Punishment;
import de.syscy.kageban.common.storage.dao.AbstractPunishmentDao;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface IPunishmentStorage {
	AbstractPunishmentDao getDao();

	void init();

	void shutdown();

	CompletableFuture<List<Punishment>> loadPunishments(UUID playerId);

	CompletableFuture<Void> savePunishment(Punishment punishment);

	CompletableFuture<Void> removePunishment(Punishment punishment);

	CompletableFuture<Void> performMaintenanceTasks();
}