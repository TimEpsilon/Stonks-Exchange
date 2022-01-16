package fr.tim.smpbank.bank;

import fr.tim.smpbank.StonksExchange;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.UUID;

public class Trader {

    public static void deposit(int n, Player p, UUID uuid) {
        Bank b = Bank.bankList.get(uuid);
        float taux = StonksExchange.getPlugin().getTaux().getTaux();

        if (!p.getInventory().contains(Material.DIAMOND) && !p.getInventory().contains(Material.EMERALD)) return;

        int i = 0;

        for (ItemStack item : p.getInventory().getContents()) {
            if (n < 0) break;
            if (item == null) continue;
            if (item.getItemMeta().getPersistentDataContainer().has(CustomItems.CustomItemKey, PersistentDataType.STRING)) {
                if(item.getItemMeta().getPersistentDataContainer().get(CustomItems.CustomItemKey,PersistentDataType.STRING).contains(ChatColor.AQUA + "" + ChatColor.BOLD + "M-Coin")) {
                    int amount = item.getAmount();
                    item.setAmount(Math.max(amount - n,0));
                    int cleared = Math.min(n, amount);
                    n -= cleared;
                    b.add(Math.round(1000f*cleared)/1000f);
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

            b.add(Math.round(1000f*taux*Math.min(n-i,j))/1000f);
            p.getInventory().removeItem(removeQuantity);
        }

    }

    public static void withdraw(int n, Player p,UUID uuid) {
        Bank b = Bank.bankList.get(uuid);

        int retirer = (int) Math.min(Math.floor(b.getSolde()),n);

        ItemStack item = CustomItems.MCOIN.getItem();
        item.setAmount(retirer);
        HashMap<Integer, ItemStack> surplus = p.getInventory().addItem(item);
        b.add(Math.round(1000f*-1*retirer)/1000f);

        for (ItemStack i : surplus.values()) {
            if (i == null || i.getAmount() == 0) continue;
            p.getWorld().dropItem(p.getLocation(),i);
        }

    }
}
