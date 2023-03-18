package majhrs16.ssw.events;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import majhrs16.ssw.main;
import majhrs16.ssw.util;

public class respawn implements Listener{
	private main plugin;
	private util util;
	public respawn(main plugin) {
		this.plugin = plugin;
		this.util   = new util(plugin);
	}

	@EventHandler
	public void _respawn(PlayerRespawnEvent event) {
		FileConfiguration config = plugin.getConfig();
		util._Sender player            = util.new _Sender(event.getPlayer());

		String path         = "respawn." + player.getWorld().getName();
		if (config.contains(path)) {
			util.WXYZYPGM l = util.getLoc(player, path);

			if (util.IF(path + ".Force")) {
				player.spigot().respawn();
			}

			player.setGameMode(l.getGamemode());
			event.setRespawnLocation(l);
			
		}
	}
}
