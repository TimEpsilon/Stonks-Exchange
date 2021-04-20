package fr.tim.smpbank.files;

import fr.tim.smpbank.bank.Bank;
import fr.tim.smpbank.smpBank;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.UUID;

public class Autosave {

    public static void loop() {
        long delay = LocalTime.now().until(LocalTime.parse("00:00:00"), ChronoUnit.SECONDS)*20;

        Bukkit.getScheduler().runTaskTimerAsynchronously(smpBank.getPlugin(), new Runnable() {
            @Override
            public void run() {
                Bukkit.broadcastMessage("§2Sauvegarde des donnees...");
                loadConfigManager();

            }
        },delay,432000);
    }


    public static void loadConfigManager() {
        String date = LocalDate.now().getDayOfMonth() + "_" + LocalDate.now().getMonthValue() + "_" + LocalDate.now().getYear();
        ConfigManager cfgm = new ConfigManager();
        cfgm.setup(date);
        write(cfgm);
        cfgm.saveDate();
        cfgm.reloadDate();
    }

    private static void write(ConfigManager cfgm) {
        FileConfiguration fc = cfgm.getDate();
        HashMap<UUID, Bank> players = smpBank.getPlugin().getListeJoueurs();

        for (UUID uuid : players.keySet()) {
            float solde = players.get(uuid).getSolde();
            String name = players.get(uuid).getName();
            fc.set("Player." + uuid + ".Solde", solde);
            fc.set("Player." + uuid + ".Name", name);
        }

        cfgm.saveDate();
    }

}
