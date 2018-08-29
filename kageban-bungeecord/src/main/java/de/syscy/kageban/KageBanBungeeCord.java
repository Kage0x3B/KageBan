package de.syscy.kageban;

import de.syscy.kageban.common.KageBanPlugin;
import de.syscy.kageban.common.bootstrap.IKageBanBootstrap;
import de.syscy.kageban.common.bootstrap.scheduler.SchedulerAdapter;
import de.syscy.kageban.common.config.KBConfigurationSection;
import de.syscy.kageban.common.sender.SenderFactory;
import de.syscy.kageban.common.util.PlatformType;
import lombok.Getter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import java.util.stream.Stream;

public final class KageBanBungeeCord extends Plugin implements IKageBanBootstrap {
	private KageBanPlugin plugin;

	private KBConfigurationSection configWrapper;

	private @Getter SchedulerAdapter scheduler;
	private @Getter SenderFactory<CommandSender> senderFactory;

	@Override
	public void onEnable() {
		loadConfig();

		scheduler = new BungeeSchedulerAdapter(this);
		senderFactory = new BungeeSenderFactory(this);

		plugin = new KageBanPlugin(this);
		plugin.onEnable();
	}

	@Override
	public void onDisable() {
		plugin.onDisable();
	}

	private void loadConfig() {
		try {
			Configuration configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
			configWrapper = new ConfigurationSectionWrapper(configuration);
		} catch(IOException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public KBConfigurationSection getKBConfig() {
		return configWrapper;
	}

	@Override
	public PlatformType getType() {
		return PlatformType.BUNGEE;
	}

	@Override
	public String getServerBrand() {
		return getProxy().getName();
	}

	@Override
	public String getServerVersion() {
		return getProxy().getVersion();
	}

	@Override
	public File getDataDirectory() {
		return getDataFolder();
	}

	@Override
	public InputStream getResourceStream(String path) {
		return getResourceAsStream(path);
	}

	@Override
	public Stream<String> getPlayerNameStream() {
		return getProxy().getPlayers().stream().map(CommandSender::getName);
	}

	@Override
	public Stream<UUID> getPlayerIdStream() {
		return getProxy().getPlayers().stream().map(ProxiedPlayer::getUniqueId);
	}

	@Override
	public boolean isPlayerOnline(UUID playerId) {
		ProxiedPlayer player = getProxy().getPlayer(playerId);

		return player != null && player.isConnected();
	}
}