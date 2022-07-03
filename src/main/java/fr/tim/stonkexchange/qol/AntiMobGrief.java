package fr.tim.stonkexchange.qol;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class AntiMobGrief implements Listener {

    @EventHandler
    public void onCreeperExplode(EntityExplodeEvent e) {
        if (!e.getEntity().getType().equals(EntityType.CREEPER)) return;
        e.blockList().clear();
    }

    @EventHandler
    public void onEndermanPickup(EntityChangeBlockEvent e) {
        if (e.getEntity().getType().equals(EntityType.ENDERMAN)) return;
        e.setCancelled(true);
    }
}
