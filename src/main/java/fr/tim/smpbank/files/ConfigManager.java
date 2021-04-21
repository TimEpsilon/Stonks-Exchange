package fr.tim.smpbank.files;

import fr.tim.smpbank.smpBank;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigManager {

    private smpBank plugin = smpBank.getPlugin(smpBank.class);
    private FileConfiguration datecfg;
    private File datefile;

    /**Setup
     * Met en place le fichier de la date spécifiée
     * @param date
     */
    public void setup(String date) {
        //Crée le datafolder
        if(!plugin.getDataFolder().exists()) plugin.getDataFolder().mkdir();

        //Crée le fichier .yml
        datefile = new File(plugin.getDataFolder(),date+".yml");

        //L'enregistre
        if(!datefile.exists()) {
            try {
                datefile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //Assigne la configuration du fichier .yml
        datecfg = YamlConfiguration.loadConfiguration(datefile);
    }

    /**SaveDate
     * Sauvegarde les modifications faites sur la config
     */
    public void saveDate() {
        try {
            datecfg.save(datefile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**ReloadDate
     * Récupère la config à partir du fichier
     */
    public void reloadDate() {
        datecfg = YamlConfiguration.loadConfiguration(datefile);
        Bukkit.broadcastMessage("§2Reload de la banque");
    }

    public FileConfiguration getDate() {
        return datecfg;
    }
}
