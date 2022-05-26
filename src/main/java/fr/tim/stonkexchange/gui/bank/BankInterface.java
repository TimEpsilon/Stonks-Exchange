package fr.tim.stonkexchange.gui.bank;

import fr.tim.stonkexchange.StonkExchange;
import fr.tim.stonkexchange.bank.Bank;
import fr.tim.stonkexchange.bank.Trader;
import fr.tim.stonkexchange.bank.rank.BankRank;
import fr.tim.stonkexchange.bank.rank.RankManager;
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
    float taux = StonkExchange.getPlugin().getTaux().getTaux();

    public BankInterface(Player p) {
        this(p,p.getUniqueId());
    }

    public BankInterface(Player viewer,UUID uuid) {
        this.bank = Bank.bankList.get(uuid);

        this.inventory = Bukkit.createInventory(null, 27, Component.text(ChatColor.GREEN + "Banque"));

        this.inventory.setItem(VisualItems.DEPOSIT_1.getSlot(), VisualItems.DEPOSIT_1.getItem());
        this.inventory.setItem(VisualItems.DEPOSIT_8.getSlot(),VisualItems.DEPOSIT_8.getItem());
        this.inventory.setItem(VisualItems.DEPOSIT_64.getSlot(),VisualItems.DEPOSIT_64.getItem());
        this.inventory.setItem(VisualItems.DEPOSIT_ALL.getSlot(),VisualItems.DEPOSIT_ALL.getItem());
        this.inventory.setItem(VisualItems.WITHDRAW_1.getSlot(),VisualItems.WITHDRAW_1.getItem());
        this.inventory.setItem(VisualItems.WITHDRAW_8.getSlot(),VisualItems.WITHDRAW_8.getItem());
        this.inventory.setItem(VisualItems.WITHDRAW_64.getSlot(),VisualItems.WITHDRAW_64.getItem());
        this.inventory.setItem(VisualItems.TAUX.getSlot(),VisualItems.TAUX.getItem());
        this.inventory.setItem(VisualItems.SOLDE.getSlot(),VisualItems.SOLDE.getItem());
        this.inventory.setItem(VisualItems.RANK_GREEN.getSlot(),VisualItems.RANK_GREEN.getItem());

        viewer.openInventory(this.inventory);

        showBank(uuid);
    }

    public static void interaction(ItemStack item,Player player,Inventory inv) {

        UUID uuid = ((SkullMeta)inv.getItem(VisualItems.SOLDE.getSlot()).getItemMeta()).getOwningPlayer().getUniqueId();
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

            case RANK_GREEN:
            case RANK_BLUE:
            case RANK_GOLD:
            case RANK_YELLOW:
            case PURPLE:
            case RANK_RED:
                RankManager.upggradeAccount(player);
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
        this.inventory.setItem(VisualItems.SOLDE.getSlot(), item);

        //Taux
        item = VisualItems.TAUX.getItem();
        ItemMeta itemMeta = item.getItemMeta();
        List<Component> lore =  itemMeta.lore();
        lore.set(0,Component.text(ChatColor.GRAY + "" + this.taux));
        itemMeta.lore(lore);
        item.setItemMeta(itemMeta);
        this.inventory.setItem(VisualItems.TAUX.getSlot(),item);

        update(this.inventory,this.bank);
    }

    public static void update(Inventory inv, Bank bank) {
        BankRank br = bank.getRank();

        ItemStack item = inv.getItem(VisualItems.SOLDE.getSlot());
        ItemMeta meta = item.getItemMeta();
        List<Component> lore =  meta.lore();
        lore.set(0,Component.text("§d" + bank.getSolde() + ChatColor.GRAY + " / " + br.getMaxStorage()));
        meta.lore(lore);
        item.setItemMeta(meta);
        inv.setItem(VisualItems.SOLDE.getSlot(),item);

        inv.setItem(VisualItems.RANK_GREEN.getSlot(), VisualItems.getItemByRank(br).getItem());
    }

}
