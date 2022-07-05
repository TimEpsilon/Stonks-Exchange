package fr.tim.stonkexchange.listeners.bank;

import fr.tim.stonkexchange.gui.bank.BankInterface;
import fr.tim.stonkexchange.gui.bank.GroupInterface;
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

       if (!e.getView().title().equals(Component.text(ChatColor.GREEN + "Banque")) && !e.getView().title().equals(Component.text(ChatColor.BLUE + "Groupe"))) return;

        e.setCancelled(true);

        if (e.getView().title().equals(Component.text(ChatColor.GREEN + "Banque"))) {
            BankInterface.interaction(item,player,e.getClickedInventory());
            return;
        }

        if (e.getView().title().equals(Component.text(ChatColor.BLUE + "Groupe"))) {
            GroupInterface.groupInterfaceList.get(player.getUniqueId()).interaction(item, player);
        }
    }
}
