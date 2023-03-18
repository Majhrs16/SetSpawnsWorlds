package majhrs16.ssw.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import majhrs16.ssw.main;

public class youtube implements CommandExecutor{ // v1.1
	@SuppressWarnings("unused")
	private main plugin;
	public youtube(main plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command comando, String label, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			player.sendMessage(ChatColor.DARK_RED + "---------------------------------");
			player.sendMessage(ChatColor.GREEN + "Visita mi canal de youtube: " + ChatColor.RED + "https://www.youtube.com/@Majhrs16");
			player.sendMessage(ChatColor.DARK_RED + "---------------------------------");
			return true;
		} else {
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Mo puedes ejecutar comandos desde la consola");
			return false;
		}
		
	}
}
