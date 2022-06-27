package fr.tim.stonkexchange.items;

import fr.tim.stonkexchange.StonkExchange;
import fr.tim.stonkexchange.items.CustomItems;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class RecallPotion implements Listener {

    private void returnToSpawn(Player p) {
        Location loc;
        if (p.isSneaking() || p.getBedSpawnLocation() == null) loc = p.getWorld().getSpawnLocation();
        else loc = p.getBedSpawnLocation();

        Location current = p.getLocation();

        p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION,50,2));
        p.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS,70,2));
        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,50,6));
        p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS,50,10));
        p.spawnParticle(Particle.SONIC_BOOM,current.clone().add(0,0.5,0),50,0.2,0.6,0.2);

        Bukkit.getScheduler().runTaskLater(StonkExchange.getPlugin(), () -> {
            p.spawnParticle(Particle.SONIC_BOOM,loc.clone().add(0,0.5,0),30,0.2,0.6,0.2);
            p.teleport(loc);
            p.playSound(loc,Sound.ITEM_CHORUS_FRUIT_TELEPORT,1,1);
        },20);
    }

    @EventHandler
    public void onDrink(PlayerItemConsumeEvent e) {
        if (!(e.getItem().getType() == Material.POTION)) return;
        ItemStack item = e.getItem();
        PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();

        if (!pdc.has(CustomItems.CustomItemKey)) return;
        if (!pdc.get(CustomItems.CustomItemKey, PersistentDataType.STRING).contains(CustomItems.RECALL_POTION.getName())) return;

        Player p = e.getPlayer();
        p.getInventory().removeItem(item);
        returnToSpawn(p);
    }




}
