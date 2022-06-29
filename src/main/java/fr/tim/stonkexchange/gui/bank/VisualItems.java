package fr.tim.stonkexchange.gui.bank;

import fr.tim.stonkexchange.bank.rank.BankRank;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
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
    SOLDE(Material.PLAYER_HEAD, 1, ChatColor.GOLD + "Solde : ", "§d" + "0.0 / 10.0",0,12),
    GROUP(Material.TOTEM_OF_UNDYING,1,ChatColor.BLUE + "Groupe",ChatColor.GRAY + "Informations sur votre groupe",0,0),
    NEXT(Material.ARROW,1,ChatColor.BLUE + "Next Groupe",ChatColor.GRAY + "Passez à votre groupe suivant",0,0),
    OWNER(Material.PLAYER_HEAD,1,ChatColor.LIGHT_PURPLE + "Owner : ","",0,1),
    MEMBERS(Material.BUNDLE,1,ChatColor.YELLOW + "Membres : ", "", 0,19),
    EMBLEM(Material.PAPER,1,ChatColor.GOLD + "GroupName : ", ChatColor.GRAY + "Modifiez votre groupe avec /group",0,10),
    BANK(Material.IRON_INGOT,1,ChatColor.GOLD + "Solde du Groupe : ", "0.0",12,12);

    private final ItemStack item;
    private final int slot;
    private final String name;

    VisualItems(Material material, int count, String name, String lore, int cmd,int slot) {
        List<Component> Lore = new ArrayList<>();
        Lore.add(Component.text(lore));

        this.name = name;
        this.item = new ItemStack(material,count);
        ItemMeta meta = this.item.getItemMeta();
        meta.setCustomModelData(cmd);
        meta.setDisplayName(name);
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

    public static VisualItems searchItem(ItemStack item) {
        Material mat = item.getType();
        int n = item.getAmount();
        Component display = item.getItemMeta().displayName();
        if (display == null) return null;
        switch (mat) {
            case ARROW:
                return NEXT;
            case DIAMOND:
                switch (n) {
                    case 1:
                        return DEPOSIT_1;
                    case 8:
                        return DEPOSIT_8;
                    case 64:
                        return DEPOSIT_64;
                }
            case DIAMOND_BLOCK:
                return DEPOSIT_ALL;
            case EMERALD:
                switch (n) {
                    case 1:
                        return WITHDRAW_1;
                    case 8:
                        return WITHDRAW_8;
                    case 64:
                        return WITHDRAW_64;
                }
            case NETHER_STAR:
                return TAUX;
            case PLAYER_HEAD: {
                if (display.equals(Component.text(SOLDE.getName()))) return SOLDE;
                if (display.equals(Component.text(OWNER.getName()))) return OWNER;
                break;
            }


            case LIME_WOOL:
                return RANK_GREEN;
            case LIGHT_BLUE_WOOL:
                return RANK_BLUE;
            case MAGENTA_WOOL:
                return PURPLE;
            case RED_WOOL:
                return RANK_RED;
            case YELLOW_WOOL:
                return RANK_YELLOW;
            case GOLD_BLOCK:
                return RANK_GOLD;
            case BUNDLE:
                return MEMBERS;
            case IRON_INGOT:
                return BANK;
            case TOTEM_OF_UNDYING:
                return GROUP;
        }

        if (display.equals(Component.text(EMBLEM.getName()))) return EMBLEM;

        return null;
    }

    public static VisualItems getItemByRank(BankRank br) {
        return switch (br) {
            case GREEN -> VisualItems.RANK_GREEN;
            case BLUE -> VisualItems.RANK_BLUE;
            case PURPLE -> VisualItems.PURPLE;
            case RED -> VisualItems.RANK_RED;
            case YELLOW -> VisualItems.RANK_YELLOW;
            case GOLD -> VisualItems.RANK_GOLD;
        };
    }

    public String getName() {
        return name;
    }
}
