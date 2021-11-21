package fr.tim.smpbank.listeners;

import fr.tim.smpbank.gui.GraphRender;
import fr.tim.smpbank.smpBank;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;


public class ListenerManager {

    public static void registerEvents(smpBank plugin) {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PlayerJoin(),plugin);
        pm.registerEvents(new OnClick(),plugin);
        pm.registerEvents(new OnDeath(),plugin);
        pm.registerEvents(new GraphRender(),plugin);
        //add onleft
    }
}
