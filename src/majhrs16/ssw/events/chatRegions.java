package majhrs16.ssw.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class chatRegions implements Listener{
	@EventHandler
	public void _chatRegions(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		RegionManager regionManager = WGBukkit.getRegionManager(player.getWorld());
		ApplicableRegionSet set = regionManager.getApplicableRegions(player.getLocation());
		for (ProtectedRegion region : set) {
			if (region.getId().equals("spawn")) {
				for (Player p : Bukkit.getOnlinePlayers()) {
					RegionManager regionManager2 = WGBukkit.getRegionManager(player.getWorld());
					ApplicableRegionSet set2 = regionManager2.getApplicableRegions(player.getLocation());
					if (set2.size() == 0) {
						event.getRecipients().remove(p);
					} else {
						for (@SuppressWarnings("unused") ProtectedRegion region2 : set2) {
							if (region.getId().equals("spawn")) {
								event.getRecipients().remove(p);								
							}
						}
					}
				}
				return;
			}
		}
	}
}

/*
		////////////
		// ESTO SIRVE PARA SSW v1.9
		Location location = new Location(Bukkit.getServer().getWorld(""), 0, 0, 0);
		RegionManager regionManager = WGBukkit.getRegionManager(location.getWorld());

		ApplicableRegionSet set = regionManager.getApplicableRegions(location);
		for (ProtectedRegion region : set.getRegions()) {
			
		}
 */