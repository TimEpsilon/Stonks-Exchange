package fr.tim.smpbank;

import fr.tim.smpbank.bank.Bank;

import fr.tim.smpbank.commands.Deposit;
import fr.tim.smpbank.commands.Forcesave;
import fr.tim.smpbank.commands.Gui;
import fr.tim.smpbank.commands.Withdraw;
import fr.tim.smpbank.files.Autosave;
import fr.tim.smpbank.listeners.ListenerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public class smpBank extends JavaPlugin implements Listener {

    HashMap<UUID, Bank> listeJoueurs = new HashMap<>();
    public static smpBank plugin;

    @Override
    public void onEnable() {
        ListenerManager.registerEvents(this);
        registerCommands();
        addOnline();
        Autosave.read();
        Autosave.loop();
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

    public static smpBank getPlugin() {
        return plugin;
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

    }
}
