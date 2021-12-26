package fr.tim.smpbank.files;

import fr.tim.smpbank.smpBank;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class CoeffConfig {

    private static FileConfiguration load() {
        File file = new File(FileManager.BANK_PATH + "config.yml");
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

            case "Somme":
                coeff[0] = (float)fc.getDouble(path+"aJoueurs");
                coeff[1] = (float)fc.getDouble(path+"aVariation");
                coeff[2] = (float)fc.getDouble(path+"aMort");
                coeff[3] = (float)fc.getDouble(path+"aDiamonds");
                break;

            case "Pente":
                coeff[0] = (float)fc.getDouble(path+"a");
                coeff[1] = (float)fc.getDouble(path+"K");
                coeff[2] = (float)fc.getDouble(path+"r");
                break;
        }

        return coeff;
    }

}
