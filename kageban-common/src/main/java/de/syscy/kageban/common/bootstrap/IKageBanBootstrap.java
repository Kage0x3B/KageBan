package de.syscy.kageban.common.bootstrap;

import de.syscy.kageban.common.bootstrap.scheduler.SchedulerAdapter;
import de.syscy.kageban.common.config.KBConfigurationSection;
import de.syscy.kageban.common.sender.SenderFactory;
import de.syscy.kageban.common.util.PlatformType;

import java.io.File;
import java.io.InputStream;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Stream;

public interface IKageBanBootstrap {
	Logger getLogger();

	SchedulerAdapter getScheduler();

	SenderFactory<?> getSenderFactory();

	KBConfigurationSection getKBConfig();

	PlatformType getType();

	String getServerBrand();

	String getServerVersion();

	default String getServerName() {
		return null;
	}

	File getDataDirectory();

	InputStream getResourceStream(String path);

	Stream<String> getPlayerNameStream();

	Stream<UUID> getPlayerIdStream();

	boolean isPlayerOnline(UUID playerId);
}