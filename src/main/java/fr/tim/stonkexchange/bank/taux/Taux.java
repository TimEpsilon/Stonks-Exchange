package fr.tim.stonkexchange.bank.taux;

import com.google.gson.Gson;
import fr.tim.stonkexchange.StonkExchange;
import fr.tim.stonkexchange.bank.BankLog;
import fr.tim.stonkexchange.files.ConfigManager;
import fr.tim.stonkexchange.files.FileManager;
import fr.tim.stonkexchange.gui.pda.GestionPDA;
import fr.tim.stonkexchange.listeners.taux.GetTauxParametres;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Taux implements Serializable {
    private float taux;
    private float pente;
    private List<BankLog> tauxLog = new ArrayList<>();
    private List<BankLog> penteLog = new ArrayList<>();

    private transient static float moyenne = 5;
    private transient static float ecartRandom = 0.1f;
    private transient static float ecartMax = 2;
    public transient static final long time = ConfigManager.getTimer(); //7h55min


    public Taux() {
        this.taux = 5;
        this.pente = 0;
    }

    public float getTaux() {
        return taux;
    }

    private void logTaux(long time) {
        penteLog.add(new BankLog(this.pente,time));
        tauxLog.add(new BankLog(this.taux,time));
    }

    public void saveData() {
        try {
            File file = new File(FileManager.BANK_PATH  + "Taux.bank");

            if (!file.exists()) {
                file.createNewFile();
            }

            logTaux(System.currentTimeMillis());

            Gson gson = new Gson();
            Writer writer = new FileWriter(file);
            gson.toJson(this,writer);
            writer.flush();
            writer.close();

        } catch (IOException e) {
            System.out.println("An error has occurred saving Taux. Current Amount : " + taux);
            e.printStackTrace();
        }
    }

    public void loadData() {
        File file = new File(FileManager.BANK_PATH + "Taux.bank");

        try {

            if (!file.exists()) {
                saveData();
                return;
            }

            Gson gson = new Gson();
            Reader reader = new FileReader(file);
            Taux taux = gson.fromJson(reader,Taux.class);

            tauxLog = taux.getTauxLog();
            this.taux = taux.getTaux();
            pente = taux.getPente();
            penteLog = taux.getPenteLog();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<BankLog> getPenteLog() {
        return this.penteLog;
    }

    private float getPente() {
        return this.pente;
    }

    public List<BankLog> getTauxLog() {
        return this.tauxLog;
    }

    public void nextTaux() {

        float j = GetTauxParametres.JoinedList.size();
        float m = 0;
        float d = 0;
        float v = 0;
        float b = 0;
        float a = 0;
        float o = 0;

        for (int i : GetTauxParametres.DeadList.values()) {
            m += i;
        }

        for (int i : GetTauxParametres.DiamondList.values()) {
            d += i;
        }

        v = GetTauxParametres.getTotalNow()
                - GetTauxParametres.getTotalBefore(System.currentTimeMillis()- time*1000 + 10000);

        for (int i : GetTauxParametres.BossCount.values()) {
            b += i;
        }

        for (int i : GetTauxParametres.AdvancementCount.values()) {
            a += i;
        }

        for (int i : GetTauxParametres.OreCount.values()) {
            o += i;
        }

        float somme = CalculTaux.somme(j,m,d,v,b,a,o);
        pente = CalculTaux.pente(somme);

        taux = (float) (taux + pente + Math.random()*2*ecartRandom - ecartRandom + residus());

        if (taux > moyenne + ecartMax) taux = 2*moyenne + 2*ecartMax - taux;
        if (taux < moyenne - ecartMax) taux = 2*moyenne - 2*ecartMax - taux;

        taux += CalculTaux.retour(taux);

        taux = Math.round(taux*1000f)/1000f;

        GetTauxParametres.resetParameters();

    }

    private float residus() {
        float penteResiduelle = 0;
        for (int i =-1; i>-Math.min(10,this.getPenteLog().size()); i--) {
            penteResiduelle += this.getPenteLog().get(this.getPenteLog().size() - 1 + i).getSolde() / -Math.pow(i+0.05,2);
        }
        return penteResiduelle;
    }

    public void dailyUpdate() {
        Bukkit.getScheduler().runTaskTimer(StonkExchange.getPlugin(),() -> {
            sendInfoToDiscord();
            nextTaux();
            Bukkit.broadcast(Component.text(GestionPDA.PDAText + ChatColor.AQUA + "Nouveau Taux : " + ChatColor.GOLD + this.taux));
            saveData();

        },time*20,time*20);
    }

    private void sendInfoToDiscord() {

        float[] coeff = ConfigManager.getCoeff("Taux.Somme");
        float aJoueurs = coeff[0];
        float aVariation = coeff[1];
        float aMort = coeff[2];
        float aDiamonds = coeff[3];
        float aBoss = coeff[4];
        float aAdvancement = coeff[5];
        float aOres = coeff[6];

        float j = GetTauxParametres.JoinedList.size();
        float m = 0;
        int d = 0;
        int a = 0;
        int b = 0;
        int o = 0;

        for (int i : GetTauxParametres.DeadList.values()) {
            m += i;
        }

        for (int i : GetTauxParametres.DiamondList.values()) {
            d += i;
        }

        for (int i : GetTauxParametres.AdvancementCount.values()) {
            a += i;
        }

        for (int i : GetTauxParametres.BossCount.values()) {
            b += i;
        }

        for (int i : GetTauxParametres.OreCount.values()) {
            o += i;
        }

        float v = GetTauxParametres.getTotalNow()
                - GetTauxParametres.getTotalBefore(System.currentTimeMillis()- time*1000 + 10000);

        DiscordLink.sendMessage("**Nouveau Taux** : " + taux + " " + DiscordLink.mcoinEmoji,
                "Joueurs Connectés : " + j + " - Influence : " + Math.round(aJoueurs*CalculTaux.joueurs(j)*1000f)/1000f,
                "Nombre de Morts : " + m + " - Influence : " + Math.round(aMort*CalculTaux.morts(m)*1000f)/1000f,
                "Diamants Minés : " + d + " - Influence : " + Math.round(aDiamonds*CalculTaux.diamonds(d)*1000f)/1000f,
                "Variation de la Banque : " + v + " - Influence : " + Math.round(aVariation*CalculTaux.variation(v)*1000f)/1000f,
                "Boss vaincus : " + b + " - Influence : " + Math.round(aBoss*CalculTaux.boss(b)*1000f)/1000f,
                "Advancements obtenus : " + a + " - Influence : " + Math.round(aAdvancement*CalculTaux.advancement(a)*1000f)/1000f,
                "Minerais Rares Minés : " + o + " - Influence : " + Math.round(aOres*CalculTaux.ores(o)*1000f)/1000f);
    }

}
