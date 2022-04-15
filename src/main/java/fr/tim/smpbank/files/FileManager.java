package fr.tim.smpbank.files;

import fr.tim.smpbank.StonksExchange;

import java.io.File;

public class FileManager {

    public static final String BANK_PATH = StonksExchange.getPlugin().getDataFolder() + File.separator + "bank" + File.separator;
    public static final String CONFIG_PATH = StonksExchange.getPlugin().getDataFolder() + File.separator + "";

    public FileManager() {
        initDataFolder();
    }

    private void initDataFolder() {
        if (!StonksExchange.getPlugin().getDataFolder().exists()) {
            StonksExchange.getPlugin().getDataFolder().mkdir();
        }
        File f = new File(BANK_PATH);
        if (!f.exists()) f.mkdir();
        StonksExchange.getPlugin().saveDefaultConfig();
    }
}
