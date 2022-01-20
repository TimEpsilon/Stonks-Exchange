package fr.tim.smpbank.listeners.environment;

import fr.tim.smpbank.items.CustomItems;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.persistence.PersistentDataType;

public class AntiEChest implements Listener {

    @EventHandler
    public void onItemDrag(InventoryClickEvent e) {
        if (e.getInventory().getType().equals(InventoryType.ENDER_CHEST)) {
            if (e.getCurrentItem().getAmount()==0) return;
            if (e.getCurrentItem().getItemMeta().getPersistentDataContainer().has(CustomItems.CustomItemKey, PersistentDataType.STRING)) {
                e.setCancelled(true);
            }
        }
        if (e.getClick().isKeyboardClick()) {
            e.setCancelled(true);
            e.setResult(Event.Result.DENY);
            return;
        }

    }
}
