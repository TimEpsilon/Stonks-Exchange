package fr.tim.smpbank.bank;

import org.bukkit.entity.Player;

public class Bank {

    private final Player player;
    private float solde;

    public Bank(Player player) {
        this.player = player;
        this.solde = 0;
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

}
