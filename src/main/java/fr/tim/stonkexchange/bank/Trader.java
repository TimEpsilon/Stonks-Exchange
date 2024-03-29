package fr.tim.stonkexchange.bank;

import fr.tim.stonkexchange.StonkExchange;
import fr.tim.stonkexchange.bank.group.Group;
import fr.tim.stonkexchange.items.CustomItems;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.UUID;

public class Trader {

    public static void deposit(int n, Player p, UUID uuid) {
        Bank b = Bank.bankList.get(uuid);
        deposit(n,p,b);
    }

    public static void deposit(int n, Player p, Bank b) {
        float init = b.getSolde();
        float taux = StonkExchange.getPlugin().getTaux().getTaux();

        if (!p.getInventory().contains(Material.DIAMOND) && !p.getInventory().contains(Material.EMERALD)) return;

        int i = 0;
        int max = b.getRank().getMaxStorage();

        for (ItemStack item : p.getInventory().getContents()) {
            if (n < 0) break;
            if (item == null) continue;

            if (item.getItemMeta().getPersistentDataContainer().has(CustomItems.CustomItemKey, PersistentDataType.STRING)) {
                if(item.getItemMeta().getPersistentDataContainer().get(CustomItems.CustomItemKey,PersistentDataType.STRING).contains(CustomItems.MCOIN.getName())) {
                    int amount = item.getAmount();
                    item.setAmount(Math.max(amount - n,0));
                    int cleared = Math.min(n, amount);
                    n -= cleared;
                    b.add(cleared);

                }
            }
        }

        if (i<n) {
            int j = 0;

            for (ItemStack item : p.getInventory().getContents()) {
                if (item == null) continue;
                if (item.isSimilar(new ItemStack(Material.DIAMOND))) {
                    j += item.getAmount();
                }
            }

            ItemStack removeQuantity = new ItemStack(Material.DIAMOND,Math.min(n-i,j));

            b.add(taux*Math.min(n-i,j));
            p.getInventory().removeItem(removeQuantity);
        }

        if (b.getSolde() > max) {
            int diff = (int) b.getSolde() - max + 1;
            ItemStack mcoin = CustomItems.MCOIN.getItem();
            mcoin.setAmount(diff);
            HashMap<Integer,ItemStack> rest = p.getInventory().addItem(mcoin);
            for (ItemStack restItem : rest.values()) {
                p.getWorld().dropItem(p.getLocation(),restItem);
            }
            b.add(-diff);
        }
        StonkExchange.logs.log("(DEPOSIT) " + b.getName() + " -> " + Math.round((b.getSolde() - init)*1000f)/1000f);

    }

    public static void deposit(int n, Player p, Group group) {
        float init = group.getSolde();
        float taux = StonkExchange.getPlugin().getTaux().getTaux();

        if (!p.getInventory().contains(Material.DIAMOND) && !p.getInventory().contains(Material.EMERALD)) return;

        int i = 0;
        int max = group.getMax();

        for (ItemStack item : p.getInventory().getContents()) {
            if (n < 0) break;
            if (item == null) continue;

            if (item.getItemMeta().getPersistentDataContainer().has(CustomItems.CustomItemKey, PersistentDataType.STRING)) {
                if(item.getItemMeta().getPersistentDataContainer().get(CustomItems.CustomItemKey,PersistentDataType.STRING).contains(CustomItems.MCOIN.getName())) {
                    int amount = item.getAmount();
                    item.setAmount(Math.max(amount - n,0));
                    int cleared = Math.min(n, amount);
                    n -= cleared;
                    group.soldeAdd(cleared);

                }
            }
        }

        if (i<n) {
            int j = 0;

            for (ItemStack item : p.getInventory().getContents()) {
                if (item == null) continue;
                if (item.isSimilar(new ItemStack(Material.DIAMOND))) {
                    j += item.getAmount();
                }
            }

            ItemStack removeQuantity = new ItemStack(Material.DIAMOND,Math.min(n-i,j));

            group.soldeAdd(taux*Math.min(n-i,j));
            p.getInventory().removeItem(removeQuantity);
        }

        if (group.getSolde() > max) {
            int diff = (int) group.getSolde() - max + 1;
            ItemStack mcoin = CustomItems.MCOIN.getItem();
            mcoin.setAmount(diff);
            HashMap<Integer,ItemStack> rest = p.getInventory().addItem(mcoin);
            for (ItemStack restItem : rest.values()) {
                p.getWorld().dropItem(p.getLocation(),restItem);
            }
            group.soldeAdd(-diff);
        }

        StonkExchange.logs.log("(DEPOSIT) " + group.getName() + " - " + p.getName() + " -> " + Math.round((group.getSolde() - init)*1000f)/1000f);
    }

    public static void withdraw(int n, Player p, UUID uuid) {
        Bank b = Bank.bankList.get(uuid);
        withdraw(n,p,b);
    }

    public static void withdraw(int n, Player p,Bank b) {
        float init = b.getSolde();

        int retirer = (int) Math.min(Math.floor(b.getSolde()),n);

        ItemStack item = CustomItems.MCOIN.getItem();
        item.setAmount(retirer);
        HashMap<Integer, ItemStack> surplus = p.getInventory().addItem(item);
        b.add(-retirer);

        for (ItemStack i : surplus.values()) {
            if (i == null || i.getAmount() == 0) continue;
            p.getWorld().dropItem(p.getLocation(),i);
        }

        StonkExchange.logs.log("(WITHDRAW) " + b.getName() + " -> " + Math.round((init - b.getSolde())*1000f)/1000f);
    }

    public static void withdraw(int n, Player p,Group group) {
        float init = group.getSolde();

        int retirer = (int) Math.min(Math.floor(group.getSolde()),n);

        ItemStack item = CustomItems.MCOIN.getItem();
        item.setAmount(retirer);
        HashMap<Integer, ItemStack> surplus = p.getInventory().addItem(item);
        group.soldeAdd(-retirer);

        for (ItemStack i : surplus.values()) {
            if (i == null || i.getAmount() == 0) continue;
            p.getWorld().dropItem(p.getLocation(),i);
        }

        StonkExchange.logs.log("(WITHDRAW) " + group.getName() + " - " + p.getName() + " -> " + Math.round((init - group.getSolde())*1000f)/1000f);
    }

    public static void withdrawDiamonds(int n, Player p, Bank bank) {
        float init = bank.getSolde();
        int maxDiamonds = (int) Math.floor(init / StonkExchange.getPlugin().getTaux().getTaux());

        int m = Math.min(maxDiamonds,n);
        ItemStack diamonds = new ItemStack(Material.DIAMOND,m);
        HashMap<Integer, ItemStack> surplus = p.getInventory().addItem(diamonds);
        bank.add(-m*StonkExchange.getPlugin().getTaux().getTaux());

        for (ItemStack i : surplus.values()) {
            if (i == null || i.getAmount() == 0) continue;
            p.getWorld().dropItem(p.getLocation(),i);
        }

        StonkExchange.logs.log("(WITHDRAW) " + p.getUniqueId() + " - " + p.getName() + " -> " + Math.round((init - bank.getSolde())*1000f)/1000f);
    }
}
