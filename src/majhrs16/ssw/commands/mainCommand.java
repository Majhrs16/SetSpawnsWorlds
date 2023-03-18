package majhrs16.ssw.commands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import majhrs16.ssw.main;
import majhrs16.ssw.util;

public class mainCommand implements CommandExecutor {
	private main plugin;
	private util util;
	public mainCommand(main plugin) {
		this.plugin = plugin;
		this.util   = new util(plugin);
	}

	public String getFormatedData(util.WXYZYPGM Data) {
		String wXYZyp = "&aMundo&f: &f'&b%world%&f'&f, &aX&f: &b%x%&f, &aY&f: &b%y%&f, &a&Z&f: &b%z%&f, &aYaw&f: &b%yaw%&f, &aPitch&f: &b%pitch%&f, &aGamemode&f: &b%gamemode%";
		wXYZyp        = ChatColor.translateAlternateColorCodes("&".charAt(0), wXYZyp);
		wXYZyp        = wXYZyp.replace("%world%", Data.getWorld().getName());
		wXYZyp        = wXYZyp.replace("%x%", "" + Data.getX());
		wXYZyp        = wXYZyp.replace("%y%", "" + Data.getY());
		wXYZyp        = wXYZyp.replace("%z%", "" + Data.getZ());
		wXYZyp        = wXYZyp.replace("%yaw%", "" + Data.getYaw());
		wXYZyp        = wXYZyp.replace("%pitch%", "" + Data.getPitch());
		wXYZyp        = wXYZyp.replace("%gamemode%", Data.getGamemode().toString());
		return wXYZyp;
	}

	public void showHelp(util._Sender player) {
		ArrayList<String> msg = new ArrayList<String>();
		msg.add("&e/ssw &aMuestra este mensaje de ayuda&f.");
		msg.add("&e  help &aMuestra este mensaje de ayuda&f.");
		msg.add("&e  spawn &aIr al spawn&f.");
		msg.add("&e  reload &aRecargar config&f.");
		msg.add("&e  infoplayer &6[Jugador] &aObtiene informacion detallada de un jugador&f.");
		msg.add("&e  version &aVersion&f.");
		msg.add("&e  set &aAgrega o modifica&f...");
		msg.add("&e    spawn &6[Mundo [X Y Z [YAW PITCH [Gamemode]]] &aEstablecer punto de spawn. Tepea al jugador al entrar al server&f.");
		msg.add("&e    lobby &6[Mundo [X Y Z [YAW PITCH [Gamemode]]] &aEstablecer punto de lobby. Tepea al punto establecido al entrar al mundo especificado&f.");
		msg.add("&e    lastcord &6[Mundo [X Y Z [YAW PITCH [Gamemode]]] &aActiva el guardado de ultima ubicacion del jugador en el mundo especificado&f.");
		msg.add("&e    respawn &6[Mundo [X Y Z [YAW PITCH [Gamemode]]] &aEstablecer punto de respawn para el mundo especificado&f.");
		msg.add("&e  del &cElimina&f...");
		msg.add("&e    spawn &6[Mundo] &cElimina el punto de spawn&f.");
		msg.add("&e    lobby &6[Mundo] &cElimina el lobby para el mundo especificado&f.");
		msg.add("&e    lastcord &6[Mundo] &cDesactiva el guardado de ultima ubicacion del jugador en el mundo especificado&f.");
		msg.add("&e    respawn &6[Mundo] &cElimina el punto de respawn para el mundo especificado&f.");
		msg.add("");
		msg.add("&aEjemplo&f:");
		msg.add("&e  /ssw set spawn &6world 1 ~ 6 160 80 survival&f.");
		for (int i = 0; i < msg.size(); i++) {
			String s = msg.get(i);
			if (s.length() > 0) s = plugin.name + ChatColor.translateAlternateColorCodes("&".charAt(0), s);

			player.sendMessage(s);
		}
	}

