package fr.tim.smpbank.files;

import fr.tim.smpbank.bank.Bank;
import fr.tim.smpbank.StonksExchange;
import fr.tim.smpbank.bank.taux.Taux;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public abstract class Autosave {

    public static void loop() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!Bank.bankList.containsKey(p.getUniqueId())) {
                new Bank(p);
            }
        }

        Bukkit.getScheduler().runTaskTimerAsynchronously(StonksExchange.getPlugin(), () -> {
            Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "Sauvegarde des donnees...");

            for (Bank bank : Bank.bankList.values()) {
                bank.logBankState();
            }

        },0, Taux.time*20);
    }
}
