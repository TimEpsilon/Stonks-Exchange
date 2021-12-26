package fr.tim.smpbank.bank;

import fr.tim.smpbank.files.Autosave;
import fr.tim.smpbank.files.CoeffConfig;
import fr.tim.smpbank.smpBank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;

public abstract class CalculTaux {


    private static float variation(double x) {
        float[] coeff = CoeffConfig.getCoeff("Taux.variation");
        float m = coeff[0];
        float M = coeff[1];

        if (x>0) {
            return (float) (2/(1+Math.pow(3,-x/M))-1);
        }
        else {
            return (float) (-2/(1+Math.pow(3,-x/m))+1);
        }
    }

    private static float joueurs(float x) {
        float[] coeff = CoeffConfig.getCoeff("Taux.joueurs");
        float m = coeff[0];
        float a = coeff[1];

        return (float) (1/a * ((1+a)/(1+a*Math.pow(a+2,-x/m))-1));
    }

    private static float morts(double x) {
        float[] coeff = CoeffConfig.getCoeff("Taux.mort");
        float m = coeff[0];

        return (float) (Math.pow(2,-x/m)-1);
    }

    private static float diamonds(float x) {
        float[] coeff = CoeffConfig.getCoeff("Taux.diamonds");

        float m = coeff[0];

        return (float) (Math.pow(2,-x/m)-1);
    }

    public static float somme(float joueurs,float morts, float diamond, float variation) {
        float[] coeff = CoeffConfig.getCoeff("Somme");
        float aJoueurs = coeff[0];
        float aVariation = coeff[1];
        float aMort = coeff[2];
        float aDiamonds = coeff[3];

        return aJoueurs*joueurs(joueurs) + aVariation*variation(variation) + aDiamonds*diamonds(diamond) + aMort*morts(morts);

    }

    public static float pente(float somme) {
        float[] coeff = CoeffConfig.getCoeff("Pente");
        float a = coeff[0];
        float K = coeff[1];
        float r = coeff[2];

        double exp = Math.exp(-r * Math.abs(somme));
        float p = (float) (K/(1+a* exp));
        float q = (float) (K-K/(1+1/a* exp));

        float tirage = (float) Math.random();

        if (tirage<=p) {
            return p;
        } else if (tirage > p + q) {
            return 0;
        } else {
            return -q;
        }
    }

    public static int temps(float pente) {
        return (int) Math.min(4,Math.round(1/Math.sqrt(Math.abs(pente))));
    }
}