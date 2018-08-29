package de.syscy.kageban;

import de.syscy.kageban.common.sender.Sender;
import de.syscy.kageban.common.sender.SenderFactory;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

@RequiredArgsConstructor
public class BukkitSenderFactory extends SenderFactory<CommandSender> {
	private final KageBanSpigot bootstrap;

	@Override
	public Sender getSender(UUID senderId) {
		if(senderId.equals(Sender.CONSOLE_UUID)) {
			return wrapSender(Bukkit.getConsoleSender());
		}

		Player player = Bukkit.getPlayer(senderId);

		return player != null ? wrapSender(player) : null;
	}

	@Override
	public Sender getSender(String senderName) {
		if(senderName.equals(Sender.CONSOLE_NAME)) {
			return wrapSender(Bukkit.getConsoleSender());
		}

		Player player = Bukkit.getPlayer(senderName);

		return player != null ? wrapSender(player) : null;
	}

	@Override
	public UUID getId(CommandSender sender) {
		if(sender instanceof Player) {
			return ((Player) sender).getUniqueId();
		}

		return Sender.CONSOLE_UUID;
	}

	@Override
	public String getUsername(CommandSender sender) {
		if(sender instanceof Player) {
			return sender.getName();
		}

		return Sender.CONSOLE_NAME;
	}

	@Override
	public boolean isConsole(CommandSender sender) {
		return sender instanceof ConsoleCommandSender;
	}

	@Override
	public void sendMessage(CommandSender sender, String message) {
		sender.sendMessage(message);
	}

	@Override
	public boolean hasPermission(CommandSender sender, String permission) {
		return sender.hasPermission(permission);
	}

	@Override
	public void disconnect(CommandSender sender, String message) {
		if(sender instanceof Player) {
			((Player) sender).kickPlayer(message);
		}
	}
}