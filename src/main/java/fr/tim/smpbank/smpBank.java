package fr.tim.smpbank;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import fr.tim.smpbank.sql.HistorySQL;
import fr.tim.smpbank.sql.MySQL;
import fr.tim.smpbank.sql.SQLGetter;


public class smpBank extends JavaPlugin implements Listener {
	
	private static smpBank plugin;
	
	//Au lancement
	@Override
	public void onEnable() { 
		plugin = this;
		
		new BukkitRunnable() {
			@Override
			public void run() {
				//Connexion
				try {
					MySQL.connect();
				} catch (ClassNotFoundException | SQLException e) {
					System.out.println("Database non connectee");
				}
				
				new BukkitRunnable() {
					@Override
					public void run() {
						//Confirmation de connexion
						if (MySQL.isConnected()) {
							System.out.println("Database connectee");
							SQLGetter.createTable();
							HistorySQL.createHistory();
							HistorySQL.createTaux();
					}
				}
				
				}.runTask(plugin);
			}
		}.runTaskAsynchronously(this);
		
		//Activation des listener
		this.getServer().getPluginManager().registerEvents(this, this);
		
		long ticktime = (long) 1728000 - ((java.time.LocalDateTime.now().getSecond() + (java.time.LocalDateTime.now().getMinute())*60 + (java.time.LocalDateTime.now().getHour()) * 3600) * 20);
		
		new BukkitRunnable() {
			@Override
			public void run() {
				Bukkit.broadcastMessage("�2Sauvegarde des donnees...");
				HistorySQL.saveDay();
				
				
				float stonks = HistorySQL.getTaux(1);
				float banque = HistorySQL.getBanque(1);
				float variation = HistorySQL.getVariation();
				float playerToday = joined.size();
				
				float newTaux = taux.newTaux(0f,stonks,playerToday,banque,variation);
				
				HistorySQL.setTaux(newTaux);
				
				joined.clear();
				
				Bukkit.broadcastMessage("Taux hier := " + stonks );
				Bukkit.broadcastMessage("Banque hier := " + banque );
				Bukkit.broadcastMessage("Variation hier-avant hier := " + variation );
				Bukkit.broadcastMessage("Joueurs := " + playerToday );
				
				
			}
		}.runTaskTimerAsynchronously(this,ticktime,1728000L);
	}
	
	
	@Override
	public void onDisable() {
		//Deconnexion
		MySQL.disconnect();
	}
	
	public HashMap<UUID, Boolean> joined = new HashMap<>();
	
	@EventHandler()
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		SQLGetter.createPlayer(player);
		HistorySQL.createPlayer(player);
		
		joined.put(player.getUniqueId(), true);
	}
	
	
		
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (label.equalsIgnoreCase("gui")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("Action non autoris�e");
				return true;
			}
			
			Player player = (Player) sender;
			Inventory inv = Bukkit.createInventory(null, 27);
			player.openInventory(inv);
			
			gui.banque(player);
			
			return true;
		}
		
		if (label.equalsIgnoreCase("forcesave")) {
			Bukkit.broadcastMessage("�2Sauvegarde des donnees...");
			HistorySQL.saveDay();
			
			
			float stonks = HistorySQL.getTaux(1);
			float banque = HistorySQL.getBanque(1);
			float variation = HistorySQL.getVariation();
			float playerToday = joined.size();
			
			float newTaux = taux.newTaux(0f,stonks,playerToday,banque,variation);
			
			HistorySQL.setTaux(newTaux);
			
			joined.clear();
			
			Bukkit.broadcastMessage("Taux hier := " + stonks );
			Bukkit.broadcastMessage("Banque hier := " + banque );
			Bukkit.broadcastMessage("Variation hier-avant hier := " + variation );
			Bukkit.broadcastMessage("Joueurs := " + playerToday );
		}
		
		
		return false;
	}
	
	
	@EventHandler()
	public void onClick(InventoryClickEvent event) {
		
		Player player = (Player) event.getWhoClicked();
		ItemStack item = (ItemStack) event.getCurrentItem();
		int slot = event.getSlot();
		String titre = event.getView().getTitle();
		
		if (titre.equals(ChatColor.GREEN + "Banque")) {
			
			
			if (item == null) return;
			
			event.setCancelled(true);
			
			
			switch (slot) {
			
			case 7: 
				utils.deposit(player, 1);
				break;
				
			case 16:
				utils.deposit(player, 8);
				break;
				
			case 25:
				utils.deposit(player, 64);
				break;
				
			case 15:
				utils.deposit(player, 2304);
				break;
				
			case 5:
				utils.withdraw(player, 1);
				break;
				
			case 14:
				utils.withdraw(player, 8);
				break;
				
			case 23:
				utils.withdraw(player, 64);
				break;

			}
			
			
			return;
		}
		
		
	}
	
	
	public static smpBank getPlugin() {
		return plugin;
	}
	
}
