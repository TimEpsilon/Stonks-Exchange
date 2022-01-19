package fr.tim.smpbank.listeners.bank;

import fr.tim.smpbank.gui.bank.BankInterface;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class InterfaceInteraction implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        ItemStack item = e.getCurrentItem();

        if (item == null) return;
        if (!e.getView().title().equals(Component.text(ChatColor.GREEN + "Banque"))) return;

        e.setCancelled(true);

        BankInterface.interaction(item,player,e.getClickedInventory());
    }
}
