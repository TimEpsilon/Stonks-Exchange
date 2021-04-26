package fr.tim.smpbank.files;

import fr.tim.smpbank.smpBank;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class CoeffConfig {
    private static FileConfiguration load() {
        File file = new File(smpBank.getPlugin().getDataFolder(),"config.yml");
        return YamlConfiguration.loadConfiguration(file);
    }

    public static float[] getCoeff(String path) {
        FileConfiguration fc = load();
        float[] coeff = new float[6];

        switch (path) {

            case "Taux.variation":
                coeff[0]= (float)fc.getDouble("Taux.variation.m");
                coeff[1] = (float)fc.getDouble("Taux.variation.M");
                break;

            case "Taux.joueurs":
                coeff[0] = (float)fc.getDouble("Taux.joueurs.eps");
                coeff[1] = (float)fc.getDouble("Taux.joueurs.M");
                break;

            case "Taux.total":
                coeff[0] = (float)fc.getDouble("Taux.total.eps");
                coeff[1] = (float)fc.getDouble("Taux.total.m");
                break;

            case "Taux.taux":
                coeff[0] = (float)fc.getDouble("Taux.taux.dm");
                coeff[1] = (float)fc.getDouble("Taux.taux.eps");
                break;

            case "Taux.admin":
                coeff[0] = (float)fc.getDouble("Taux.joueurs.eps");
                break;

            case "Taux.moyenne":
                coeff[0] = (float)fc.getDouble("Taux.moyenne.aAdmin");
                coeff[1] = (float)fc.getDouble("Taux.moyenne.aTaux");
                coeff[2] = (float)fc.getDouble("Taux.moyenne.aJoueurs");
                coeff[3] = (float)fc.getDouble("Taux.moyenne.aTotal");
                coeff[4] = (float)fc.getDouble("Taux.moyenne.aVariation");
                coeff[5] = (float)fc.getDouble("Taux.moyenne.aMort");
                break;

            case "Taux.ecart":
                coeff[0] = (float)fc.getDouble("Taux.ecart.aEcart");
                coeff[1] = (float)fc.getDouble("Taux.ecart.bEcart");
                break;

            case "Taux.mort":
                coeff[0] = (float)fc.getDouble("Taux.mort.m");
                coeff[1] = (float)fc.getDouble("Taux.mort.eps");
                break;
        }

        return coeff;
    }

}
