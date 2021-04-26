package fr.tim.smpbank.listeners;

import fr.tim.smpbank.smpBank;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.HashMap;
import java.util.UUID;

public class OnDeath implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        HashMap<UUID,Boolean> dead = smpBank.getPlugin().getDead();
        if (!dead.containsKey(player.getUniqueId())) dead.put(player.getUniqueId(),true);

    }
}
