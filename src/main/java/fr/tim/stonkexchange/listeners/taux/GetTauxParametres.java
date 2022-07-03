package fr.tim.stonkexchange.listeners.taux;

import fr.tim.stonkexchange.bank.Bank;
import fr.tim.stonkexchange.bank.BankLog;
import fr.tim.stonkexchange.bank.group.Group;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.*;

public class GetTauxParametres implements Listener {

    public static HashMap<UUID,Integer> DeadList = new HashMap<>();
    public static HashMap<UUID,Boolean> JoinedList = new HashMap<>();
    public static HashMap<UUID,Integer> DiamondList = new HashMap<>();
    public static HashMap<UUID,Integer> BossCount = new HashMap<>();
    public static HashMap<UUID,Integer> AdvancementCount = new HashMap<>();
    public static HashMap<UUID,Integer> OreCount = new HashMap<>();

    public static final ArrayList<EntityType> BossList = new ArrayList<>(List.of(
            EntityType.ENDER_DRAGON,
            EntityType.WITHER,
            EntityType.WARDEN,
            EntityType.ELDER_GUARDIAN));
    public static final ArrayList<Material> oreList = new ArrayList<>(List.of(
            Material.ANCIENT_DEBRIS,
            Material.EMERALD_ORE,
            Material.GOLD_ORE,
            Material.DEEPSLATE_EMERALD_ORE,
            Material.DEEPSLATE_GOLD_ORE));

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

    @EventHandler
    public void onBossDeath(EntityDeathEvent e) {
        if (!BossList.contains(e.getEntity().getType())) return;
        if (e.getEntity().getKiller() == null) return;

        Player p = e.getEntity().getKiller();

        BossCount.compute(p.getUniqueId(),(k,v) -> (v == null) ? 1 : v+1);
    }

    @EventHandler
    public void onAchievementGet(PlayerAdvancementDoneEvent e) {
        Player p = e.getPlayer();
         if (e.getAdvancement().getDisplay() == null || !e.getAdvancement().getDisplay().doesAnnounceToChat()) return;
        AdvancementCount.compute(p.getUniqueId(),(k,v) -> (v == null) ? 1 : v+1);
    }

    @EventHandler void onOreMined(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if (oreList.contains(e.getBlock().getType()) && !p.getInventory().getItemInMainHand().getEnchantments().containsKey(Enchantment.SILK_TOUCH)) {
            OreCount.compute(p.getUniqueId(),(k, v) -> (v == null) ? 1 : v+1);
        }
    }

    public static float getTotalBefore(long time) {
        float total = 0;
        for (OfflinePlayer p : Bukkit.getOfflinePlayers()) {
            if (!Bank.bankList.containsKey(p.getUniqueId())) {
                new Bank(p.getUniqueId().toString(),p.getName());
            }

            List<BankLog> logList = Bank.bankList.get(p.getUniqueId()).getBankLogList();

            for (int i = logList.size()-1; i > -1; i--) {
                if (logList.get(i).getTime() <= time) {
                    total += logList.get(i).getSolde();
                    break;
                }
            }
        }

        for (Group group : Group.incList.values()) {
            List<BankLog> logList = group.getBankLogList();

            for (int i = logList.size()-1; i > -1; i--) {
                if (logList.get(i).getTime() <= time) {
                    total += logList.get(i).getSolde();
                    break;
                }
            }
        }

        return total;
    }

    public static float getTotalNow() {
        float total = 0;
        for (OfflinePlayer p : Bukkit.getOfflinePlayers()) {
            if (!Bank.bankList.containsKey(p.getUniqueId())) {
                new Bank(p.getUniqueId().toString(),p.getName());
            }

            total += Bank.bankList.get(p.getUniqueId()).getSolde();
        }

        return total;
    }

    public static void resetParameters() {
        JoinedList.clear();
        DiamondList.clear();
        DeadList.clear();
        OreCount.clear();
        BossCount.clear();
        AdvancementCount.clear();
    }

    public static float[] getFakeParameters() {
        float[] parameters = {0,0,0,0};
        //0 : joueurs
        //1 : morts
        //2 : diamonds
        //3 : variation

        parameters[0] = Math.round(7*Math.random());

        for (int i =0; i < parameters[0]; i++) {
            while (Math.random() < 0.2) {
                parameters[1] += 1;
            }
            parameters[2] += Math.round(Math.random()*10);
            parameters[3] += Math.round(Math.random()*700-200);
        }

        return parameters;
    }
}
