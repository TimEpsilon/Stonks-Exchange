package fr.tim.smpbank.bank;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.UUID;

public class Bank {

    private Player player;
    private float solde;

    public Bank(Player player) {
        this.player = player;
        this.solde = solde;
    }

    public float getSolde() {
        return this.solde;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setSolde(float newSolde) {
        this.solde = newSolde;
    }

    /**Deposit
     * Permet de déposer une quantité n de m-coin et diamond
     * dans cet ordre.
     * Multiplie le nombre de diamants déposés par le taux du jour.
     *Depose le minimum entre n et le nombre d'items possibles dans l'inventaire.
     * @param n un entier représentant le nombre d'items à déposer dans le compte.
     */
    public void deposit(int n) {
        Player p = this.player;

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
        int coin = 0;

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
        float stonks =5; //Récupérer taux du jour

        this.setSolde(this.getSolde() + depositD * stonks + depositM);
        //Ajouter sauvegarde

        return;
    }
}
