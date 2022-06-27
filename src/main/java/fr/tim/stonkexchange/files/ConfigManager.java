package fr.tim.stonkexchange.files;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ConfigManager {

    private static FileConfiguration load() {
        File file = new File(FileManager.CONFIG_PATH + "config.yml");
        return YamlConfiguration.loadConfiguration(file);
    }

    public static long getTimer() {
        FileConfiguration fc = load();
        return fc.getLong("Periode");
    }

    public static String getString(String path) {
        FileConfiguration fc = load();
        return fc.getString(path);
    }

    public static float[] getCoeff(String path) {
        FileConfiguration fc = load();
        float[] coeff = new float[7];

        switch (path) {

            case "Taux.variation":
                coeff[0]= (float)fc.getDouble(path+".m");
                coeff[1] = (float)fc.getDouble(path+".M");
                break;

            case "Taux.joueurs":
                coeff[0] = (float)fc.getDouble(path+".m");
                coeff[1] = (float)fc.getDouble(path+".a");
                break;

            case "Taux.mort":
            case "Taux.diamonds":
            case "Taux.boss":
            case "Taux.advancement":
            case "Taux.ores":
                coeff[0] = (float)fc.getDouble(path+".m");
                break;

            case "Taux.Somme":
                coeff[0] = (float)fc.getDouble(path+".aJoueurs");
                coeff[1] = (float)fc.getDouble(path+".aVariation");
                coeff[2] = (float)fc.getDouble(path+".aMort");
                coeff[3] = (float)fc.getDouble(path+".aDiamonds");
                coeff[4] = (float)fc.getDouble(path+".aBoss");
                coeff[5] = (float)fc.getDouble(path+".aAdvancement");
                coeff[6] = (float)fc.getDouble(path+".aOres");
                break;

            case "Taux.Pente":
                coeff[0] = (float)fc.getDouble(path+".a");
                coeff[1] = (float)fc.getDouble(path+".r");
                break;

            case "Taux.Retour":
                coeff[0] = (float)fc.getDouble(path+".r");
        }

        return coeff;
    }

}
