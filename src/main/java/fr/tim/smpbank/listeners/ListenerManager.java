package fr.tim.smpbank.listeners;

import fr.tim.smpbank.gui.GestionPDA;
import fr.tim.smpbank.StonksExchange;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;


public class ListenerManager {

    public static void registerEvents(StonksExchange plugin) {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PlayerJoin(),plugin);
        pm.registerEvents(new InterfaceInteraction(),plugin);
        pm.registerEvents(new GetTauxParametres(),plugin);
        pm.registerEvents(new GestionPDA(),plugin);
    }
}
