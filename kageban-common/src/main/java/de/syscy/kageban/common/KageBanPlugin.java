package de.syscy.kageban.common;

import de.syscy.kageban.common.bootstrap.IKageBanBootstrap;
import de.syscy.kageban.common.bootstrap.scheduler.SchedulerAdapter;
import de.syscy.kageban.common.command.CommandManager;
import de.syscy.kageban.common.commands.Commands;
import de.syscy.kageban.common.config.KBConfigurationSection;
import de.syscy.kageban.common.punishment.PunishmentManager;
import de.syscy.kageban.common.storage.AbstractStorage;
import de.syscy.kageban.common.storage.IPunishmentStorage;
import de.syscy.kageban.common.storage.dao.AbstractPunishmentDao;
import de.syscy.kageban.common.storage.dao.RethinkDBDao;
import lombok.Getter;

import java.util.logging.Logger;

public class KageBanPlugin {
	private static @Getter KageBanPlugin instance;

	private final @Getter IKageBanBootstrap bootstrap;

	private @Getter PunishmentManager punishmentManager;
	private @Getter IPunishmentStorage storage;

	private @Getter CommandManager commandManager;

	public KageBanPlugin(IKageBanBootstrap bootstrap) {
		this.bootstrap = bootstrap;

		instance = this;
	}

	public void onEnable() {
		punishmentManager = new PunishmentManager(this);

		KBConfigurationSection databaseSection = bootstrap.getKBConfig().getSection("database");
		String databasePrefix = databaseSection.getString("prefix", "kb");
		AbstractPunishmentDao storageDao = new RethinkDBDao(this, databasePrefix, databaseSection);
		storage = AbstractStorage.create(this, storageDao);
		storage.init();

		commandManager = Commands.createCommandManager(this);
	}

	public void onDisable() {
		bootstrap.getScheduler().shutdown();
		storage.shutdown();
	}

	public Logger getLogger() {
		return bootstrap.getLogger();
	}

	public SchedulerAdapter getScheduler() {
		return bootstrap.getScheduler();
	}
}