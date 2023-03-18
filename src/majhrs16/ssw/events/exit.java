package majhrs16.ssw.events;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import majhrs16.ssw.main;
import majhrs16.ssw.util;

public class exit implements Listener{
	private main plugin;
	private util util;
	public exit(main plugin) {
		this.plugin = plugin;
		this.util   = new util(plugin);
	}
	
	@EventHandler
	public void onExit(PlayerQuitEvent event) {
		FileConfiguration config  = plugin.getConfig();
		FileConfiguration players = plugin.getPlayers();
		Player player             = event.getPlayer();
		Location l                = player.getLocation();

		if (config.contains("spawn.TpLastCords." + l.getWorld().getName())) {
			String path = player.getUniqueId() + "." + l.getWorld().getName();

			players.set(player.getUniqueId() + ".name", player.getName());
			players.set(path + ".x", l.getX());
			players.set(path + ".y", l.getY());
			players.set(path + ".z", l.getZ());
			players.set(path + ".yaw", l.getYaw());
			players.set(path + ".pitch", l.getPitch());
			if (util.IF("SaveLastGameMode")) {
				players.set(path + ".gamemode", player.getGameMode().toString());
			}
			plugin.savePlayers();
		}
	}
}
