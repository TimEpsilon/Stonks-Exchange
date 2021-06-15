package fr.tim.smpbank.bank;

import fr.tim.smpbank.smpBank;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;

public class Trader {

    /**Deposit
     * Permet de déposer une quantité n de m-coin et diamond
     * dans cet ordre.
     * Multiplie le nombre de diamants déposés par le taux du jour.
     *Depose le minimum entre n et le nombre d'items possibles dans l'inventaire.
     * @param n un entier représentant le nombre d'items à déposer dans le compte.
     */
    public static void deposit(int n,Player p) {

        Bank b = smpBank.getPlugin().getListeJoueurs().get(p.getUniqueId());

        //Uniquement si l'inventaire contient des emeralds ou diamonds
        if (!p.getInventory().contains(Material.EMERALD) && !p.getInventory().contains(Material.DIAMOND)) return;

        //Définition du m-coin
        //Le custom model data est toujours 42
        //Le nom est aqua en gras
        ItemStack mcoin = new ItemStack(Material.EMERALD,n);
        ItemMeta Mmeta = mcoin.getItemMeta();
        Mmeta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "M-Coin");
        Mmeta.setCustomModelData(42);
        mcoin.setItemMeta(Mmeta);

        //Compteurs
        int totalDiamond = 0;
        int totalMcoin = 0;
        int diamond = 0;
        int coin;

        //Parcours de l'inventaire, dénombrement des mcoins et diamonds
        for (ItemStack item : p.getInventory().getContents()) {
            if (item == null) continue;
            if (item.isSimilar(mcoin)) totalMcoin += item.getAmount();
            if (item.isSimilar(new ItemStack(Material.DIAMOND))) totalDiamond += item.getAmount();
        }
        //Nombre maximal d'items dans l'inventaire
        if (n>=2304) {
            p.getInventory().remove(mcoin);
            p.getInventory().remove(new ItemStack(Material.DIAMOND));

            diamond = totalDiamond;
            coin = totalMcoin;
        }

        int reste = 0;

        //Supprime au mieux les mcoins
        HashMap<Integer,ItemStack> notRemoved = p.getInventory().removeItem(mcoin);
        if (notRemoved.size()==0) {
            coin = n;
        } else {
            for (ItemStack item : notRemoved.values()) {
                if (item.isSimilar(mcoin)) {
                    reste += item.getAmount();
                }
            }

            coin = totalMcoin;
            notRemoved.clear();
            notRemoved = p.getInventory().removeItem(new ItemStack(Material.DIAMOND,reste));

            if (notRemoved.size()==0) {
                diamond = reste;
            } else {
                diamond = totalDiamond;
            }
            notRemoved.clear();
        }

        final int depositD = diamond;
        final int depositM = coin;
        float stonks = smpBank.getPlugin().getTaux();

        b.setSolde(Math.round((b.getSolde() + depositD * stonks + depositM)*1000.0d)/1000.0d);

    }

    /**Withdraw
     * Permet de retirer une quantité n de mcoins.
     * Remplit au mieux l'inventaire.
     * Ne retire au compte que ce qu'il peut ajouter à l'inventaire.
     * @param n un entier représentant le nombre de mcoins à retirer du compte.
     */
    public static void withdraw(int n,Player p) {

        Bank b = smpBank.getPlugin().getListeJoueurs().get(p.getUniqueId());
        double solde = b.getSolde();
        int thune;
        int resteThune = 0;

        //Pas de solde négatif autorisé
        if (n < solde) {
            thune = n;
        } else {
            thune = (int) Math.floor(solde);
        }

        //Définition du m-coin
        //Le custom model data est toujours 42
        //Le nom est aqua en gras
        ItemStack mcoin = new ItemStack(Material.EMERALD,thune);
        ItemMeta Mmeta = mcoin.getItemMeta();
        Mmeta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "M-Coin");
        Mmeta.setCustomModelData(42);
        mcoin.setItemMeta(Mmeta);

        //Hashmap représentant les itemstack qui n'ont pas pu être ajouté
        HashMap<Integer,ItemStack> reste = p.getInventory().addItem(mcoin);

        //Compteur des items non ajoutés par manque de place
        if (reste.size() !=0) {
            for (ItemStack item : reste.values()) {
                resteThune += item.getAmount();
            }
        }

        reste.clear();

        b.setSolde(solde-thune+resteThune);
    }
}
