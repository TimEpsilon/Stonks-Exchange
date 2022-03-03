package fr.tim.smpbank.bank;

import fr.tim.smpbank.StonksExchange;
import fr.tim.smpbank.items.CustomItems;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.UUID;

public class Trader {

    //TODO tester les cas limites

    public static void deposit(int n, Player p, UUID uuid) {
        Bank b = Bank.bankList.get(uuid);
        float taux = StonksExchange.getPlugin().getTaux().getTaux();

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
    }

    public static void withdraw(int n, Player p,UUID uuid) {
        Bank b = Bank.bankList.get(uuid);

        int retirer = (int) Math.min(Math.floor(b.getSolde()),n);

        ItemStack item = CustomItems.MCOIN.getItem();
        item.setAmount(retirer);
        HashMap<Integer, ItemStack> surplus = p.getInventory().addItem(item);
        b.add(-retirer);

        for (ItemStack i : surplus.values()) {
            if (i == null || i.getAmount() == 0) continue;
            p.getWorld().dropItem(p.getLocation(),i);
        }

    }
}
