package fr.tim.smpbank.files;

import fr.tim.smpbank.smpBank;

import java.io.File;

public class FileManager {

    public static final String BANK_PATH = smpBank.getPlugin().getDataFolder() + File.separator + "";

    public FileManager() {
        initDataFolder();
    }

    private void initDataFolder() {
        if (!smpBank.getPlugin().getDataFolder().exists()) {
            smpBank.getPlugin().getDataFolder().mkdir();
            smpBank.getPlugin().saveDefaultConfig();
        }
    }
}
