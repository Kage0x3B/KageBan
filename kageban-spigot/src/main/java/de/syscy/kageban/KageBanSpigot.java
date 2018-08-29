package de.syscy.kageban;

import de.syscy.kageban.common.KageBanPlugin;
import de.syscy.kageban.common.bootstrap.IKageBanBootstrap;
import de.syscy.kageban.common.bootstrap.scheduler.SchedulerAdapter;
import de.syscy.kageban.common.config.KBConfigurationSection;
import de.syscy.kageban.common.sender.SenderFactory;
import de.syscy.kageban.common.util.PlatformType;
import de.syscy.kageban.listener.BukkitChatListener;
import de.syscy.kageban.listener.BukkitConnectionListener;
import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStream;
import java.util.UUID;
import java.util.stream.Stream;

public final class KageBanSpigot extends JavaPlugin implements IKageBanBootstrap {
	private KageBanPlugin plugin;

	private KBConfigurationSection configWrapper;

	private @Getter SchedulerAdapter scheduler;
	private @Getter SenderFactory<CommandSender> senderFactory;

	@Override
	public void onEnable() {
		loadConfig();

		scheduler = new BukkitSchedulerAdapter(this);
		senderFactory = new BukkitSenderFactory(this);

		plugin = new KageBanPlugin(this);
		plugin.onEnable();

		getServer().getPluginManager().registerEvents(new BukkitConnectionListener(plugin), this);
		getServer().getPluginManager().registerEvents(new BukkitChatListener(plugin), this);

		PluginCommand mainCommand = getCommand("kageban");
		BukkitCommandExecutor commandExecutor = new BukkitCommandExecutor(this, plugin.getCommandManager());
		mainCommand.setExecutor(commandExecutor);
		mainCommand.setTabCompleter(commandExecutor);
	}

	private void loadConfig() {
		saveDefaultConfig();

		configWrapper = new ConfigurationSectionWrapper(getConfig());
	}

	@Override
	public void onDisable() {
		plugin.onDisable();
	}

	@Override
	public KBConfigurationSection getKBConfig() {
		return configWrapper;
	}

	@Override
	public PlatformType getType() {
		return PlatformType.BUKKIT;
	}

	@Override
	public String getServerBrand() {
		return getServer().getName();
	}

	@Override
	public String getServerVersion() {
		return getServer().getVersion() + " - " + getServer().getBukkitVersion();
	}

	@Override
	public String getServerName() {
		return getServer().getServerName();
	}

	@Override
	public File getDataDirectory() {
		return getDataFolder();
	}

	@Override
	public InputStream getResourceStream(String path) {
		return getResource(path);
	}

	@Override
	public Stream<String> getPlayerNameStream() {
		return getServer().getOnlinePlayers().stream().map(Player::getName);
	}

	@Override
	public Stream<UUID> getPlayerIdStream() {
		return getServer().getOnlinePlayers().stream().map(Player::getUniqueId);
	}

	@Override
	public boolean isPlayerOnline(UUID playerId) {
		Player player = getServer().getPlayer(playerId);

		return player != null && player.isOnline();
	}
}