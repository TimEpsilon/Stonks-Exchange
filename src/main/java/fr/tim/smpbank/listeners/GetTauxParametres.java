package fr.tim.smpbank.listeners;

import fr.tim.smpbank.bank.Bank;
import fr.tim.smpbank.bank.BankLog;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class GetTauxParametres implements Listener {

    public static HashMap<UUID,Integer> DeadList = new HashMap<>();
    public static HashMap<UUID,Boolean> JoinedList = new HashMap<>();
    public static HashMap<UUID,Integer> DiamondList = new HashMap<>();

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        DeadList.compute(player.getUniqueId(),(k, v) -> (v == null) ? 1 : v+1);
    }

    @EventHandler
    public void onDiamondMined(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if ((e.getBlock().getType().equals(Material.DIAMOND_ORE) || e.getBlock().getType().equals(Material.DEEPSLATE_DIAMOND_ORE)) && !p.getInventory().getItemInMainHand().getEnchantments().containsKey(Enchantment.SILK_TOUCH)) {
            DiamondList.compute(p.getUniqueId(),(k, v) -> (v == null) ? 1 : v+1);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (!JoinedList.containsKey(p.getUniqueId())) JoinedList.put(p.getUniqueId(),true);
    }

    public static float getTotalBefore(long time) {
        float total = 0;
        for (OfflinePlayer p : Bukkit.getOfflinePlayers()) {
            if (!Bank.bankList.containsKey(p.getUniqueId())) {
                new Bank(p.getUniqueId().toString());
            }

            List<BankLog> logList = Bank.bankList.get(p.getUniqueId()).getBankLogList();

            for (int i = logList.size()-1; i > -1; i--) {
                if (logList.get(i).getTime() <= time) {
                    total += logList.get(i).getSolde();
                    break;
                }
            }
        }

        return total;
    }

    public static void resetParameters() {
        JoinedList.clear();
        DiamondList.clear();
        DeadList.clear();
    }
}
