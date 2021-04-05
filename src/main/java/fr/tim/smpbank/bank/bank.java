package fr.tim.smpbank.bank;

import org.bukkit.entity.Player;

import java.util.UUID;

public class bank {

    private String name;
    private final UUID uuid;
    private float solde;

    public bank(Player player) {
        this.name = player.getName();
        this.uuid = player.getUniqueId();
        this.solde = solde;
    }

    public float getSolde() {
        return this.solde;
    }

    public String getName() {
        return this.name;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public void setName(String newName) {
        if (this.name != newName) this.name = newName;
    }

    public void setSolde(float newSolde) {
        this.solde = newSolde;
    }
}
