package fr.tim.smpbank;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

import fr.tim.smpbank.sql.HistorySQL;
import fr.tim.smpbank.sql.SQLGetter;


public class utils {
	
	private static smpBank plugin = smpBank.getPlugin();

	
	//Place un item dans une case sp�cifique de l'inventaire
	public static void setItem(Inventory inv, int pos, String id, int amount, String name, String lore,int modelData) {
		ItemStack item = new ItemStack(Material.matchMaterial(id),amount);
		ItemMeta meta = item.getItemMeta();
		List<String> Lore = new ArrayList<String>();
		Lore.add(lore);
		
		meta.setDisplayName(name);
		meta.setLore(Lore);
		meta.setCustomModelData(modelData);
		item.setItemMeta(meta);
		inv.setItem(pos, item);
	}
	
	
	//D�pose n diamants
	public static void deposit(Player player,int n) {
		//M-coin non d�tect�
		
		if (!player.getInventory().contains(Material.EMERALD) && !player.getInventory().contains(Material.DIAMOND)) return;
		
		ItemStack mcoin = new ItemStack(Material.EMERALD,n);
		ItemMeta Mmeta = mcoin.getItemMeta();
		Mmeta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "M-Coin");
		Mmeta.setCustomModelData(42);
		mcoin.setItemMeta(Mmeta);
		
		int totalDiamond = 0;
		int totalMcoin = 0;
		int diamond = 0;
		int coin = 0;
		
		for (ItemStack item : player.getInventory().getContents()) {
			if (item == null) continue;
			if (item.isSimilar(mcoin)) totalMcoin += item.getAmount();
			if (item.isSimilar(new ItemStack(Material.DIAMOND))) totalDiamond += item.getAmount();
		}
				
		if (n>=2304) {
			player.getInventory().remove(mcoin);
			player.getInventory().remove(new ItemStack(Material.DIAMOND));
			
			diamond = totalDiamond;
			coin = totalMcoin;
		}

		int reste = 0;
		HashMap<Integer,ItemStack> notRemoved = player.getInventory().removeItem(mcoin);
		if (notRemoved.size()==0) {
			coin = n;
		} else {
			for (ItemStack item : notRemoved.values()) {
				if (item.isSimilar(mcoin)) {
					reste += item.getAmount();
				}
			}
			coin = totalMcoin;
			notRemoved.clear();
			notRemoved = player.getInventory().removeItem(new ItemStack(Material.DIAMOND,reste));
			
			if (notRemoved.size()==0) {
				diamond = reste;
			} else {
				diamond = totalDiamond;
			}
			notRemoved.clear();
		}
		
		final int depositD = diamond;
		final int depositM = coin;
		
		new BukkitRunnable() {
			@Override
			public void run() {
				float stonk = HistorySQL.getTaux(0);
				SQLGetter.addPoints(player.getUniqueId(),depositD*stonk + depositM);
				
				if (player.getOpenInventory() != null) {
					Inventory inv = player.getOpenInventory().getTopInventory();
					String soldeS = player.getOpenInventory().getTopInventory().getItem(12).getItemMeta().getLore().toString();
					soldeS = soldeS.replace("[", "");
					soldeS = soldeS.replace("]", "");
					float solde = Float.valueOf(ChatColor.stripColor(soldeS));
					utils.setItem(inv, 12, "minecraft:player_head", 1, ChatColor.GOLD + "Solde : ", "�d" + (float)Math.round((solde+depositD*stonk + depositM)*1000)/1000,0);
					ItemStack item = (ItemStack) inv.getItem(12);
					SkullMeta meta = (SkullMeta) inv.getItem(12).getItemMeta();
					meta.setOwningPlayer(player);
					item.setItemMeta(meta);
					inv.setItem(12, item);
				}
			}
		}.runTaskAsynchronously(plugin);
		
		return;
	}
	
	public static void withdraw(Player player, int n) {
		//Revoir syst�me de stack
		
		ItemStack mcoin = new ItemStack(Material.EMERALD);
		ItemMeta Mmeta = mcoin.getItemMeta();
		Mmeta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "M-Coin");
		Mmeta.setCustomModelData(42);
		mcoin.setItemMeta(Mmeta);
		
		if (player.getInventory().firstEmpty() == -1 && !player.getInventory().contains(Material.EMERALD)) return;
		new BukkitRunnable() {
			@Override
			public void run() {
				int thune = 0;
				if (n < SQLGetter.getPoints(player.getUniqueId())) {
					thune = n;
				} else {
					thune = (int) Math.floor(SQLGetter.getPoints(player.getUniqueId()));
				}
				SQLGetter.addPoints(player.getUniqueId(), -1*thune);
				
				for (ItemStack item : player.getInventory().getContents()) {
					if (!player.getInventory().contains(mcoin) || thune <= 0) break;
					if (item.isSimilar(mcoin)) {
						int diff = 64 - item.getAmount();
						if (thune>=diff) {
							item.setAmount(64);
							thune -= diff;
						}
					}
				}
				
				if (thune > 0) {
					mcoin.setAmount(thune);
					player.getInventory().addItem(mcoin);
				}
				
			}
		}.runTaskAsynchronously(plugin);
		return;
	}
	
	
	public static String getPrevDay(int minusDay) {
		LocalDate date = LocalDate.now().minusDays(minusDay);
		return date.getDayOfMonth() + "_" + date.getMonthValue() + "_" + date.getYear();
	}
	
	
}
