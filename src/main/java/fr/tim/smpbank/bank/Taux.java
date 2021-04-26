package fr.tim.smpbank.bank;

import fr.tim.smpbank.files.Autosave;
import fr.tim.smpbank.files.CoeffConfig;
import fr.tim.smpbank.smpBank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
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

    private static double Tauxdx(double dx) {
        float[] coeff = CoeffConfig.getCoeff("Taux.variation");
        float m = coeff[0];
        float M = coeff[1];

        if (dx>0) {
            return (float)Math.pow(dx/M,1/3f);
        }
        else {
            return (float)Math.pow(-dx/m,1/3f);
        }
    }

    private static float Tauxjn(float jn) {
        float[] coeff = CoeffConfig.getCoeff("Taux.joueurs");
        float M = coeff[1];
        float eps = coeff[0];

        float a = (float)(eps*Math.log(10)/M);
        float b = (float)(-eps*Math.log(10));

        return (float)Math.exp((double)a*jn+b);
    }

    private static double Tauxx(double x) {
        float[] coeff = CoeffConfig.getCoeff("Taux.total");
        float m = coeff[1];
        float eps = coeff[0];

        float a = (float)((Math.pow(10,eps)-1)/m);
        float b = 1f;

        return 1/(a*x+b);
    }

    private static float TauxTn(float Tn) {
        float[] coeff = CoeffConfig.getCoeff("Taux.taux");
        float dm = coeff[0];
        float eps = coeff[1];

        double s = Math.pow(dm,5)/(2*eps*Math.log(10));

        if (Tn<=5) {
            return 1- (float)Math.exp(Math.pow(Tn-5,5)/(2*s));
        }
        else {
            return (float)Math.exp(-Math.pow(Tn-5,5)/(2*s)) -1;
        }
    }

    private static float TauxM(float d) {
        float[] coeff = CoeffConfig.getCoeff("Taux.mort");
        float m = coeff[0];
        float eps = coeff[1];

        double s = Math.pow(m,2)/(2*eps*Math.log(10));

        return (float)Math.exp(-Math.pow(d,2)/(2*s))-1;
    }

    public static float newTaux(float tau,float Tn,float jn, double x, double dx, float d) {
        float[] coeff = CoeffConfig.getCoeff("Taux.moyenne");
        float aTau = coeff[0];
        float aTn = coeff[1];
        float aJn = coeff[2];
        float aX = coeff[3];
        float aDx = coeff[4];
        float aD = coeff[5];

        float[] coeffEcart = CoeffConfig.getCoeff("Taux.ecart");

        double moyenne = (aTau*tau + aTn*TauxTn(Tn) + aJn*Tauxjn(jn) + aX*Tauxx(x) + aDx*Tauxdx(dx) + aD*TauxM(d));
        float sigma = coeffEcart[0]*Math.abs(TauxTn(Tn))+coeffEcart[1];

        Random r = new Random();
        float f = Math.round(((float)r.nextGaussian()*sigma+(float)moyenne)*1000f)/1000f;

        return Math.round((Tn+f)*1000f)/1000f;

    }

    public static void dailyTaux() {
        smpBank.getPlugin().setTaux(5);

        long delay = LocalTime.now().until(LocalTime.parse("00:00:00"), ChronoUnit.SECONDS)*20;

        Bukkit.getScheduler().runTaskTimerAsynchronously(smpBank.getPlugin(), () -> {
            Bukkit.broadcastMessage(ChatColor.AQUA + "Nouveau taux!");
            float tau = CoeffConfig.getCoeff("Taux.total")[0];
            float Tn = Autosave.getTauxDate(1);
            int jn = smpBank.getPlugin().getJoined().size();
            double x = Autosave.getTotalSolde(1);
            double dx = x - Autosave.getTotalSolde(2);
            float d = smpBank.getPlugin().getDead().size();

            Bukkit.broadcastMessage("Tn = " + Tn);
            Bukkit.broadcastMessage("jn = " + jn);
            Bukkit.broadcastMessage("x = " + x);
            Bukkit.broadcastMessage("dx = " + dx);
            float taux = newTaux(tau,Tn,jn,x,dx,d);

            smpBank.getPlugin().setTaux(taux);

            smpBank.getPlugin().getJoined().clear();
            smpBank.getPlugin().getDead().clear();
        },delay,1728000);
    }
}
