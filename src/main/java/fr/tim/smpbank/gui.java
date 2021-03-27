package fr.tim.smpbank;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import fr.tim.smpbank.sql.HistorySQL;
import fr.tim.smpbank.sql.SQLGetter;


public class gui extends JavaPlugin {
	
	private static smpBank plugin = smpBank.getPlugin();
	
	public static void banque(Player player) {
		Inventory inv = Bukkit.createInventory(null, 27,ChatColor.GREEN + "Banque");
		
		new BukkitRunnable() {
			@Override
			public void run() {
				final float soldeAsync = SQLGetter.getPoints(player.getUniqueId());
				final float taux = HistorySQL.getTaux(0);
				utils.setItem(inv, 12, "minecraft:player_head", 1, ChatColor.GOLD + "Solde : ", "�d" + soldeAsync,0);
				ItemStack item = (ItemStack) inv.getItem(12);
				SkullMeta meta = (SkullMeta) inv.getItem(12).getItemMeta();
				meta.setOwningPlayer(player);
				item.setItemMeta(meta);
				inv.setItem(12, item);
				
				utils.setItem(inv, 9, "minecraft:nether_star", 1, ChatColor.YELLOW + "Taux du M-coin actuel :",ChatColor.GRAY + "" + taux,0);
				
			}
		}.runTaskAsynchronously(plugin);
				
		utils.setItem(inv, 7, "minecraft:diamond", 1, ChatColor.GREEN + "+1", ChatColor.GRAY + "D�poser 1 diamant",0);
		
		utils.setItem(inv, 16, "minecraft:diamond", 8, ChatColor.GREEN + "+8", ChatColor.GRAY + "D�poser 8 diamants",0);
		
		utils.setItem(inv, 25, "minecraft:diamond", 64, ChatColor.GREEN + "+64", ChatColor.GRAY + "D�poser 64 diamants",0);
		
		utils.setItem(inv, 15, "minecraft:diamond_block", 1, ChatColor.GREEN + "All", ChatColor.GRAY + "Tout d�poser",0);
		
		utils.setItem(inv, 5, "minecraft:emerald", 1, ChatColor.RED + "-1", ChatColor.GRAY + "Retirer 1 M-coin",42);
		
		utils.setItem(inv, 14, "minecraft:emerald", 8, ChatColor.RED + "-8", ChatColor.GRAY + "Retirer 8 M-coins",42);
		
		utils.setItem(inv, 23, "minecraft:emerald", 64, ChatColor.RED + "-64", ChatColor.GRAY + "Retirer 64 M-coins",42);
		
		player.openInventory(inv);
		}
	
	
	
}
