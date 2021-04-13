package fr.tim.smpbank.listeners;

import fr.tim.smpbank.bank.Bank;
import fr.tim.smpbank.bank.Trader;
import fr.tim.smpbank.gui.Interface;
import fr.tim.smpbank.smpBank;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class OnClick implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        ItemStack item = e.getCurrentItem();
        int slot = e.getSlot();
        String titre = e.getView().getTitle();

        if (titre.equals(ChatColor.GREEN + "Banque")) {


            if (item == null) return;

            e.setCancelled(true);

            interaction(player,slot);
        }
    }

    private void interaction(Player p,int slot) {
        Bank b = smpBank.getPlugin().getListeJoueurs().get(p.getUniqueId());

        switch (slot) {

            case 7:
                Trader.deposit(1,p);
                break;

            case 16:
                Trader.deposit(8,p);
                break;

            case 25:
                Trader.deposit(64,p);
                break;

            case 15:
                Trader.deposit(2304,p);
                break;

            case 5:
                Trader.withdraw(1, p);
                break;

            case 14:
                Trader.withdraw(8, p);
                break;

            case 23:
                Trader.withdraw(64, p);
                break;

        }
        b.getGUI().reload();
    }
}


