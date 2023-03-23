package majhrs16.ssw.events;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import majhrs16.ssw.main;
import majhrs16.ssw.util;

public class setworld implements Listener{
	private main plugin;
	private util util;
	public setworld(main plugin) {
		this.plugin = plugin;
		this.util   = new util(plugin);
	}

	@EventHandler
	public void _setworld(PlayerTeleportEvent event) {
		String WorldNameBefore = event.getFrom().getWorld().getName();
		String WorldNameAfter  = event.getTo().getWorld().getName();

		if (WorldNameBefore.equals(WorldNameAfter)) {
			return;
		}

		String path, msg1;
		FileConfiguration config  = plugin.getConfig();
		FileConfiguration players = plugin.getPlayers();
		final util._Sender player = util.new _Sender(event.getPlayer());

		path = "spawn";
		if (config.contains(path + ".world")) {
			if (config.getString(path + ".world").equals(WorldNameAfter) && config.contains(path + ".Lobbys." + WorldNameAfter) || 
				config.contains(path + ".Lobbys." + WorldNameAfter) && config.contains(path + ".TpLastCords." + WorldNameAfter) ||
				config.getString(path + ".world").equals(WorldNameAfter) && config.contains(path + ".TpLastCords." + WorldNameAfter)) {

				msg1 = ChatColor.translateAlternateColorCodes("&".charAt(0), plugin.name + " &e[&cError&e] &cDuplicacion de mundo&f:");
				Bukkit.getConsoleSender().sendMessage(msg1);
				if (player.hasPermission("SetSpawnsWorlds.ConflictWorldName.show")) {
					player.sendMessage(msg1);
				}
			}
		}

		ArrayList<String> list = new ArrayList<String>();
		if (config.contains(path + ".world") && config.getString(path + ".world").equals(WorldNameAfter) && config.contains(path + ".Lobbys." + WorldNameAfter)) {
			list.add(ChatColor.translateAlternateColorCodes("&".charAt(0), plugin.name + "   &espawn&f.&cworld &6<&f-&6> &espawn&f.&eLobbys&f.&c" + WorldNameAfter));

		} if (config.contains(path + ".Lobbys." + WorldNameAfter) && config.contains(path + ".TpLastCords." + WorldNameAfter)) {
			list.add(ChatColor.translateAlternateColorCodes("&".charAt(0), plugin.name + "   &espawn&f.&eLobbys&f.&c" + WorldNameAfter + " &6<&f-&6> &espawn&f.&eTpLastCords&f.&c" + WorldNameAfter));

		} if (config.contains(path + ".world") && config.getString(path + ".world").equals(WorldNameAfter) && config.contains(path + ".TpLastCords." + WorldNameAfter)) {
			list.add(ChatColor.translateAlternateColorCodes("&".charAt(0), plugin.name + "   &espawn&f.&eTpLastCords&f.&c" + WorldNameAfter + " &6<&f-&6> &espawn&f.&cworld"));
		}
		
		if (list.size() > 0) {
			for (Integer i = 0; i < list.size(); i++) {
				Bukkit.getConsoleSender().sendMessage(list.get(i));

				if (player.hasPermission("SetSpawnsWorlds.ConflictWorldName.show")) {
					player.sendMessage(list.get(i));
				}
			}
			return;
		}

		if (config.contains(path + ".world") && config.getString(path + ".world").equals(WorldNameAfter)) { /////////////////////////////////////////////////////////////// SPAWN
//			event.setCancelled(true); // bugazo // no se puede tepear un jugador del vacio absoluto a algun lugar. // NO USAR, causa bugs.
			util.tpto(player, util.getLoc(player, path));

		} else if(config.contains(path + "Lobbys." + WorldNameAfter)) { ///////////////////////////////////////////////////////////////////////// LOBBYS
			util.WXYZYPGM l = util.getLoc(player, path + ".Lobbys." + WorldNameAfter);
			l.setWorld(event.getTo().getWorld());
			util.tpto(player, l);
		}

		if (config.contains(path + ".TpLastCords")) { //////////////////////////////////////////////////////////////////// TpLastCords
			GameMode Gamemode;
			final util.WXYZYPGM l2;

			path = player.getUniqueId() + "." + event.getTo().getWorld().getName();
			if (players.contains(path)) {
				Gamemode = util.toGameMode(player, path);
				if (util.IF("SaveLastGameMode")) {
					Gamemode = util.toGameMode(players, player, path);
				}

				l2 = util.getLoc(players, player, path);
				l2.setGamemode(Gamemode);

			} else {
				path = "spawn.TpLastCords." + WorldNameAfter;
				l2 = util.getLoc(player, path);
			}

			l2.setWorld(event.getTo().getWorld());

			path = "spawn.TpLastCords.";
			if (config.contains(path + WorldNameBefore)) {
				Location l = event.getFrom();
				Double x   = l.getX();
				Double y   = l.getY();
				Double z   = l.getZ();
				
				///////////////////////////////
				// CORRECTOR DEL END
				if(l.getWorld().getBlockAt(x.intValue(), y.intValue(), z.intValue()).getType() == Material.ENDER_PORTAL ) {
					while (l.getWorld().getBlockAt(x.intValue(), y.intValue(), z.intValue()).getType() == Material.ENDER_PORTAL) {
						x++;
					}

					while (!(l.getWorld().getBlockAt(x.intValue(), y.intValue() + 1, z.intValue()).getType() == Material.AIR && l.getWorld().getBlockAt(x.intValue(), y.intValue() + 2, z.intValue()).getType() == Material.AIR)) {
						y = y + 2;
					}

					if (l.getWorld().getBlockAt(x.intValue(), y.intValue() + 2, z.intValue()).getType() == Material.AIR && l.getWorld().getBlockAt(x.intValue(), y.intValue() + 3, z.intValue()).getType() == Material.AIR) {
						y++;
					}

				} else if(l.getWorld().getBlockAt(x.intValue(), y.intValue() + 1, z.intValue()).getType() == Material.ENDER_PORTAL ||
						  l.getWorld().getBlockAt(x.intValue(), y.intValue() + 2, z.intValue()).getType() == Material.ENDER_PORTAL) {
					y = Double.valueOf(y.intValue());
				}

				path = String.valueOf(player.getUniqueId()) + "." + l.getWorld().getName();
				players.set(player.getUniqueId() + ".name", player.getName());
				players.set(path + ".x", x);
				players.set(path + ".y", y);
				players.set(path + ".z", z);
				players.set(path + ".yaw", l.getYaw());
				players.set(path + ".pitch", l.getPitch());
				if (util.IF(config, "SaveLastGameMode")) {
					players.set(path + ".gamemode", player.getGameMode().toString());
				}
				plugin.savePlayers();

				if (event.getCause() == PlayerTeleportEvent.TeleportCause.END_PORTAL ||
						event.getCause() == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL || 
						event.getCause() == PlayerTeleportEvent.TeleportCause.COMMAND) {
					return;
				}

				if(l.getWorld().getBlockAt(x.intValue(), y.intValue(), z.intValue()).getType() != Material.PORTAL && event.getTo().getWorld().getBlockAt(0, 127, 0).getType() == Material.BEDROCK || // Overworld -> Nether
						l.getWorld().getBlockAt(x.intValue(), y.intValue(), z.intValue()).getType() != Material.PORTAL && event.getTo().getWorld().getBlockAt(0, 127, 0).getType() == Material.AIR) { // Nether -> Overworld0
					return;
				}
			}

			//////////////////////
			// Tp a LastCord
			path = "spawn.TpLastCords.";
			if (config.contains(path + WorldNameAfter)) {
				util.tpto(player, l2);
			}
		}
	}
}