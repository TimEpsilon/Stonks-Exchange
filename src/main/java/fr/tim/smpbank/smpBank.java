package fr.tim.smpbank;

import fr.tim.smpbank.bank.Bank;
import fr.tim.smpbank.listeners.ListenerManager;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public class smpBank extends JavaPlugin implements Listener {

    HashMap<UUID, Bank> listeJoueurs;
    public static smpBank plugin;

    @Override
    public void onEnable() {
        ListenerManager.registerEvents(this);
    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onLoad() {
        plugin = this;
    }

    public HashMap<UUID, Bank> getListeJoueurs() {
        return listeJoueurs;
    }

    public static smpBank getPlugin() { return plugin; }

}
