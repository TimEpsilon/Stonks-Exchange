package fr.tim.smpbank.files;

import fr.tim.smpbank.StonksExchange;

import java.io.File;

public class FileManager {

    public static final String BANK_PATH = StonksExchange.getPlugin().getDataFolder() + File.separator + "";

    public FileManager() {
        initDataFolder();
    }

    private void initDataFolder() {
        if (!StonksExchange.getPlugin().getDataFolder().exists()) {
            StonksExchange.getPlugin().getDataFolder().mkdir();
        }
        StonksExchange.getPlugin().saveDefaultConfig();
    }
}
