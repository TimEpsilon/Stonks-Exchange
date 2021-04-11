package fr.tim.smpbank;

import fr.tim.smpbank.bank.Bank;
import fr.tim.smpbank.commands.Gui;
import fr.tim.smpbank.listeners.ListenerManager;
import org.bukkit.Bukkit;
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

    public static smpBank getPlugin() {
        return plugin;
    }


    private void addOnline() {
        HashMap<UUID,Bank> liste = getListeJoueurs();
        for (Player p : this.getServer().getOnlinePlayers()) {
            if (!liste.containsKey(p.getUniqueId())){
                Bukkit.broadcastMessage("test");
                liste.put(p.getUniqueId(),new Bank(p));
            }
        }
    }

    private void registerCommands() {
        getCommand("bank").setExecutor(new Gui());
    }
}
