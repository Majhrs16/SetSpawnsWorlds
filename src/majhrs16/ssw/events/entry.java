package majhrs16.ssw.events;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import majhrs16.ssw.main;
import majhrs16.ssw.util;

public class entry implements Listener{
	private main plugin;
	private util util;
	public entry(main plugin) {
		this.plugin = plugin;
		this.util   = new util(plugin);
	}

	@EventHandler
	public void onEntry(PlayerJoinEvent event) {
		FileConfiguration config  = plugin.getConfig();
		util._Sender player       = util.new _Sender(event.getPlayer());

		/*
		TextComponent message = new TextComponent("Click me");
		message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.spigotmc.org"));
		message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Visit the Spigot website!").create()));
		player.spigot().sendMessage(message);
		*/

		String path = "spawn";
		if (config.contains(path + ".world") && config.getString(path + ".world").equals(player.getWorld().getName())) {
			util.tpto(player, util.getLoc(player, path));

		} else if (config.contains(path + ".Lobbys." + player.getWorld().getName())) {
			util.tpto(player, util.getLoc(player, path + ".Lobbys." + player.getWorld().getName()));

		} else if (config.contains(path + ".TpLastCords." + player.getWorld().getName())) {
			util.tpto(player, util.getLoc(player, path + ".TpLastCords." + player.getWorld().getName()));
		}

		if (util.IF("welcome-message")) {
			List<String> text = config.getStringList("welcome-message-text");
			for(int i = 0; i < text.size(); i++) {
				player.sendMessage(ChatColor.translateAlternateColorCodes("&".charAt(0), text.get(i)).replaceAll("%player%", player.getName()));
			}
		}
	}
}