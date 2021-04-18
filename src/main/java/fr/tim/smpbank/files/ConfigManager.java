package fr.tim.smpbank.files;

import fr.tim.smpbank.smpBank;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigManager {

    private smpBank plugin = smpBank.getPlugin(smpBank.class);

    public FileConfiguration datecfg;
    public File datefile;

    public void setup(String date) {
        if(!plugin.getDataFolder().exists()) plugin.getDataFolder().mkdir();

        datefile = new File(plugin.getDataFolder(),date);

        if(!datefile.exists()) {
            try {
                datefile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        datecfg = YamlConfiguration.loadConfiguration(datefile);
    }

    public void saveDate() {
        try {
            datecfg.save(datefile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reloadDate() {
        datecfg = YamlConfiguration.loadConfiguration(datefile);
        Bukkit.broadcastMessage("§2Reload de la banque");
    }

    public FileConfiguration getDate() {
        return datecfg;
    }
}
