package fr.tim.stonkexchange.files;

import fr.tim.stonkexchange.StonkExchange;

import java.io.File;

public class FileManager {

    public static final String BANK_PATH = StonkExchange.getPlugin().getDataFolder() + File.separator + "bank" + File.separator;
    public static final String LOG_PATH = StonkExchange.getPlugin().getDataFolder() + File.separator + "logs" + File.separator;
    public static final String CONFIG_PATH = StonkExchange.getPlugin().getDataFolder() + File.separator + "";

    public FileManager() {
        initDataFolder();
    }

    private void initDataFolder() {
        if (!StonkExchange.getPlugin().getDataFolder().exists()) {
            StonkExchange.getPlugin().getDataFolder().mkdir();
        }
        File f = new File(BANK_PATH);
        if (!f.exists()) f.mkdir();

        f = new File(LOG_PATH);
        if (!f.exists()) f.mkdir();

        StonkExchange.getPlugin().saveDefaultConfig();
    }
}
