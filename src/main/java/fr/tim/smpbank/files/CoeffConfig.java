package fr.tim.smpbank.files;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class CoeffConfig {

    private static FileConfiguration load() {
        File file = new File(FileManager.CONFIG_PATH + "config.yml");
        return YamlConfiguration.loadConfiguration(file);
    }

    public static float[] getCoeff(String path) {
        FileConfiguration fc = load();
        float[] coeff = new float[4];

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
                coeff[0] = (float)fc.getDouble(path+".m");
                break;

            case "Taux.Somme":
                coeff[0] = (float)fc.getDouble(path+".aJoueurs");
                coeff[1] = (float)fc.getDouble(path+".aVariation");
                coeff[2] = (float)fc.getDouble(path+".aMort");
                coeff[3] = (float)fc.getDouble(path+".aDiamonds");
                break;

            case "Taux.Pente":
                coeff[0] = (float)fc.getDouble(path+".a");
                coeff[1] = (float)fc.getDouble(path+".r");
                break;
        }

        return coeff;
    }

}
