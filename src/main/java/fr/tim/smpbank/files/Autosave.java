package fr.tim.smpbank.files;

import fr.tim.smpbank.bank.Bank;
import fr.tim.smpbank.smpBank;
import org.bukkit.Bukkit;

public abstract class Autosave {

    public static void loop() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(smpBank.getPlugin(), () -> {
            Bukkit.broadcastMessage("§2Sauvegarde des donnees...");

            for (Bank bank : Bank.bankList.values()) {
                bank.logBankState();
            }

        },0,288000);
    }
}
