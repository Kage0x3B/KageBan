package de.syscy.kageban.listener;

import de.syscy.kageban.common.KageBanPlugin;
import de.syscy.kageban.common.listener.AbstractChatListener;
import de.syscy.kageban.common.sender.Sender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class BukkitChatListener extends AbstractChatListener implements Listener {
	public BukkitChatListener(KageBanPlugin plugin) {
		super(plugin);
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		Sender player = plugin.getBootstrap().getSenderFactory().wrapSender(event.getPlayer());

		EventResult result = onChatMessage(player);
		event.setCancelled(result.isCancelled());
	}
}