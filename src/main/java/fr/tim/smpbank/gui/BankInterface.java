package fr.tim.smpbank.gui;

import fr.tim.smpbank.bank.Bank;
import fr.tim.smpbank.bank.Trader;
import fr.tim.smpbank.StonksExchange;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;
import java.util.UUID;

public class BankInterface implements Listener {
    private final Inventory inventory;
    private final Bank bank;
    float taux = StonksExchange.getPlugin().getTaux().getTaux();

    public BankInterface(Player p) {
        this(p,p.getUniqueId());
    }

    public BankInterface(Player viewer,UUID uuid) {
        this.bank = Bank.bankList.get(uuid);

        this.inventory = Bukkit.createInventory(null, 27, Component.text(ChatColor.GREEN + "Banque"));

        this.inventory.setItem(7,VisualItems.DEPOSIT_1.getItem());
        this.inventory.setItem(16,VisualItems.DEPOSIT_8.getItem());
        this.inventory.setItem(25,VisualItems.DEPOSIT_64.getItem());
        this.inventory.setItem(15,VisualItems.DEPOSIT_ALL.getItem());
        this.inventory.setItem(5,VisualItems.WITHDRAW_1.getItem());
        this.inventory.setItem(14,VisualItems.WITHDRAW_8.getItem());
        this.inventory.setItem(23,VisualItems.WITHDRAW_64.getItem());
        this.inventory.setItem(10,VisualItems.TAUX.getItem());
        this.inventory.setItem(12,VisualItems.SOLDE.getItem());

        viewer.openInventory(this.inventory);

        showBank(uuid);
    }

    public static void interaction(ItemStack item,Player player,Inventory inv) {

        UUID uuid = ((SkullMeta)inv.getItem(12).getItemMeta()).getOwningPlayer().getUniqueId();
        VisualItems itemEnum = VisualItems.searchItem(item.getType().toString(), item.getAmount());
        switch (itemEnum) {

            case DEPOSIT_1:
                Trader.deposit(1, player,uuid);
                break;

            case DEPOSIT_8:
                Trader.deposit(8, player,uuid);
                break;

            case DEPOSIT_64:
                Trader.deposit(64, player,uuid);
                break;

            case DEPOSIT_ALL:
                Trader.deposit(2304, player,uuid);
                break;

            case WITHDRAW_1:
                Trader.withdraw(1, player,uuid);
                break;

            case WITHDRAW_8:
                Trader.withdraw(8, player,uuid);
                break;

            case WITHDRAW_64:
                Trader.withdraw(64, player,uuid);
                break;

        }
        update(inv,Bank.bankList.get(uuid));
    }

    private void showBank(UUID uuid) {
        //Custom head
        ItemStack item = VisualItems.SOLDE.getItem();
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(uuid));
        item.setItemMeta(meta);
        this.inventory.setItem(12, item);

        //Taux
        item = VisualItems.TAUX.getItem();
        ItemMeta itemMeta = item.getItemMeta();
        List<Component> lore =  itemMeta.lore();
        lore.set(0,Component.text(ChatColor.GRAY + "" + this.taux));
        itemMeta.lore(lore);
        item.setItemMeta(itemMeta);
        this.inventory.setItem(10,item);

        update(this.inventory,this.bank);
    }

    public static void update(Inventory inv, Bank bank) {
        ItemStack item = inv.getItem(12);
        ItemMeta meta = item.getItemMeta();
        List<Component> lore =  meta.lore();
        lore.set(0,Component.text("§d" + bank.getSolde()));
        meta.lore(lore);
        item.setItemMeta(meta);
        inv.setItem(12,item);
    }

}
