package fr.tim.smpbank.files;

import fr.tim.smpbank.bank.Bank;
import fr.tim.smpbank.smpBank;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.UUID;

public class Autosave {

    public static void loop() {
        long delay = LocalTime.now().until(LocalTime.parse("00:00:00"), ChronoUnit.SECONDS)*20;

        Bukkit.getScheduler().runTaskTimerAsynchronously(smpBank.getPlugin(), () -> {
            Bukkit.broadcastMessage("§2Sauvegarde des donnees...");
            loadConfigManager();

        },delay,432000);
    }


    public static void loadConfigManager() {
        String date = dateFormat(0);
        ConfigManager cfgm = new ConfigManager();
        cfgm.setup(date);
        write(cfgm);
        cfgm.reloadDate();
    }

    private static void write(ConfigManager cfgm) {
        FileConfiguration fc = cfgm.getDate();
        HashMap<UUID, Bank> players = smpBank.getPlugin().getListeJoueurs();

        for (UUID uuid : players.keySet()) {
            double solde = players.get(uuid).getSolde();
            String name = players.get(uuid).getName();
            fc.set("Player." + uuid + ".Solde", solde);
            fc.set("Player." + uuid + ".Name", name);
        }

        cfgm.saveDate();
    }

    public static void read() {
        int prevDay=0;
        File datefile = new File(smpBank.getPlugin().getDataFolder(),dateFormat(prevDay)+".yml");
        while (!datefile.exists()) {
            prevDay ++;
            datefile = new File(smpBank.getPlugin().getDataFolder(),dateFormat(prevDay)+".yml");
        }

        FileConfiguration fc = YamlConfiguration.loadConfiguration(datefile);
        HashMap<UUID, Bank> players = smpBank.getPlugin().getListeJoueurs();
        double solde = 0;

        for (OfflinePlayer op : smpBank.getPlugin().getServer().getOfflinePlayers()) {
            if (!players.containsKey(op.getUniqueId())) {
                players.put(op.getUniqueId(),new Bank(op));
            }
            if (fc.contains("Player." + op.getUniqueId() + ".Solde")) {
                solde = (double) fc.get("Player." + op.getUniqueId() + ".Solde");
            }
            smpBank.getPlugin().getServer().getConsoleSender().sendMessage(""+solde);
            Bank b = players.get(op.getUniqueId());

            b.setSolde(solde);
        }
    }

    private static String dateFormat(int prevDay) {
        return LocalDate.now().minusDays(prevDay).getDayOfMonth() + "_" + LocalDate.now().minusDays(prevDay).getMonthValue() + "_" + LocalDate.now().minusDays(prevDay).getYear();
    }

}
