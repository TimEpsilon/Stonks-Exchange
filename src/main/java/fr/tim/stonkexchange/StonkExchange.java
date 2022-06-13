package fr.tim.stonkexchange;

import fr.tim.stonkexchange.bank.Bank;

import fr.tim.stonkexchange.bank.taux.Taux;
import fr.tim.stonkexchange.commands.Gui;
import fr.tim.stonkexchange.commands.OnTabComplete;
import fr.tim.stonkexchange.commands.ShowRank;
import fr.tim.stonkexchange.commands.VaultSpawn;
import fr.tim.stonkexchange.files.Autosave;
import fr.tim.stonkexchange.files.FileManager;
import fr.tim.stonkexchange.files.Logs;
import fr.tim.stonkexchange.items.CustomCraft;
import fr.tim.stonkexchange.listeners.ListenerManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;


public class StonkExchange extends JavaPlugin {

    public static StonkExchange plugin;
    public Taux taux;
    public static Logs logs;

    @Override
    public void onEnable() {
        startUpMessage();

        ListenerManager.registerEvents(this);
        registerCommands();
        new FileManager();
        new CustomCraft();
        Autosave.loop();

        taux = new Taux();
        taux.loadData();
        taux.dailyUpdate();

        logs = new Logs();
    }

    @Override
    public void onDisable() {
        for (Bank bank : Bank.bankList.values()) {
            bank.logBankState();
        }
    }

    @Override
    public void onLoad() {
        plugin = this;
    }

    public static StonkExchange getPlugin() {
        return plugin;
    }

    public Taux getTaux() {
        return this.taux;
    }

    private void registerCommands() {
        getCommand("bank").setExecutor(new Gui());
        getCommand("baltop").setExecutor(new ShowRank());
        getCommand("vault").setExecutor(new VaultSpawn());
        getCommand("vault").setTabCompleter(new OnTabComplete());

    }

    public static void startUpMessage() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + " $$$$$$\\    $$\\                         $$\\                 $$$$$$$$\\                     $$\\                                               ");
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "$$  __$$\\   $$ |                        $$ |                $$  _____|                    $$ |                                              ");
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "$$ /  \\__|$$$$$$\\    $$$$$$\\  $$$$$$$\\  $$ |  $$\\  $$$$$$$\\ $$ |      $$\\   $$\\  $$$$$$$\\ $$$$$$$\\   $$$$$$\\  $$$$$$$\\   $$$$$$\\   $$$$$$\\  ");
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "\\$$$$$$\\  \\_$$  _|  $$  __$$\\ $$  __$$\\ $$ | $$  |$$  _____|$$$$$\\    \\$$\\ $$  |$$  _____|$$  __$$\\  \\____$$\\ $$  __$$\\ $$  __$$\\ $$  __$$\\ ");
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + " \\____$$\\   $$ |    $$ /  $$ |$$ |  $$ |$$$$$$  / \\$$$$$$\\  $$  __|    \\$$$$  / $$ /      $$ |  $$ | $$$$$$$ |$$ |  $$ |$$ /  $$ |$$$$$$$$ |");
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "$$\\   $$ |  $$ |$$\\ $$ |  $$ |$$ |  $$ |$$  _$$<   \\____$$\\ $$ |       $$  $$<  $$ |      $$ |  $$ |$$  __$$ |$$ |  $$ |$$ |  $$ |$$   ____|");
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "\\$$$$$$  |  \\$$$$  |\\$$$$$$  |$$ |  $$ |$$ | \\$$\\ $$$$$$$  |$$$$$$$$\\ $$  /\\$$\\ \\$$$$$$$\\ $$ |  $$ |\\$$$$$$$ |$$ |  $$ |\\$$$$$$$ |\\$$$$$$$\\ ");
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + " \\______/    \\____/  \\______/ \\__|  \\__|\\__|  \\__|\\_______/ \\________|\\__/  \\__| \\_______|\\__|  \\__| \\_______|\\__|  \\__| \\____$$ | \\_______|");
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "                                                                                                                        $$\\   $$ |          ");
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "                                                                                                                        \\$$$$$$  |          ");
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "                                                                                                                         \\______/           ");
        Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "by TimEpsilon");
        Bukkit.getConsoleSender().sendMessage(" ");

    }

}
