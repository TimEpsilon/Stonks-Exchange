package fr.tim.stonkexchange.items.recallpotion;

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
        if (p.isSneaking()) loc = p.getWorld().getSpawnLocation();
        else loc = p.getBedSpawnLocation();
        
        p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION,40,0));
        p.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS,40,0));
        p.spawnParticle(Particle.SCULK_CHARGE,loc,100,0.2,0.7,0.2,1);

        Bukkit.getScheduler().runTaskLater(StonkExchange.getPlugin(), () -> {
            p.spawnParticle(Particle.SCULK_CHARGE,p.getLocation(),100,0.2,0.7,0.2,1);
            p.teleport(loc);
            p.playSound(loc,Sound.ITEM_CHORUS_FRUIT_TELEPORT,1,1);
        },40);
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
