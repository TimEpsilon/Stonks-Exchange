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
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BankInterface {
    private final Inventory inventory;
    private final Bank bank;
    float taux = StonkExchange.getPlugin().getTaux().getTaux();

    public BankInterface(Player p) {
        this(p,p.getUniqueId());
    }

    public BankInterface(Player viewer,UUID uuid) {
        this.bank = Bank.bankList.get(uuid);

        inventory = Bukkit.createInventory(null, 27, Component.text(ChatColor.GREEN + "Banque"));

        inventory.setItem(VisualItems.DEPOSIT_1.getSlot(), VisualItems.DEPOSIT_1.getItem());
        inventory.setItem(VisualItems.DEPOSIT_8.getSlot(),VisualItems.DEPOSIT_8.getItem());
        inventory.setItem(VisualItems.DEPOSIT_64.getSlot(),VisualItems.DEPOSIT_64.getItem());
        inventory.setItem(VisualItems.DEPOSIT_ALL.getSlot(),VisualItems.DEPOSIT_ALL.getItem());
        inventory.setItem(VisualItems.WITHDRAW_1.getSlot(),VisualItems.WITHDRAW_1.getItem());
        inventory.setItem(VisualItems.WITHDRAW_8.getSlot(),VisualItems.WITHDRAW_8.getItem());
        inventory.setItem(VisualItems.WITHDRAW_64.getSlot(),VisualItems.WITHDRAW_64.getItem());
        inventory.setItem(VisualItems.TAUX.getSlot(),VisualItems.TAUX.getItem());
        inventory.setItem(VisualItems.SOLDE.getSlot(),VisualItems.SOLDE.getItem());
        inventory.setItem(VisualItems.RANK_GREEN.getSlot(),VisualItems.RANK_GREEN.getItem());
        inventory.setItem(VisualItems.GROUP.getSlot() ,VisualItems.GROUP.getItem());
        inventory.setItem(VisualItems.DIAMOND_WITHDRAW_1.getSlot(), VisualItems.DIAMOND_WITHDRAW_1.getItem());
        inventory.setItem(VisualItems.DIAMOND_WITHDRAW_8.getSlot(), VisualItems.DIAMOND_WITHDRAW_8.getItem());
        inventory.setItem(VisualItems.DIAMOND_WITHDRAW_64.getSlot(), VisualItems.DIAMOND_WITHDRAW_64.getItem());


        viewer.openInventory(this.inventory);

        showBank(uuid);
    }

    public static void interaction(ItemStack item,Player player,Inventory inv) {

        UUID uuid = player.getUniqueId();
        VisualItems itemEnum = VisualItems.searchItem(item);
        if (itemEnum == null) return;
        switch (itemEnum) {
            case DEPOSIT_1 -> Trader.deposit(1, player, uuid);
            case DEPOSIT_8 -> Trader.deposit(8, player, uuid);
            case DEPOSIT_64 -> Trader.deposit(64, player, uuid);
            case DEPOSIT_ALL -> Trader.deposit(2304, player, uuid);
            case WITHDRAW_1 -> Trader.withdraw(1, player, uuid);
            case WITHDRAW_8 -> Trader.withdraw(8, player, uuid);
            case WITHDRAW_64 -> Trader.withdraw(64, player, uuid);
            case DIAMOND_WITHDRAW_1 -> Trader.withdrawDiamonds(1,player,Bank.bankList.get(player.getUniqueId()));
            case DIAMOND_WITHDRAW_8 -> Trader.withdrawDiamonds(8,player,Bank.bankList.get(player.getUniqueId()));
            case DIAMOND_WITHDRAW_64 -> Trader.withdrawDiamonds(64,player,Bank.bankList.get(player.getUniqueId()));
            case RANK_GREEN, RANK_BLUE, RANK_GOLD, RANK_YELLOW, PURPLE, RANK_RED -> RankManager.upggradeAccount(player);
            case GROUP -> new GroupInterface(player);
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
        if (item == null) return;
        ItemMeta meta = item.getItemMeta();
        List<Component> lore =  new ArrayList<>();
        lore.add(Component.text("§d" + bank.getSolde() + ChatColor.GRAY + " / " + br.getMaxStorage()));
        meta.lore(lore);
        item.setItemMeta(meta);
        inv.setItem(VisualItems.SOLDE.getSlot(),item);

        inv.setItem(VisualItems.RANK_GREEN.getSlot(), VisualItems.getItemByRank(br).getItem());
    }

}
