package fr.tim.smpbank;

import fr.tim.smpbank.bank.bank;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public class smpBank extends JavaPlugin implements Listener {

    HashMap<UUID,bank> listeJoueurs;

    @Override
    public void onEnable() {
    this.getServer().getPluginManager().registerEvents(this,this);
    }

    @Override
    public void onDisable() {

    }

    private HashMap<UUID, bank> getListeJoueurs() {
        return listeJoueurs;
    }

}
