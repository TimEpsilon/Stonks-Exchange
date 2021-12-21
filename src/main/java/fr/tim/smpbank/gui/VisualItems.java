package fr.tim.smpbank.gui;

import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public enum VisualItems {

    DEPOSIT_1(Material.DIAMOND, 1, ChatColor.GREEN + "+1", ChatColor.GRAY + "Déposer 1 diamant",0),
    DEPOSIT_8(Material.DIAMOND, 8, ChatColor.GREEN + "+8", ChatColor.GRAY + "Déposer 8 diamants",0),
    DEPOSIT_64(Material.DIAMOND, 64, ChatColor.GREEN + "+64", ChatColor.GRAY + "Déposer 64 diamants",0),
    WITHDRAW_1(Material.EMERALD, 1, ChatColor.RED + "-1", ChatColor.GRAY + "Retirer  1 M-Coin",42),
    WITHDRAW_8(Material.EMERALD, 8, ChatColor.RED + "-8", ChatColor.GRAY + "Retirer 8 M-Coin",42),
    WITHDRAW_64(Material.EMERALD, 64, ChatColor.RED + "-64", ChatColor.GRAY + "Retirer 64 M-Coin",42),
    DEPOSIT_ALL(Material.DIAMOND_BLOCK,1,ChatColor.GREEN + "All", ChatColor.GRAY + "Tout déposer",0),
    TAUX(Material.NETHER_STAR, 1, ChatColor.YELLOW + "Taux du M-coin actuel :",ChatColor.GRAY + " null", 0),
    SOLDE( Material.PLAYER_HEAD, 1, ChatColor.GOLD + "Solde : ", "§d" + "0.0",0);

    private ItemStack item;

    VisualItems(Material material, int count, String name, String lore, int cmd) {
        List<Component> Lore = new ArrayList<>();
        Lore.add(Component.text(lore));

        this.item = new ItemStack(material,count);
        ItemMeta meta = this.item.getItemMeta();
        meta.setCustomModelData(cmd);
        meta.displayName(Component.text(name));
        meta.lore(Lore);
        this.item.setItemMeta(meta);

    }

    public ItemStack getItem() {
        return this.item;
    }

    public static VisualItems searchItem(String name,int n) {
        switch (name) {
            case "DIAMOND":
                switch (n) {
                    case 1:
                        return DEPOSIT_1;
                    case 8:
                        return DEPOSIT_8;
                    case 64:
                        return DEPOSIT_64;
                }
            case "DIAMOND_BLOCK":
                return DEPOSIT_ALL;
            case "EMERALD":
                switch (n) {
                    case 1:
                        return WITHDRAW_1;
                    case 8:
                        return WITHDRAW_8;
                    case 64:
                        return WITHDRAW_64;
                }
            case "NETHER_STAR":
                return TAUX;
            case "PLAYER_HEAD":
                return SOLDE;
        }
    }

}
