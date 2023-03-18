package majhrs16.ssw.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import majhrs16.ssw.main;
import majhrs16.ssw.util;

public class kills implements Listener{
	private util util;
	public kills(main plugin) {
		this.util = new util(plugin);
	}

	@SuppressWarnings({ "static-access", "deprecation" })
	@EventHandler
	public void skull(EntityDeathEvent event) {
		EntityType entity        = event.getEntityType();
		Player killer            = event.getEntity().getKiller();
		if(killer != null && killer.getType().equals(EntityType.PLAYER) && entity.equals(entity.PLAYER) && util.IF("respawn.GivePlayerSkull")) {
			ItemStack stack   = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
			ItemMeta meta     = stack.getItemMeta();
			meta.setDisplayName(ChatColor.DARK_RED + event.getEntity().getName());
			List<String> lore = new ArrayList<String>();
			lore.add(ChatColor.translateAlternateColorCodes("&".charAt(0), "&4&m                                       "));
			lore.add("");
			lore.add(ChatColor.DARK_PURPLE +                                   "Se te ha otorgado esta cabeza por tu gran fuerza.");
			lore.add("");
			lore.add(ChatColor.translateAlternateColorCodes("&".charAt(0), "&4&m                                       "));
			meta.setLore(lore);
			meta.addEnchant(Enchantment.DAMAGE_ALL, 0, false);
			stack.setItemMeta(meta);

			if(killer.getInventory().firstEmpty() == -1) {
				killer.sendMessage(ChatColor.RED + "No es posible otorgarte la cabeza de " + entity.getName());
				killer.sendMessage(ChatColor.YELLOW + "  Razon: Inventario lleno.");
			} else {
				killer.getInventory().addItem(stack);
			}

			Bukkit.broadcastMessage("El jugador " + killer.getName() + " ha matado a " + event.getEntity().getName());
		}
	}
}