package majhrs16.ssw;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.entity.Player.Spigot;
import org.bukkit.scheduler.BukkitRunnable;

public class util {
	private main plugin;
	public util(main plugin) {
		this.plugin = plugin;
	}

	public class WXYZYPGM extends Location {
		private GameMode Gamemode;

		public WXYZYPGM(World world, Double x, Double y, Double z, Float Yaw, Float Pitch, GameMode Gamemode) {
			super(world, x, y, z, Yaw, Pitch);

			setWorld(world);
			setX(x); setY(y); setZ(z);
			setYaw(Yaw); setPitch(Pitch);
			setGamemode(Gamemode);
		}

		public GameMode getGamemode() { return Gamemode; }
		public void setGamemode(GameMode Gamemode) { this.Gamemode = Gamemode; }
	}
	
	public WXYZYPGM getLoc(FileConfiguration file, _Sender player, String path) {
		Location l = player.getLocation();
		World world;
		Double x, y, z;
		Float Yaw, Pitch;
		GameMode Gamemode;

		// Python moment XD -->
		// IA moment XD -->
		if (file.contains(path + ".world"))
			world = plugin.getServer().getWorld(file.getString(path + ".world"));
		else
			world = l.getWorld();

		if (file.contains(path + ".x"))
			x = Double.valueOf(file.getString(path + ".x"));
		else
			x = l.getX();

		if (file.contains(path + ".y"))
			y = Double.valueOf(file.getString(path + ".y"));
		else
			y = l.getY();

		if (file.contains(path + ".z"))
			z = Double.valueOf(file.getString(path + ".z"));
		else
			z = l.getZ();

		if (file.contains(path + ".yaw"))
			Yaw = Float.valueOf(file.getString(path + ".yaw"));
		else 
			Yaw = l.getYaw();

		if (file.contains(path + ".pitch"))
			Pitch = Float.valueOf(file.getString(path + ".pitch"));
		else 
			Pitch = l.getYaw();

		if (file.contains(path + ".gamemode"))
			Gamemode = toGameMode(file, player, path);
		else 
			Gamemode = player.getGameMode();
		// <-- IA moment XD
		// <-- Python moment XD

		return new WXYZYPGM(world, x, y, z, Yaw, Pitch, Gamemode); 
	}

	public WXYZYPGM getLoc(_Sender player, String path) {
		FileConfiguration config = plugin.getConfig();
		return getLoc(config, player, path);
	}
	
	public boolean IF(FileConfiguration file, String path) {
		return file.contains(path) && file.getBoolean(path);
	}
	
	public boolean IF(String path) {
		FileConfiguration config = plugin.getConfig();
		return IF(config, path);
	}

	public GameMode _toGameMode(String gamemode) {
		GameMode Gamemode;
		if (gamemode.equalsIgnoreCase("survival") || gamemode.equalsIgnoreCase("0")) {
			Gamemode = GameMode.SURVIVAL;

		} else if (gamemode.equalsIgnoreCase("creative") || gamemode.equalsIgnoreCase("1")) {
			Gamemode = GameMode.CREATIVE;

		} else if (gamemode.equalsIgnoreCase("adventure") || gamemode.equalsIgnoreCase("2")) {
			Gamemode = GameMode.ADVENTURE;

		} else if (gamemode.equalsIgnoreCase("spectator") || gamemode.equalsIgnoreCase("3")) {
			Gamemode = GameMode.SPECTATOR;

		} else {
			Gamemode = null;
		}

		return Gamemode;
	}
	
	public GameMode toGameMode(FileConfiguration file, _Sender player, String path) {
		String msg1, msg2, gamemode;
		GameMode Gamemode;

		if (file.contains(path + ".gamemode")) {
			gamemode = file.getString(path + ".gamemode");
		} else {
			gamemode = Bukkit.getServer().getDefaultGameMode().toString(); // multiverse core api por favor
		}

		Gamemode = _toGameMode(gamemode);
		if (Gamemode == null) {
			msg1 = ChatColor.translateAlternateColorCodes("&".charAt(0), "&e[&cError&e] &cValor invalido&f: &e%path% &f=&e &f'&b%gamemode%&f'");
			msg2 = plugin.name + ChatColor.GREEN + "   Por favor verifique su ortografia" + ChatColor.WHITE + ".";
			msg1 = msg1.replace("%path%", path);
			msg1 = msg1.replace("%gamemode%", gamemode);

			Bukkit.getConsoleSender().sendMessage(msg1); Bukkit.getConsoleSender().sendMessage(msg2);
			if (player.hasPermission("SetSpawnsWorlds.ConflictGamemode.show")) {
				player.sendMessage(msg1); player.sendMessage(msg2);
			}
		}
		return Gamemode;
	}
	
	public GameMode toGameMode(_Sender player, String path) {
		FileConfiguration config = plugin.getConfig();
		return toGameMode(config, player, path);
	}

	public void tpto(final _Sender player, final WXYZYPGM l) {
		new BukkitRunnable() {
			public void run() { player.teleport(l); }
		}.runTaskLater(plugin, 1L);

		new BukkitRunnable() {
			public void run() { player.setGameMode(l.getGamemode()); }
		}.runTaskLater(plugin, 1L);
	}

	public class _Sender { // extends Player // NO COMPATIBLY
		private CommandSender sender;
		public _Sender(CommandSender sender) {
			this.sender = sender;
		}

		public boolean isConsole() {
			return !isPlayer();
		}

		public boolean isPlayer() {
			return sender instanceof Player;
		}

		public void sendMessage(String s) {
			sender.sendMessage(s);
		}

		public Location getLocation() {
			if (isPlayer()) return getPlayer().getLocation();
			return null;
		}

		public GameMode getGameMode() {
			if (isPlayer()) return getPlayer().getGameMode();

			return null;
		}

		public String getName() {
			return sender.getName();
		}

		public boolean hasPermission(String s) {
			return sender.hasPermission(s);
		}

		public UUID getUniqueId() {
			if (isPlayer()) return getPlayer().getUniqueId();

			return null;
		}

		public Player getPlayer() {
			if (isPlayer()) return (Player) sender;
			return null;
		}

		public CommandSender getConsole() {
			if (isConsole()) return sender;
			return null;
		}
		
		public void setGameMode(GameMode Gamemode) {
			if (isPlayer()) getPlayer().setGameMode(Gamemode);
		}
		
		public void teleport(Location l) {
			if (isPlayer()) getPlayer().teleport(l);
		}
		
		public World getWorld() {
			if (isPlayer()) return getPlayer().getWorld();
			return null;
		}
		
		public Spigot spigot() {
			if (isPlayer()) return getPlayer().spigot();
			return null;
		}
	}
}
