package fr.tim.smpbank.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;
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
        if (e.getBlock().getType().equals(Material.DIAMOND_ORE) || e.getBlock().getType().equals(Material.DEEPSLATE_DIAMOND_ORE)) {
            DiamondList.compute(p.getUniqueId(),(k, v) -> (v == null) ? 1 : v+1);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (!JoinedList.containsKey(p.getUniqueId())) JoinedList.put(p.getUniqueId(),true);
    }

    public void resetParameters() {
        JoinedList.clear();
        DiamondList.clear();
        DeadList.clear();
    }
}
