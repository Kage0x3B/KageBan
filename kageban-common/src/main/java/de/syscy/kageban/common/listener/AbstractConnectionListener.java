package de.syscy.kageban.common.listener;

import de.syscy.kageban.common.KageBanPlugin;
import de.syscy.kageban.common.punishment.Punishment;
import de.syscy.kageban.common.punishment.PunishmentType;
import de.syscy.kageban.common.sender.Sender;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public abstract class AbstractConnectionListener {
	protected final KageBanPlugin plugin;

	protected LoginResult onPlayerConnect(Sender player) {
		LoginResult loginResult = new LoginResult();
		CompletableFuture<Void> future = plugin.getPunishmentManager().loadPunishments(player.getId());
		future.join();

		if(plugin.getPunishmentManager().isBanned(player.getId())) {
			Optional<Punishment> banPunishment = plugin.getPunishmentManager().getPunishments(player.getId()).stream().filter(p -> p.getType() == PunishmentType.BAN).findAny();

			if(banPunishment.isPresent()) {
				loginResult.setAllowed(false);
				loginResult.setKickMessage(banPunishment.get().getReason().orElse("Empty"));
			}
		}

		return loginResult;
	}

	protected void onPlayerDisconnect(Sender player) {

	}

	@Data
	protected static class LoginResult {
		private boolean allowed = true;
		private String kickMessage = "";
	}
}