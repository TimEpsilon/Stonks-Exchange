package fr.tim.smpbank.bank;

import java.util.Random;

public class Taux {
    /**Taux
     * Effectue tous les calculs du taux du jour.
     * Les valeurs des constantes peuvent être modifiées dans le plugin.yml (à terme).
     * C'est globalement une combinaison linéaire de 5 fonctions qui permettent de normaliser chaque variable sur un intervalle pertinent.
     * Toutes les variables interviennent dans la moyenne d'une loi normale qui sert à la décaler autour de 0 pour faire varier le taux.
     * L'écart type ne dépend que du taux précédent, pour éviter des valeurs aléatoires trop grandes en cas d'écart trop important à 5.
     * C'est une purge absolue pour simuler une situation réelle donc je sens bien que les premiers jours ça va être du tweaking massif.
     */

    public static float Tauxdx(float dx) {
        float m = -6000f;
        float M = 3000f;

        if (dx>0) {
            return (float)Math.pow(dx/M,1/3f);
        }
        else {
            return (float)Math.pow(-dx/m,1/3f);
        }
    }

    public static float Tauxjn(float jn) {
        float M = 12;

        float a = 6/M;
        float b = -6;

        return (float)Math.exp((double)a*jn+b);
    }

    public static float Tauxx(float x) {
        float a = (float)(Math.pow(10, -2)-Math.pow(10, -5));
        float b = 1f;

        return 1/(a*x+b);
    }

    public static float TauxTn(float Tn) {
        double s = -16/Math.log(0.3);

        if (Tn<=5) {
            return 1- (float)Math.exp(Math.pow(Tn-5,5)/(2*s));
        }
        else {
            return (float)Math.exp(-Math.pow(Tn-5,5)/(2*s)) -1;
        }
    }


    public static float newTaux(float tau,float Tn,float jn, float x, float dx) {
        float aTau  =0.2f;
        float aTn =0.155f;
        float aJn =0.1f;
        float aX =0.04f;
        float aDx =0.005f;

        float moyenne = (aTau*tau + aTn*TauxTn(Tn) + aJn*Tauxjn(jn) + aX*Tauxx(x) + aDx*Tauxdx(dx));
        float sigma = -0.09f*Math.abs(TauxTn(Tn))+0.1f;

        Random r = new Random();
        float f = (float)r.nextGaussian()*sigma+moyenne;

        return Tn+f;

    }
}
