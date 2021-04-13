package fr.tim.smpbank.bank;

import fr.tim.smpbank.gui.Interface;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Bank {

    private final String name;
    private final UUID uuid;
    private float solde;
    private final Interface gui;

    public Bank(Player player) {
        this.name = player.getName();
        this.uuid = player.getUniqueId();
        this.solde = 0;
        this.gui = new Interface(this);
    }

    public float getSolde() {
        return this.solde;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public Interface getGUI() {
        return this.gui;
    }

    public void setSolde(float newSolde) {
        this.solde = newSolde;
    }


}
