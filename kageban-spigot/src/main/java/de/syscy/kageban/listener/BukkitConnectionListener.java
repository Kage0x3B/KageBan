package de.syscy.kageban.listener;

import de.syscy.kageban.common.KageBanPlugin;
import de.syscy.kageban.common.listener.AbstractConnectionListener;
import de.syscy.kageban.common.sender.Sender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class BukkitConnectionListener extends AbstractConnectionListener implements Listener {
	public BukkitConnectionListener(KageBanPlugin plugin) {
		super(plugin);
	}

	@EventHandler
	public void onPlayerJoin(PlayerLoginEvent event) {
		Sender player = plugin.getBootstrap().getSenderFactory().wrapSender(event.getPlayer());
		LoginResult result = onPlayerConnect(player);
		event.setResult(result.isAllowed() ? PlayerLoginEvent.Result.ALLOWED : PlayerLoginEvent.Result.KICK_BANNED);
		event.setKickMessage(result.getKickMessage());
	}

	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		Sender player = plugin.getBootstrap().getSenderFactory().wrapSender(event.getPlayer());
		onPlayerDisconnect(player);
	}
}