package fr.tim.smpbank;

import fr.tim.smpbank.bank.Bank;

import fr.tim.smpbank.bank.taux.Taux;
import fr.tim.smpbank.commands.Gui;
import fr.tim.smpbank.files.Autosave;
import fr.tim.smpbank.files.FileManager;
import fr.tim.smpbank.items.CustomCraft;
import fr.tim.smpbank.listeners.ListenerManager;
import org.bukkit.plugin.java.JavaPlugin;


public class StonksExchange extends JavaPlugin {

    public static StonksExchange plugin;
    public Taux taux;

    @Override
    public void onEnable() {
        ListenerManager.registerEvents(this);
        registerCommands();
        new FileManager();
        new CustomCraft();
        Autosave.loop();

        this.taux = new Taux();
        this.taux.dailyUpdate();


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

    public static StonksExchange getPlugin() {
        return plugin;
    }

    public Taux getTaux() {
        return this.taux;
    }

    private void registerCommands() {
        getCommand("bank").setExecutor(new Gui());

    }
}
