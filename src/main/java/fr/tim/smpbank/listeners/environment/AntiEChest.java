package fr.tim.smpbank.listeners.environment;

import fr.tim.smpbank.items.CustomItems;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;

public class AntiEChest implements Listener {

    @EventHandler
    public void onItemDrag(InventoryCloseEvent e) {
        if (e.getInventory().getType().equals(InventoryType.ENDER_CHEST)) {
            if (e.getInventory().contains(Material.EMERALD) ||e.getInventory().contains(Material.FILLED_MAP) || e.getInventory().contains(Material.PAPER)) {
                for (ItemStack item : e.getInventory().getContents()) {
                    if (item == null) continue;
                    if (item.getItemMeta().getPersistentDataContainer().has(CustomItems.CustomItemKey,PersistentDataType.STRING)) {
                        HashMap<Integer,ItemStack> map = e.getPlayer().getInventory().addItem(item);
                        if (!map.isEmpty()) {
                            for (ItemStack remain : map.values()) {
                                e.getPlayer().getWorld().dropItem(e.getPlayer().getLocation(),remain);
                            }
                        }
                        e.getInventory().remove(item);
                    }
                }
            }
        }
    }
}
