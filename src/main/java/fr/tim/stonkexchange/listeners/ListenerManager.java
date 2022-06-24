package fr.tim.stonkexchange.listeners;

import fr.tim.stonkexchange.StonkExchange;
import fr.tim.stonkexchange.bank.vault.PocketVault;
import fr.tim.stonkexchange.bank.vault.Vault;
import fr.tim.stonkexchange.gui.pda.GestionPDA;
import fr.tim.stonkexchange.items.recallpotion.RecallPotion;
import fr.tim.stonkexchange.listeners.bank.InterfaceInteraction;
import fr.tim.stonkexchange.listeners.environment.AntiEChest;
import fr.tim.stonkexchange.listeners.environment.PlayerJoin;
import fr.tim.stonkexchange.listeners.taux.GetTauxParametres;
import fr.tim.stonkexchange.misc.PlayerHeadPersistent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;


public class ListenerManager {

    public static void registerEvents(StonkExchange plugin) {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PlayerJoin(),plugin);
        pm.registerEvents(new InterfaceInteraction(),plugin);
        pm.registerEvents(new GetTauxParametres(),plugin);
        pm.registerEvents(new GestionPDA(),plugin);
        pm.registerEvents(new AntiEChest(),plugin);
        pm.registerEvents(new Vault(),plugin);
        pm.registerEvents(new PocketVault(),plugin);
        pm.registerEvents(new PlayerHeadPersistent(),plugin);
        pm.registerEvents(new RecallPotion(),plugin);
    }
}
