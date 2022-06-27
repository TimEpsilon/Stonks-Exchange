package fr.tim.stonkexchange.bank.taux;

import fr.tim.stonkexchange.files.ConfigManager;


public abstract class CalculTaux {


    public static float variation(double x) {
        float[] coeff = ConfigManager.getCoeff("Taux.variation");
        float m = coeff[0];
        float M = coeff[1];

        if (x>0) {
            return (float) (2/(1+Math.pow(3,-x/M))-1);
        }
        else {
            return (float) (-2/(1+Math.pow(3,-x/m))+1);
        }
    }

    public static float joueurs(float x) {
        float[] coeff = ConfigManager.getCoeff("Taux.joueurs");
        float m = coeff[0];
        float a = coeff[1];

        return (float) (1f/a * ((1+a)/(1+a*Math.pow(a+2,-x/m))-1));
    }

    public static float morts(double x) {
        float[] coeff = ConfigManager.getCoeff("Taux.mort");
        float m = coeff[0];

        return (float) (Math.pow(2,-x/m)-1);
    }

    public static float diamonds(float x) {
        float[] coeff = ConfigManager.getCoeff("Taux.diamonds");

        float m = coeff[0];

        return (float) (Math.pow(2,-x/m)-1);
    }

    public static float boss(float x) {
        float m = ConfigManager.getCoeff("Taux.boss")[0];

        return (float) (1-Math.pow(2,-x/m));
    }

    public static float advancement(float x) {
        float m = ConfigManager.getCoeff("Taux.advancement")[0];

        return (float) (2/(1+Math.pow(3,-x/m)) - 1);
    }

    public static float ores(float x) {
        float m = ConfigManager.getCoeff("Taux.ores")[0];

        return (float) (1-Math.pow(2,-x/m));
    }

    public static float somme(float joueurs,float morts, float diamond, float variation,float boss, float advancement, float ores) {
        float[] coeff = ConfigManager.getCoeff("Taux.Somme");
        float aJoueurs = coeff[0];
        float aVariation = coeff[1];
        float aMort = coeff[2];
        float aDiamonds = coeff[3];
        float aBoss = coeff[4];
        float aAdvancement = coeff[5];
        float aOres = coeff[6];

/*        Bukkit.broadcastMessage("J : " + aJoueurs + " * " + joueurs);
        Bukkit.broadcastMessage("M : " + aMort + " * " + morts);
        Bukkit.broadcastMessage("D : " + aDiamonds + " * " + diamond);
        Bukkit.broadcastMessage("V : " + aVariation + " * " + variation);*/

        return aJoueurs*joueurs(joueurs) + aVariation*variation(variation) + aDiamonds*diamonds(diamond) + aMort*morts(morts) + aBoss*boss(boss) + aAdvancement*advancement(advancement) + aOres*ores(ores);

    }

    public static float pente(float somme) {
        float[] coeff = ConfigManager.getCoeff("Taux.Pente");
        float a = coeff[0];
        float r = coeff[1];

        double exp = Math.exp(-r * somme);

        return (float) (a/(1+exp)-a/2);
    }

    public static float retour(float taux) {
        float r = ConfigManager.getCoeff("Taux.Retour")[0];

        return (float) (1f / (1+Math.exp(-r*(taux - 5))) - 0.5f);
    }
}
