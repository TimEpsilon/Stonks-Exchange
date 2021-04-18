package fr.tim.smpbank.files;

import fr.tim.smpbank.smpBank;
import org.bukkit.Bukkit;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

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


    private static void loadConfigManager() {
        String date = LocalDate.now().getDayOfMonth() + "_" + LocalDate.now().getMonthValue() + "_" + LocalDate.now().getYear();
        ConfigManager cfgm = new ConfigManager();
        cfgm.setup(date);
        cfgm.saveDate();
        cfgm.reloadDate();
    }

}
