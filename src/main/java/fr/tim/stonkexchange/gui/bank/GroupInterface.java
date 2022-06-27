package fr.tim.stonkexchange.gui.bank;

import fr.tim.stonkexchange.StonkExchange;
import fr.tim.stonkexchange.bank.Bank;
import fr.tim.stonkexchange.bank.Trader;
import fr.tim.stonkexchange.bank.group.Group;
import fr.tim.stonkexchange.bank.rank.BankRank;
import fr.tim.stonkexchange.bank.rank.RankManager;
import fr.tim.stonkexchange.gui.pda.GestionPDA;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BundleMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GroupInterface {
    private final Inventory inventory;
    private final Group group;

    public GroupInterface(Player p) {
        group = Group.getByPlayer(p);
        inventory = null;
        if (group == null) {
            p.sendMessage(GestionPDA.PDAText + ChatColor.RED + "[ERROR] Vous n'apartenez pas à un groupe");
            return;
        }
        new GroupInterface(p,Group.getByPlayer(p).getName());
    }

    public GroupInterface(Player viewer, String name) {
        group = Group.incList.get(name);

        this.inventory = Bukkit.createInventory(null, 27, Component.text(ChatColor.BLUE + "Groupe"));

        inventory.setItem(VisualItems.DEPOSIT_1.getSlot(), VisualItems.DEPOSIT_1.getItem());
        inventory.setItem(VisualItems.DEPOSIT_8.getSlot(),VisualItems.DEPOSIT_8.getItem());
        inventory.setItem(VisualItems.DEPOSIT_64.getSlot(),VisualItems.DEPOSIT_64.getItem());
        inventory.setItem(VisualItems.DEPOSIT_ALL.getSlot(),VisualItems.DEPOSIT_ALL.getItem());
        inventory.setItem(VisualItems.WITHDRAW_1.getSlot(),VisualItems.WITHDRAW_1.getItem());
        inventory.setItem(VisualItems.WITHDRAW_8.getSlot(),VisualItems.WITHDRAW_8.getItem());
        inventory.setItem(VisualItems.WITHDRAW_64.getSlot(),VisualItems.WITHDRAW_64.getItem());
        inventory.setItem(VisualItems.MEMBERS.getSlot(),VisualItems.MEMBERS.getItem());
        inventory.setItem(VisualItems.OWNER.getSlot(),VisualItems.OWNER.getItem());
        inventory.setItem(VisualItems.BANK.getSlot(),VisualItems.BANK.getItem());
        inventory.setItem(VisualItems.EMBLEM.getSlot(),VisualItems.EMBLEM.getItem());

        load();
        update(inventory,group);
        viewer.openInventory(inventory);

    }

    public static void interaction(ItemStack item, Player player, Group group, Inventory inv) {

        VisualItems itemEnum = VisualItems.searchItem(item);
        if (itemEnum == null) return;
        switch (itemEnum) {
            case DEPOSIT_1 -> Trader.deposit(1, player, group);
            case DEPOSIT_8 -> Trader.deposit(8, player, group);
            case DEPOSIT_64 -> Trader.deposit(64, player, group);
            case DEPOSIT_ALL -> Trader.deposit(2304, player, group);
            case WITHDRAW_1 -> Trader.withdraw(1, player, group);
            case WITHDRAW_8 -> Trader.withdraw(8, player, group);
            case WITHDRAW_64 -> Trader.withdraw(64, player, group);
            case EMBLEM -> {
                return; //TODO;
            }
        }
        update(inv,group);
    }

    public static void update(Inventory inv, Group group) {

        // Solde
        ItemStack item = inv.getItem(VisualItems.BANK.getSlot());
        if (item == null) return;
        ItemMeta meta = item.getItemMeta();
        List<Component> lore =  meta.lore();
        lore.set(0,Component.text("§d" + group.getSolde() + ChatColor.GRAY + " / " + group.getMax()));
        meta.lore(lore);
        item.setItemMeta(meta);
        inv.setItem(VisualItems.BANK.getSlot(),item);
    }

    private void load() {

        //Owner
        ItemStack item = inventory.getItem(VisualItems.OWNER.getSlot());
        if (item == null) return;
        SkullMeta skull = (SkullMeta) item.getItemMeta();
        List<Component> lore =  skull.lore();
        lore.set(0,Component.text(ChatColor.GREEN + group.getMembers().get(group.getOwner())));
        skull.lore(lore);
        skull.setOwningPlayer(Bukkit.getOfflinePlayer(group.getOwner()));
        item.setItemMeta(skull);
        inventory.setItem(VisualItems.OWNER.getSlot(),item);


        //Name
        ItemStack emblem = inventory.getItem(VisualItems.EMBLEM.getSlot());
        if (emblem == null) return;
        ItemMeta meta = emblem.getItemMeta();
        meta.displayName(Component.text(ChatColor.GOLD + group.getName()));
        emblem.setItemMeta(meta);
        inventory.setItem(VisualItems.EMBLEM.getSlot(), emblem);


        //Members
        ItemStack item1 = inventory.getItem(VisualItems.MEMBERS.getSlot());
        if (item1 == null) return;
        BundleMeta bundle = (BundleMeta) item1.getItemMeta();
        List<Component> lore1 =  bundle.lore();

        int i = 0;
        for (Map.Entry<UUID, String> entry : group.getMembers().entrySet()) {
            UUID uuid = entry.getKey();
            String name = entry.getValue();
            lore1.set(i,Component.text(ChatColor.YELLOW + name));
            ItemStack member = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta skullMeta = (SkullMeta) member.getItemMeta();
            skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(uuid));
            member.setItemMeta(skullMeta);
            bundle.addItem(member);

            i++;
        }
        bundle.lore(lore1);
        item1.setItemMeta(bundle);
        inventory.setItem(VisualItems.MEMBERS.getSlot(),item1);
    }

}
