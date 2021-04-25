package fr.tim.smpbank.gui;

import fr.tim.smpbank.bank.Bank;
import fr.tim.smpbank.smpBank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class Interface {
    private final Inventory inventory;
    private final Bank bank;
    float taux = smpBank.getPlugin().getTaux();

    /**Objet Interface
     * Sert d'interface avec la banque.
     * Uniquement la partie visuelle, les interactions se font au niveau des listeners.
     * Objet unique à chaque banque et donc au joueur.
     * Doit s'actualiser à chaque transaction.
     * @param b la bank
     */
    public Interface(Bank b) {
        this.bank = b;

        this.inventory = Bukkit.createInventory(null, 27, ChatColor.GREEN + "Banque");

        setItem(7, "minecraft:diamond", 1, ChatColor.GREEN + "+1", ChatColor.GRAY + "Déposer 1 diamant",0);
        setItem(16, "minecraft:diamond", 8, ChatColor.GREEN + "+8", ChatColor.GRAY + "Déposer 8 diamants",0);
        setItem(25, "minecraft:diamond", 64, ChatColor.GREEN + "+64", ChatColor.GRAY + "Déposer 64 diamants",0);
        setItem(15, "minecraft:diamond_block", 1, ChatColor.GREEN + "All", ChatColor.GRAY + "Tout déposer",0);
        setItem(5, "minecraft:emerald", 1, ChatColor.RED + "-1", ChatColor.GRAY + "Retirer 1 M-coin",42);
        setItem(14, "minecraft:emerald", 8, ChatColor.RED + "-8", ChatColor.GRAY + "Retirer 8 M-coins",42);
        setItem(23, "minecraft:emerald", 64, ChatColor.RED + "-64", ChatColor.GRAY + "Retirer 64 M-coins",42);

        reload();

        setItem(9, "minecraft:nether_star", 1, ChatColor.YELLOW + "Taux du M-coin actuel :",ChatColor.GRAY + "" + taux,0);
    }

    /**setItem
     * Permet de placer un item spécifique dans l'interface.
     * Le code est un peu maladroit mais il fait le travail correctement.
     * @param pos la position dans l'inventaire, de 0 à 26.
     * @param id l'identifiant minecraft de l'item à apparaitre. Attention à bien l'écrire sinon il lance une NullPointerException.
     * @param amount la quantité d'items.
     * @param name le CustomName.
     * @param lore le Lore.
     * @param modelData le CustomModelData.
     */
    public void setItem(int pos, String id, int amount, String name, String lore,int modelData) {

        ItemStack item = new ItemStack(Material.matchMaterial(id),amount);
        ItemMeta meta = item.getItemMeta();
        List<String> Lore = new ArrayList<>();
        Lore.add(lore);

        meta.setDisplayName(name);
        meta.setLore(Lore);
        meta.setCustomModelData(modelData);
        item.setItemMeta(meta);
        this.inventory.setItem(pos, item);
    }

    public Inventory getGUI() {
        return inventory;
    }

    public void reload() {
        setItem(12, "minecraft:player_head", 1, ChatColor.GOLD + "Solde : ", "§d" + this.bank.getSolde(),0);
        ItemStack item = this.inventory.getItem(12);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(this.bank.getUuid()));
        item.setItemMeta(meta);
        this.inventory.setItem(12, item);

        setItem(9, "minecraft:nether_star", 1, ChatColor.YELLOW + "Taux du M-coin actuel :",ChatColor.GRAY + "" + smpBank.getPlugin().getTaux(),0);
    }

}
