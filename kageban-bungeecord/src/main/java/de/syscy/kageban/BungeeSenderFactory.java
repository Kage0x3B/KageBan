package de.syscy.kageban;

import de.syscy.kageban.common.sender.Sender;
import de.syscy.kageban.common.sender.SenderFactory;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

@RequiredArgsConstructor
public class BungeeSenderFactory extends SenderFactory<CommandSender> {
	private final KageBanBungeeCord bootstrap;

	@Override
	public Sender getSender(UUID senderId) {
		if(senderId.equals(Sender.CONSOLE_UUID)) {
			return wrapSender(bootstrap.getProxy().getConsole());
		}

		ProxiedPlayer player = bootstrap.getProxy().getPlayer(senderId);

		return player != null ? wrapSender(player) : null;
	}

	@Override
	public Sender getSender(String senderName) {
		if(senderName.equals(Sender.CONSOLE_NAME)) {
			return wrapSender(bootstrap.getProxy().getConsole());
		}

		ProxiedPlayer player = bootstrap.getProxy().getPlayer(senderName);

		return player != null ? wrapSender(player) : null;
	}

	@Override
	public UUID getId(CommandSender sender) {
		if(sender instanceof ProxiedPlayer) {
			return ((ProxiedPlayer) sender).getUniqueId();
		}

		return Sender.CONSOLE_UUID;
	}

	@Override
	public String getUsername(CommandSender sender) {
		if(sender instanceof ProxiedPlayer) {
			return sender.getName();
		}

		return Sender.CONSOLE_NAME;
	}

	@Override
	public boolean isConsole(CommandSender sender) {
		return !(sender instanceof ProxiedPlayer);
	}

	@Override
	public void sendMessage(CommandSender sender, String message) {
		sender.sendMessage(new TextComponent(message));
	}

	@Override
	public boolean hasPermission(CommandSender sender, String permission) {
		return sender.hasPermission(permission);
	}

	@Override
	public void disconnect(CommandSender sender, String message) {
		if(sender instanceof ProxiedPlayer) {
			((ProxiedPlayer) sender).disconnect(new TextComponent(message));
		}
	}
}