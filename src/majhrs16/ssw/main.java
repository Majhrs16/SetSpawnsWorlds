package majhrs16.ssw;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import majhrs16.ssw.events.entry;
import majhrs16.ssw.events.exit;
import majhrs16.ssw.events.kills;
import majhrs16.ssw.events.respawn;
import majhrs16.ssw.events.setworld;
import majhrs16.ssw.commands.mainCommand;

public class main extends JavaPlugin {
	public String rutaConfig;
	private FileConfiguration players = null;
	private File playersFile          = null;
	PluginDescriptionFile pdffile     = getDescription();
	public String version             = pdffile.getVersion();
	public String name                = ChatColor.WHITE + "[" + ChatColor.GREEN + pdffile.getName() + ChatColor.WHITE + "]";

	public void onEnable() {
		Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "<------------------------->");
		Bukkit.getConsoleSender().sendMessage(name + ChatColor.translateAlternateColorCodes("&".charAt(0), "&a Activado&f. &7Version&f: &f(&a%version%&f)").replace("%version%", version));
		Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "<------------------------->");

		registerCommands();
		registerEvents();
		registerConfig();
		registerPlayers();

		updateChecker();
	}

	public void onDisable() {
		Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "<------------------------->");
		Bukkit.getConsoleSender().sendMessage(name + ChatColor.translateAlternateColorCodes("&".charAt(0), "&c Desactivado&f."));
		Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "<------------------------->");
	}

	public void registerCommands() {
//		this.getCommand("youtube").setExecutor(new youtube(this));
		this.getCommand("SetSpawnsWorlds").setExecutor(new mainCommand(this));
		this.getCommand("ssw").setExecutor(new mainCommand(this));
	}

	public void registerEvents() {
		PluginManager pe = getServer().getPluginManager();
		pe.registerEvents(new entry(this), this);
		pe.registerEvents(new exit(this), this);
		pe.registerEvents(new respawn(this), this);
		pe.registerEvents(new kills(this), this);
		pe.registerEvents(new setworld(this), this);
	}

	public void registerConfig() {
		File config = new File(this.getDataFolder(), "config.yml");
		rutaConfig  = config.getPath();
		
		if (!config.exists()) {
			this.getConfig().options().copyDefaults(true);
			saveConfig();
		}
	}

	public void updateChecker() {
		  try {
			  HttpURLConnection con = (HttpURLConnection) new URL("https://api.spigotmc.org/legacy/update.php?resource=106523").openConnection();
	          int timed_out         = 1250;
	          con.setConnectTimeout(timed_out);
	          con.setReadTimeout(timed_out);
	          String latestversion  = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
	          if (latestversion.length() <= 7) {
	        	  if (!version.equals(latestversion)) {
	        		  Bukkit.getConsoleSender().sendMessage(name + ChatColor.translateAlternateColorCodes("&".charAt(0), "&e Hay una nueva version disponible&f!. &f(&a%latestversion%&f)".replace("%latestversion%", latestversion)));
	        		  Bukkit.getConsoleSender().sendMessage(name + ChatColor.translateAlternateColorCodes("&".charAt(0), "&a  Puedes descargarla en &9https://www.spigotmc.org/resources/setspawnsworlds-abandonado.106523/"));
	        	  }
	          }

		  } catch (Exception ex) {
	          Bukkit.getConsoleSender().sendMessage(name + ChatColor.translateAlternateColorCodes("&".charAt(0), "&c Error al buscar actualizaciones&f."));
	      }
	  }

	/////////////////////////////////////////
	// Codigo para cada nuevo archivo.yml
	// Code for mew file.yml

	public FileConfiguration getPlayers() {
		if (players == null) {
			reloadPlayers();
		}

		return players;
	}
 
	public void reloadPlayers(){
		if (players == null) {
			playersFile = new File(getDataFolder(), "players.yml");
		}

		players = YamlConfiguration.loadConfiguration(playersFile);
		Reader defConfigStream;

		try {
			defConfigStream = new InputStreamReader(this.getResource("players.yml"), "UTF8");
			if (defConfigStream != null) {
				YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
				players.setDefaults(defConfig);
			}			

		} catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
	}
 
	public void savePlayers(){
		try {
			players.save(playersFile);

		} catch(IOException e){
			e.printStackTrace();
		}
	}
 
	public void registerPlayers(){
		playersFile = new File(this.getDataFolder(), "players.yml");
		if (!playersFile.exists()){
			this.getPlayers().options().copyDefaults(true);
			savePlayers();
		}
	}
}
