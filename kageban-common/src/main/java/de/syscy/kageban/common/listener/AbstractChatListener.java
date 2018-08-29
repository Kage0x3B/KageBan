package de.syscy.kageban.common.listener;

import de.syscy.kageban.common.KageBanPlugin;
import de.syscy.kageban.common.sender.Sender;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractChatListener {
	protected final KageBanPlugin plugin;

	protected EventResult onChatMessage(Sender player) {
		EventResult result = new EventResult();

		if(plugin.getPunishmentManager().isMuted(player.getId())) {
			player.sendMessage("You are muted!");
			result.setCancelled(true);
		}

		return result;
	}

	@Data
	protected static class EventResult {
		private boolean cancelled = false;
	}
}