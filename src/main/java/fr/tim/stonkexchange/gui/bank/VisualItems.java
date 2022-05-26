package fr.tim.stonkexchange.gui.bank;

import fr.tim.stonkexchange.bank.rank.BankRank;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public enum VisualItems {

    DEPOSIT_1(Material.DIAMOND, 1, ChatColor.GREEN + "+1", ChatColor.GRAY + "Déposer 1 diamant",0,7),
    DEPOSIT_8(Material.DIAMOND, 8, ChatColor.GREEN + "+8", ChatColor.GRAY + "Déposer 8 diamants",0,16),
    DEPOSIT_64(Material.DIAMOND, 64, ChatColor.GREEN + "+64", ChatColor.GRAY + "Déposer 64 diamants",0,25),
    WITHDRAW_1(Material.EMERALD, 1, ChatColor.RED + "-1", ChatColor.GRAY + "Retirer  1 M-Coin",42,5),
    WITHDRAW_8(Material.EMERALD, 8, ChatColor.RED + "-8", ChatColor.GRAY + "Retirer 8 M-Coin",42,14),
    WITHDRAW_64(Material.EMERALD, 64, ChatColor.RED + "-64", ChatColor.GRAY + "Retirer 64 M-Coin",42,23),
    DEPOSIT_ALL(Material.DIAMOND_BLOCK,1,ChatColor.GREEN + "All", ChatColor.GRAY + "Tout déposer",0,15),
    TAUX(Material.NETHER_STAR, 1, ChatColor.YELLOW + "Taux du M-coin actuel :",ChatColor.GRAY + " null", 0,10),
    RANK_GREEN(Material.LIME_WOOL,1,ChatColor.GREEN + "Rang : \u9000 Green",ChatColor.GRAY + " Prochain Rang : Blue (" + BankRank.BLUE.getPrice() + ")", 0,21),
    RANK_BLUE(Material.LIGHT_BLUE_WOOL,1,ChatColor.AQUA + "Rang : \u9001 Blue",ChatColor.GRAY + " Prochain Rang : Purple (" + BankRank.PURPLE.getPrice() + ")", 0,21),
    PURPLE(Material.MAGENTA_WOOL,1,ChatColor.LIGHT_PURPLE + "Rang : \u9002 Purple",ChatColor.GRAY + " Prochain Rang : Red (" + BankRank.RED.getPrice() + ")", 0,21),
    RANK_RED(Material.RED_WOOL,1,ChatColor.RED + "Rang : \u9003 Red",ChatColor.GRAY + " Prochain Rang : Yellow (" + BankRank.YELLOW.getPrice() + ")", 0,21),
    RANK_YELLOW(Material.YELLOW_WOOL,1,ChatColor.YELLOW + "Rang : \u9004 Yellow",ChatColor.GRAY + " Prochain Rang : Gold (" + BankRank.GOLD.getPrice() + ")", 0,21),
    RANK_GOLD(Material.GOLD_BLOCK,1,ChatColor.GOLD + "Rang : \u9005 Gold","", 0,21),
    SOLDE(Material.PLAYER_HEAD, 1, ChatColor.GOLD + "Solde : ", "§d" + "0.0 / 10.0",0,12);

    private ItemStack item;
    private int slot;

    VisualItems(Material material, int count, String name, String lore, int cmd,int slot) {
        List<Component> Lore = new ArrayList<>();
        Lore.add(Component.text(lore));

        this.item = new ItemStack(material,count);
        ItemMeta meta = this.item.getItemMeta();
        meta.setCustomModelData(cmd);
        meta.displayName(Component.text(name));
        meta.lore(Lore);
        this.item.setItemMeta(meta);

        this.slot = slot;

    }

    public ItemStack getItem() {
        return this.item;
    }

    public int getSlot() {
        return this.slot;
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
            case "LIME_WOOL":
                return RANK_GREEN;
            case "LIGHT_BLUE_WOOL":
                return RANK_BLUE;
            case "MAGENTA_WOOL":
                return PURPLE;
            case "RED_WOOL":
                return RANK_RED;
            case "YELLOW_WOOL":
                return RANK_YELLOW;
            case "GOLD_BLOCK":
                return RANK_GOLD;
        }
        return null;
    }

    public static VisualItems getItemByRank(BankRank br) {
        switch (br) {
            case GREEN:
                return VisualItems.RANK_GREEN;
            case BLUE:
                return VisualItems.RANK_BLUE;
            case PURPLE:
                return VisualItems.PURPLE;
            case RED:
                return VisualItems.RANK_RED;
            case YELLOW:
                return VisualItems.RANK_YELLOW;
            case GOLD:
                return VisualItems.RANK_GOLD;
        }
        return null;
        }
}
