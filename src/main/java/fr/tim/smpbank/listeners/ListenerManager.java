package fr.tim.smpbank.listeners;

import fr.tim.smpbank.StonksExchange;
import fr.tim.smpbank.bank.vault.PocketVault;
import fr.tim.smpbank.bank.vault.Vault;
import fr.tim.smpbank.gui.pda.GestionPDA;
import fr.tim.smpbank.listeners.bank.InterfaceInteraction;
import fr.tim.smpbank.listeners.environment.AntiEChest;
import fr.tim.smpbank.listeners.environment.PlayerJoin;
import fr.tim.smpbank.listeners.taux.GetTauxParametres;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;


public class ListenerManager {

    public static void registerEvents(StonksExchange plugin) {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PlayerJoin(),plugin);
        pm.registerEvents(new InterfaceInteraction(),plugin);
        pm.registerEvents(new GetTauxParametres(),plugin);
        pm.registerEvents(new GestionPDA(),plugin);
        pm.registerEvents(new AntiEChest(),plugin);
        pm.registerEvents(new Vault(),plugin);
        pm.registerEvents(new PocketVault(),plugin);
    }
}
