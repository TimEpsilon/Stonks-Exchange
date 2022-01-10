package fr.tim.smpbank.bank;

import fr.tim.smpbank.StonksExchange;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class Trader {

    public static void deposit(int n, Player p) {
        Bank b = Bank.bankList.get(p.getUniqueId());
        float taux = StonksExchange.getPlugin().getTaux().getTaux();

        if (!p.getInventory().contains(Material.DIAMOND) && !p.getInventory().contains(Material.EMERALD)) return;

        int i = 0;

        for (ItemStack item : p.getInventory().getContents()) {
            if (item == null) continue;
            if (item.isSimilar(CustomItems.MCOIN.getItem())) {
                i += item.getAmount();
            }
        }
        ItemStack removeQuantity = CustomItems.MCOIN.getItem();
        removeQuantity.setAmount(Math.min(i,n));

        b.add(Math.min(i,n));

        p.getInventory().removeItem(removeQuantity);

        if (i<n) {
            int j = 0;

            for (ItemStack item : p.getInventory().getContents()) {
                if (item == null) continue;
                if (item.isSimilar(new ItemStack(Material.DIAMOND))) {
                    j += item.getAmount();
                }
            }

            removeQuantity = new ItemStack(Material.DIAMOND,Math.min(n-i,j));

            b.add(taux*Math.min(n-i,j));
            p.getInventory().removeItem(removeQuantity);
        }

    }

    public static void withdraw(int n, Player p) {
        Bank b = Bank.bankList.get(p.getUniqueId());

        int retirer = (int) Math.min(Math.floor(b.getSolde()),n);

        ItemStack item = CustomItems.MCOIN.getItem();
        item.setAmount(retirer);
        HashMap<Integer, ItemStack> surplus = p.getInventory().addItem(item);
        b.add(-1*retirer);

        for (ItemStack i : surplus.values()) {
            if (i == null || i.getAmount() == 0) continue;
            p.getWorld().dropItem(p.getLocation(),i);
        }

    }
}
