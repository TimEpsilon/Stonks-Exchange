package fr.tim.smpbank;

import fr.tim.smpbank.bank.Bank;

import fr.tim.smpbank.commands.*;
import fr.tim.smpbank.files.Autosave;
import fr.tim.smpbank.listeners.ListenerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public class smpBank extends JavaPlugin implements Listener {

    HashMap<UUID, Bank> listeJoueurs = new HashMap<>();
    HashMap<UUID,Boolean> joined = new HashMap<>();
    HashMap<UUID,Boolean> dead = new HashMap<>();
    public static smpBank plugin;
    public float taux;

    @Override
    public void onEnable() {
        ListenerManager.registerEvents(this);
        registerCommands();
        addOnline();
        Autosave.read();
        Autosave.loop();
        fr.tim.smpbank.bank.Taux.dailyTaux();
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        Autosave.loadConfigManager();
    }

    @Override
    public void onLoad() {
        plugin = this;
    }

    public HashMap<UUID, Bank> getListeJoueurs() {
        return listeJoueurs;
    }

    public HashMap<UUID, Boolean> getJoined() {
        return joined;
    }

    public HashMap<UUID, Boolean> getDead() {
        return dead;
    }

    public static smpBank getPlugin() {
        return plugin;
    }

    public float getTaux() {
        return taux;
    }

    public void setTaux(float stonks) {
        taux = stonks;
    }


    private void addOnline() {
        HashMap<UUID,Bank> liste = getListeJoueurs();
        for (Player p : this.getServer().getOnlinePlayers()) {
            if (!liste.containsKey(p.getUniqueId())){
                liste.put(p.getUniqueId(),new Bank(p));
            }
        }
    }

    private void registerCommands() {
        getCommand("bank").setExecutor(new Gui());
        getCommand("forcesave").setExecutor(new Forcesave());
        getCommand("deposit").setExecutor(new Deposit());
        getCommand("withdraw").setExecutor(new Withdraw());
        getCommand("taux").setExecutor(new Taux());

    }
}
