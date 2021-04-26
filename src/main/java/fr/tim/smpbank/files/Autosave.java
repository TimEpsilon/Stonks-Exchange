package fr.tim.smpbank.files;

import fr.tim.smpbank.bank.Bank;
import fr.tim.smpbank.smpBank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.UUID;

public class Autosave {

    public static void loop() {
        long delay = LocalTime.now().until(LocalTime.parse("23:59:00"), ChronoUnit.SECONDS)*20;

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
        double total = 0;

        for (UUID uuid : players.keySet()) {
            double solde = players.get(uuid).getSolde();
            total += solde;
            String name = players.get(uuid).getName();
            fc.set("Player." + uuid + ".Solde", solde);
            fc.set("Player." + uuid + ".Name", name);
        }

        fc.set("Total",total);
        fc.set("Taux",smpBank.getPlugin().getTaux());
        fc.set("Death",smpBank.getPlugin().getDead().size());
        fc.set("Joined",smpBank.getPlugin().getJoined().size());

        cfgm.saveDate();
    }

    public static void read() {
        File datefile = getLastSave(0);
        if (datefile == null) return;

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

            Bank b = players.get(op.getUniqueId());
            smpBank.getPlugin().setTaux((float)fc.getDouble("Taux"));
            b.setSolde(solde);
        }
    }

    public static double getTotalSolde(int prevDay) {
        double total = 0;
        try {
            File datefile = getLastSave(prevDay);
            FileConfiguration fc = YamlConfiguration.loadConfiguration(datefile);
            total = fc.getDouble("Total");
        } catch (Exception e) {
            Bukkit.broadcastMessage(ChatColor.RED + "Fichier non trouvé");
        }


        return total;
    }

    public static float getTauxDate(int prevDay) {
        float taux = 5;
        try {
            File datefile = getLastSave(prevDay);
            FileConfiguration fc = YamlConfiguration.loadConfiguration(datefile);
            taux = (float) fc.getDouble("Taux");
        } catch (Exception e) {
            Bukkit.broadcastMessage(ChatColor.RED + "Fichier non trouvé");
        }
        return taux;
    }


    private static File getLastSave(int minusDay) {
        int prevDay=0;
        File datefile = new File(smpBank.getPlugin().getDataFolder(),dateFormat(prevDay-minusDay)+".yml");
        if (smpBank.getPlugin().getDataFolder().listFiles().length == 0) return null;

        while (!datefile.exists()) {
            prevDay ++;
            datefile = new File(smpBank.getPlugin().getDataFolder(),dateFormat(prevDay-minusDay)+".yml");
        }
        return datefile;
    }

    private static String dateFormat(int prevDay) {
        return LocalDate.now().minusDays(prevDay).getDayOfMonth() + "_" + LocalDate.now().minusDays(prevDay).getMonthValue() + "_" + LocalDate.now().minusDays(prevDay).getYear();
    }

}
