package fr.tim.smpbank.bank.vault;

import fr.tim.smpbank.gui.bank.BankInterface;
import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class PocketVault implements Listener {

    @EventHandler
    public void onVaultUse(PlayerInteractEvent e) {
        if (e.getItem() == null) return;
        if (!e.getItem().getType().equals(Material.IRON_INGOT)) return;
        if (e.getAction().isLeftClick()) return;

        ItemStack item = e.getItem();

        if (!item.getItemMeta().getPersistentDataContainer().has(new NamespacedKey("eventmanager","customitem"), PersistentDataType.STRING)) return;
        if (!item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey("eventmanager","customitem"), PersistentDataType.STRING).contains("Pocket Vault")) return;

        e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.BLOCK_CHEST_LOCKED, SoundCategory.BLOCKS,1,0.5f);
        item.setAmount(item.getAmount()-1);
        new BankInterface(e.getPlayer());
    }
}
