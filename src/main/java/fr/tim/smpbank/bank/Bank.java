package fr.tim.smpbank.bank;

import fr.tim.smpbank.gui.Interface;
import org.bukkit.entity.Player;

public class Bank {

    private final Player player;
    private float solde;
    private final Interface gui;

    public Bank(Player player) {
        this.player = player;
        this.solde = 0;
        this.gui = new Interface(this);
    }

    public float getSolde() {
        return this.solde;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Interface getGUI() {
        return this.gui;
    }

    public void setSolde(float newSolde) {
        this.solde = newSolde;
    }


}
