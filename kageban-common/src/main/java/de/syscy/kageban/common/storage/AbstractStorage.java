package de.syscy.kageban.common.storage;

import de.syscy.kageban.common.KageBanPlugin;
import de.syscy.kageban.common.punishment.Punishment;
import de.syscy.kageban.common.storage.dao.AbstractPunishmentDao;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@RequiredArgsConstructor
public class AbstractStorage implements IPunishmentStorage {
	private final KageBanPlugin plugin;
	private final @Getter AbstractPunishmentDao dao;

	public static IPunishmentStorage create(KageBanPlugin plugin, AbstractPunishmentDao backing) {
		IPunishmentStorage baseStorage = new AbstractStorage(plugin, backing);

		return PhasedStorage.wrap(baseStorage);
	}

	@Override
	public void init() {
		try {
			dao.init();
		} catch(Exception ex) {
			plugin.getLogger().severe("Failed to init punishment storage");
			ex.printStackTrace();
		}
	}

	@Override
	public void shutdown() {
		try {
			dao.shutdown();
		} catch(Exception ex) {
			plugin.getLogger().severe("Failed to shutdown punishment storage");
			ex.printStackTrace();
		}
	}

	@Override
	public CompletableFuture<List<Punishment>> loadPunishments(UUID playerId) {
		return makeFuture(() -> dao.loadPunishments(playerId));
	}

	@Override
	public CompletableFuture<Void> savePunishment(Punishment punishment) {
		return makeFuture(() -> dao.savePunishment(punishment));
	}

	@Override
	public CompletableFuture<Void> removePunishment(Punishment punishment) {
		return makeFuture(() -> dao.removePunishment(punishment));
	}

	@Override
	public CompletableFuture<Void> performMaintenanceTasks() {
		return makeFuture(dao::performMaintenanceTasks);
	}

	private <T> CompletableFuture<T> makeFuture(Callable<T> supplier) {
		return CompletableFuture.supplyAsync(() -> {
			try {
				return supplier.call();
			} catch (Exception ex) {
				throw new CompletionException(ex);
			}
		}, plugin.getBootstrap().getScheduler().async());
	}

	private CompletableFuture<Void> makeFuture(ThrowingRunnable runnable) {
		return CompletableFuture.runAsync(() -> {
			try {
				runnable.run();
			} catch (Exception e) {
				throw new CompletionException(e);
			}
		}, plugin.getBootstrap().getScheduler().async());
	}

	private interface ThrowingRunnable {
		void run() throws Exception;
	}
}