	public util.WXYZYPGM getCords(util._Sender player, String[] args) {
//		1   2     3     4 5 6 7 8 9
//		0   1     2     3 4 5 6 7 8
//		set spawn world x y z y p gm
		World world;
		Double x, y, z;
		Float Yaw, Pitch;
		String gamemode;

		Boolean Default = false;
		Location l      = player.getLocation();

		try {
			if (args.length < 9) {
				Default = true;				
			}

			if ((Default || args[2].equals("~")) && player.isPlayer()) {
				world    = l.getWorld();
			} else {
				world    = plugin.getServer().getWorld(args[2]);
			}

			if ((Default || args[3].equals("~")) && player.isPlayer()) {
				x        = l.getX();
			} else {
				x        = Double.valueOf(args[3]);
			}

			if ((Default || args[4].equals("~")) && player.isPlayer()) {
				y        = l.getY();
			} else {
				y        = Double.valueOf(args[4]);
			}

			if ((Default || args[5].equals("~")) && player.isPlayer()) {
				z        = l.getZ();
			} else {
				z        = Double.valueOf(args[5]);
			}

			if ((Default || args[6].equals("~")) && player.isPlayer()) {
				Yaw      = l.getYaw();
			} else {
				Yaw      = Float.valueOf(args[6]);
			}

			if ((Default || args[7].equals("~")) && player.isPlayer()) {
				Pitch    = l.getPitch();
			} else {
				Pitch    = Float.valueOf(args[7]);
			}

			if ((Default || args[8].equals("~")) && player.isPlayer()) {
				gamemode = player.getGameMode().toString();
			} else {
				gamemode = args[8];
			}
			
		} catch (Exception e) {
			Bukkit.getConsoleSender().sendMessage(plugin.name + ChatColor.RED + " Se ha detectado un error de conversion de datos" + ChatColor.WHITE + ".");
//			Bukkit.getConsoleSender().sendMessage(plugin.name + ChatColor.YELLOW + "   Por favor verifique sus datos" + ChatColor.WHITE + ".");
			e.printStackTrace();
			return null;
		}

		return util.new WXYZYPGM(world, x, y, z, Yaw, Pitch, util._toGameMode(gamemode));
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		util.WXYZYPGM Data;
		String msg1, msg2, path;
		FileConfiguration config = plugin.getConfig();

		util._Sender player = util.new _Sender(sender);
		Location l          = player.getLocation();

		if(args.length > 0) {
			if (args[0].equalsIgnoreCase("help")) {
				showHelp(player);
				return true;

			} else if (args[0].equalsIgnoreCase("spawn")) {
				path = "spawn";
				if (config.contains(path + ".x")) {
					util.tpto(player, util.getLoc(player, path));

				} else {
					player.sendMessage(plugin.name + ChatColor.RED +   " El spawn no ha sido establecido!");
					player.sendMessage(plugin.name + ChatColor.GREEN + "   Establezcalo con " + ChatColor.YELLOW + "/ssw set spawn");
				}

				return true;

			} else if (args[0].equalsIgnoreCase("reload")) {
				player.sendMessage(plugin.name + ChatColor.YELLOW + " Recargando config...");
				plugin.reloadConfig();
				plugin.reloadPlayers();
				player.sendMessage(plugin.name + ChatColor.GREEN + " La config ha sido recargada correctamente.");
				return true;

			} else if(args[0].equalsIgnoreCase("infoplayer")) {
				if (!player.hasPermission("ssw.infoplayer.get")) {
					player.sendMessage(plugin.name + ChatColor.RED + " Usted no tiene permisos para ejecutar este comando.");
					return false;
				}

				try {
					String nick;
					if (args.length > 1) {
						nick             = args[1];
					} else {
						nick             = player.getName();
					}

					if (nick.startsWith("\"") && nick.endsWith("\"")) {
						nick             = nick.substring(1, args[1].length() - 1);
					}

					Player player2       = Bukkit.getServer().getPlayer(nick); // explode
					l                    = player2.getLocation();

					player.sendMessage(plugin.name + ChatColor.translateAlternateColorCodes("&".charAt(0), " &aNombre&f: &f'&b%player%&f',").replace("%player%", player2.getName() ));
					player.sendMessage(plugin.name + ChatColor.translateAlternateColorCodes("&".charAt(0), "   &aMundo&f:  &f'&b%world%&f',").replace("%world%", l.getWorld().getName() ));
					player.sendMessage(plugin.name + ChatColor.translateAlternateColorCodes("&".charAt(0), "   &aX&f: &b%x%&f,".replace("%x%", "" + l.getX()).replace(".", "&f.&b") ));
					player.sendMessage(plugin.name + ChatColor.translateAlternateColorCodes("&".charAt(0), "   &aY&f: &b%y%&f,".replace("%y%", "" + l.getY()).replace(".", "&f.&b") ));
					player.sendMessage(plugin.name + ChatColor.translateAlternateColorCodes("&".charAt(0), "   &aZ&f: &b%z%&f,".replace("%z%", "" + l.getZ()).replace(".", "&f.&b") ));
					player.sendMessage(plugin.name + ChatColor.translateAlternateColorCodes("&".charAt(0), "   &aYaw&f: &b%yaw%&f,".replace("%yaw%", "" + l.getYaw()).replace(".", "&f.&b") ));
					player.sendMessage(plugin.name + ChatColor.translateAlternateColorCodes("&".charAt(0), "   &aPitch&f: &b%pitch%&f,".replace("%pitch%", "" + l.getPitch()).replace(".", "&f.&b") ));
					player.sendMessage(plugin.name + ChatColor.translateAlternateColorCodes("&".charAt(0), "   &aIP&f: &b%ip%&f,".replace("%ip%", player2.getAddress().getAddress().getHostAddress()).replace(".", "&f.&b") ));
					player.sendMessage(plugin.name + ChatColor.translateAlternateColorCodes("&".charAt(0), "   &aPuerto&f: &b%port%&f.".replace("%port%", "" + player2.getAddress().getPort()) ));
					return true;

				} catch (NullPointerException e) {
					player.sendMessage(plugin.name + ChatColor.RED + " Ese jugador no existe" + ChatColor.WHITE + ".");
				}

				return false;

			} else if (args[0].equalsIgnoreCase("version")) {
				player.sendMessage(plugin.name + ChatColor.WHITE + " Version" + ChatColor.WHITE + ": " + ChatColor.GREEN + plugin.version);
				return true;

			} else if (args[0].equalsIgnoreCase("set")) {
				if(args.length < 2) {
					showHelp(player);
					return false;
				}

				if (!player.hasPermission("ssw.set")) {
					player.sendMessage(plugin.name + ChatColor.RED + " Usted no tiene permisos para ejecutar este comando.");
					return false;
				}

				if (args[1].equalsIgnoreCase("spawn")) {
					Data = getCords(player, args);
					if (Data == null) {
						player.sendMessage(plugin.name + ChatColor.translateAlternateColorCodes("&".charAt(0), " &cSintaxis invalida&f. &aPor favor use la sintaxis&f: &e/ssw set spawn &6[Mundo [X Y Z [Yaw Pitch [Gamemode]]]]"));
						return false;
					}

					path = "spawn";
					config.set(path + ".world", Data.getWorld().getName());
					config.set(path + ".x", Data.getX());
					config.set(path + ".y", Data.getY());
					config.set(path + ".z", Data.getZ());
					config.set(path + ".yaw", Data.getYaw());
					config.set(path + ".pitch", Data.getPitch());
					config.set(path + ".gamemode", Data.getGamemode().toString());
					if (!config.contains(path + ".Force")) {
						config.set(path + ".Force", "true");
					}
					plugin.saveConfig();

					msg2 = plugin.name + "   " + getFormatedData(Data);
					if (util.IF(config, "broadcast-actions")) {
						msg1 = plugin.name + ChatColor.translateAlternateColorCodes("&".charAt(0), " &f'&b%player%&f' &7Ha &aestablecido &7el punto de spawn en&f:");
						msg1 = msg1.replace("%player%", player.getName());
						Bukkit.broadcastMessage(msg1);
						Bukkit.broadcastMessage(msg2);

					} else {
						player.sendMessage(plugin.name + ChatColor.translateAlternateColorCodes("&".charAt(0), " &7Se ha &aestablecido &7el punto de spawn en&f:"));
						player.sendMessage(msg2);
					}

					return true;

				} else if(args[1].equalsIgnoreCase("lobby")) {
					Data = getCords(player, args);
					if (Data == null) {
						player.sendMessage(plugin.name + ChatColor.translateAlternateColorCodes("&".charAt(0), " &cSintaxis invalida&f. &aPor favor use la sintaxis&f: &e/ssw set lobby &6[Mundo [X Y Z [Yaw Pitch [Gamemode]]]]"));
						return false;
					}

					path = "spawn.Lobbys." + Data.getWorld().getName();
					config.set(path + ".x", Data.getX());
					config.set(path + ".y", Data.getY());
					config.set(path + ".z", Data.getZ());
					config.set(path + ".yaw", Data.getYaw());
					config.set(path + ".pitch", Data.getPitch());
					config.set(path + ".gamemode", Data.getGamemode().toString());
					plugin.saveConfig();

					msg2 = plugin.name + "   " + getFormatedData(Data);
					if (util.IF(config, "broadcast-actions")) {
						msg1 = plugin.name + ChatColor.translateAlternateColorCodes("&".charAt(0), " &f'&b%player%&f' &7Ha &aestablecido &7el lobby en&f:");
						msg1 = msg1.replace("%player%", player.getName());
						Bukkit.broadcastMessage(msg1);
						Bukkit.broadcastMessage(msg2);
					} else {
						player.sendMessage(plugin.name + ChatColor.translateAlternateColorCodes("&".charAt(0), " &7Se ha &aestablecido &7el lobby en&f:"));
						player.sendMessage(msg2);
					}

					return true;

				} else if (args[1].equalsIgnoreCase("lastcord")) {
					Data = getCords(player, args);
					if (Data == null) {
						player.sendMessage(plugin.name + ChatColor.translateAlternateColorCodes("&".charAt(0), " &cSintaxis invalida&f. &aPor favor use la sintaxis&f: &e/ssw set lastcord &6[Mundo [X Y Z [Yaw Pitch [Gamemode]]]]"));
						return false;
					}

					path = "spawn.TpLastCords." + Data.getWorld().getName();
					config.set(path + ".x", Data.getX());
					config.set(path + ".y", Data.getY());
					config.set(path + ".z", Data.getZ());
					config.set(path + ".yaw", Data.getYaw());
					config.set(path + ".pitch", Data.getPitch());
					config.set(path + ".gamemode", Data.getGamemode().toString());
					plugin.saveConfig();

					if (util.IF(config, "broadcast-actions")) {
						msg1 = plugin.name + ChatColor.translateAlternateColorCodes("&".charAt(0), " &f'&b%player%&f' &7Ha &aactivado &7el mundo &f'&b%world%&f' &7como lastcord&f.");
						msg1 = msg1.replace("%player%", player.getName());
						msg1 = msg1.replace("%world%", Data.getWorld().getName());
						Bukkit.broadcastMessage(msg1);

					} else {
						msg1 = plugin.name + ChatColor.translateAlternateColorCodes("&".charAt(0), " &7Se ha &aactivado &7el mundo &f'&b%world%&f' &7como lastcord&f.");
						msg1 = msg1.replace("%world%", Data.getWorld().getName());
						player.sendMessage(msg1);
					}

					return true;

				} else if (args[1].equalsIgnoreCase("respawn")) {
					if (!config.contains("respawn.GivePlayerSkull")) {
						config.set("respawn.GivePlayerSkull", "true");
					}

					Data = getCords(player, args);
					if (Data == null) {
						player.sendMessage(plugin.name + ChatColor.translateAlternateColorCodes("&".charAt(0), " &cSintaxis invalida&f. &aPor favor use la sintaxis&f: &e/ssw set respawn &c[Mundo [X Y Z [Yaw Pitch [Gamemode]]]]"));
						return false;
					}

					path = "respawn." + Data.getWorld().getName();
					config.set(path + ".world", Data.getWorld().getName());
					config.set(path + ".x", Data.getX());
					config.set(path + ".y", Data.getY());
					config.set(path + ".z", Data.getZ());
					config.set(path + ".yaw", Data.getYaw());
					config.set(path + ".pitch", Data.getPitch());
					config.set(path + ".gamemode", Data.getGamemode().toString());
					config.set(path + ".Force", "false");
					plugin.saveConfig();

					msg2 = plugin.name + "   " + getFormatedData(Data);
					if (util.IF(config, "broadcast-actions")) {
						msg1 = plugin.name + ChatColor.translateAlternateColorCodes("&".charAt(0), " &f'&b%player%&f' &7Ha &aestablecido &7el punto de respawn en&f:");
						msg1 = msg1.replace("%player%", player.getName());
						Bukkit.broadcastMessage(msg1);
						Bukkit.broadcastMessage(msg2);

					} else {
						player.sendMessage(plugin.name + ChatColor.translateAlternateColorCodes("&".charAt(0), " &7Se ha &aestablecido &7el punto de respawn en&f:"));
						player.sendMessage(msg2);
					}

					return true;

				} else {
					player.sendMessage(plugin.name + ChatColor.RED + " ese comando no existe!");
					player.sendMessage(plugin.name + ChatColor.translateAlternateColorCodes("&".charAt(0), "   &aPor favor use el comando&f: &e/ssw help"));
					return false;
				}

			} else if (args[0].equalsIgnoreCase("del")) {
				String World;
				if (args.length > 2) {
					World = args[2];

				} else {
					World = l.getWorld().getName();
				}
				
				if (!player.hasPermission("ssw.set")) {
					player.sendMessage(plugin.name + ChatColor.RED + " Usted no tiene permisos para ejecutar este comando" + ChatColor.WHITE + ".");
					return false;
				}
				
				if (args[1].equalsIgnoreCase("spawn")) {
					path = "spawn";
					if (config.contains(path + ".world") && config.getString(path + ".world").equals(World)) {
						config.set(path + ".world", null);
						config.set(path + ".x", null);
						config.set(path + ".y", null);
						config.set(path + ".z", null);
						config.set(path + ".yaw", null);
						config.set(path + ".pitch", null);
						config.set(path + ".gamemode", null);
						if (!config.contains(path + ".Lobbys") && !config.contains(path + ".TpLastCords")) {
							config.set(path, null);
						}
						plugin.saveConfig();

					} else {
						player.sendMessage(plugin.name + ChatColor.RED + " El mundo especificado no esta como spawn" + ChatColor.WHITE + ".");
						World = null;
						return false;
					}

					if (util.IF(config, "broadcast-actions")) {
						msg1 = plugin.name + ChatColor.translateAlternateColorCodes("&".charAt(0), " &f'&b%player%&f' &fHa &celiminado &fel mundo &f'&b%world%&f' &fcomo spawn&f.");
						msg1 = msg1.replace("%player%", player.getName());
						msg1 = msg1.replace("%world%", World);
						Bukkit.broadcastMessage(msg1);

					} else {
						msg1 = plugin.name + ChatColor.translateAlternateColorCodes("&".charAt(0), " &fSe ha &celiminado &fel mundo &f'&b%world%&f' &fcomo spawn&f.");
						msg1 = msg1.replace("%world%", World);
						player.sendMessage(msg1);
					}
					return true;

				} else if (args[1].equalsIgnoreCase("lobby")) {
					path = "spawn.Lobbys." + World;
					if (config.contains(path)) {
						config.set(path + ".x", null);
						config.set(path + ".y", null);
						config.set(path + ".z", null);
						config.set(path + ".yaw", null);
						config.set(path + ".pitch", null);
						config.set(path + ".gamemode", null);
						config.set(path, null);
						plugin.saveConfig();

					} else {
						player.sendMessage(plugin.name + ChatColor.RED + " El mundo especificado no esta como lobby" + ChatColor.WHITE + ".");
						World = null;
						return false;
					}
					
					if (util.IF(config, "broadcast-actions")) {
						msg1 = plugin.name + ChatColor.translateAlternateColorCodes("&".charAt(0), " &f'&b%player%&f' &fHa &celiminado &fel mundo &f'&b%world%&f' &acomo lobby&f.");
						msg1 = msg1.replace("%player%", player.getName());
						msg1 = msg1.replace("%world%", World);
						Bukkit.broadcastMessage(msg1);

					} else {
						msg1 = plugin.name + ChatColor.translateAlternateColorCodes("&".charAt(0), " &fSe ha &cdesactivado &fel mundo &f'&b%world%&f' &fcomo lobby&f.");
						msg1 = msg1.replace("%world%", World);
						player.sendMessage(msg1);
					}
					return true;

				} else if (args[1].equalsIgnoreCase("lastcord")) {
					path = "spawn.TpLastCords." + World;
					if (config.contains(path)) {
						config.set(path + ".x", null);
						config.set(path + ".y", null);
						config.set(path + ".z", null);
						config.set(path + ".yaw", null);
						config.set(path + ".pitch", null);
						config.set(path + ".gamemode", null);
						config.set(path, null);
						plugin.saveConfig();

					} else {
						player.sendMessage(plugin.name + ChatColor.RED + " El mundo especificado no esta como lastcord" + ChatColor.WHITE + ".");
						World = null;
						return false;
					}
					
					if (util.IF(config, "broadcast-actions")) {
						msg1 = plugin.name + ChatColor.translateAlternateColorCodes("&".charAt(0), " &f'&b%player%&f' &aHa &cdesactivado &ael mundo &f'%world%&f' &acomo lastcord&f.");
						msg1 = msg1.replace("%player%", player.getName());
						msg1 = msg1.replace("%world%", World);
						Bukkit.broadcastMessage(msg1);

					} else {
						msg1 = plugin.name + ChatColor.translateAlternateColorCodes("&".charAt(0), " &fSe ha &cdesactivado &fel mundo &f'%world%&f' &fcomo lastcord&f.");
						msg1 = msg1.replace("%world%", World);
						player.sendMessage(msg1);
					}
					return true;

				} else if (args[1].equalsIgnoreCase("respawn")) {
					path = "respawn." + World;
					if (config.contains(path)) {
						config.set(path + ".world", null);
						config.set(path + ".x", null);
						config.set(path + ".y", null);
						config.set(path + ".z", null);
						config.set(path + ".yaw", null);
						config.set(path + ".pitch", null);
						config.set(path + ".gamemode", null);
						config.set(path, null);
						plugin.saveConfig();

					} else {
						player.sendMessage(plugin.name + ChatColor.RED + " El mundo especificado no esta como respawn" + ChatColor.WHITE + ".");
						World = null;
						return false;
					}
					
					if (util.IF(config, "broadcast-actions")) {
						msg1 = plugin.name + ChatColor.translateAlternateColorCodes("&".charAt(0), " &f'&b%player%&f' &aHa &cdesactivado &ael mundo &f'%world%&f' &acomo respawn&f.");
						msg1 = msg1.replace("%player%", player.getName());
						msg1 = msg1.replace("%world%", World);
						Bukkit.broadcastMessage(msg1);

					} else {
						msg1 = plugin.name + ChatColor.translateAlternateColorCodes("&".charAt(0), " &fSe ha &cdesactivado &fel mundo &f'%world%&f' &fcomo respawn&f.");
						msg1 = msg1.replace("%world%", World);
						player.sendMessage(msg1);
					}
					return true;

				} else {
					player.sendMessage(plugin.name + ChatColor.RED + " ese comando no existe!");
					player.sendMessage(plugin.name + ChatColor.translateAlternateColorCodes("&".charAt(0), "   &aPor favor use el comando&f: &e/ssw help"));
					return false;
				}

			} else {
				player.sendMessage(plugin.name + ChatColor.RED + " ese comando no existe!");
				player.sendMessage(plugin.name + ChatColor.translateAlternateColorCodes("&".charAt(0), "   &aPor favor use el comando&f: &e/ssw help"));
				return false;
			}

		} else {
			showHelp(player);
			return true;
		}
	}
}