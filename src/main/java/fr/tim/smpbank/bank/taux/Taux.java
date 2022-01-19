package fr.tim.smpbank.bank.taux;

import fr.tim.smpbank.StonksExchange;
import fr.tim.smpbank.bank.BankLog;
import fr.tim.smpbank.files.FileManager;
import fr.tim.smpbank.gui.pda.GestionPDA;
import fr.tim.smpbank.listeners.taux.GetTauxParametres;
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
    private transient static float ecartRandom = 0.15f;
    private transient static float ecartMax = 2;
    public transient static final long time = 300; //21300; //5h55min
    private transient static float retour =0.2f;


    public Taux() {
        this.taux = 5;
        this.pente = 0;
        loadData();
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

            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(this);
            oos.close();

        } catch (IOException e) {
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

            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
            Taux taux = (Taux) ois.readObject();
            ois.close();

            this.tauxLog = taux.getTauxLog();
            this.taux = taux.getTaux();
            this.pente = taux.getPente();
            this.penteLog = taux.getPenteLog();
        } catch (IOException | ClassNotFoundException e) {
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

        for (int i : GetTauxParametres.DeadList.values()) {
            m += i;
        }

        for (int i : GetTauxParametres.DiamondList.values()) {
            d += i;
        }

        v = GetTauxParametres.getTotalNow()
                - GetTauxParametres.getTotalBefore(System.currentTimeMillis()- time*1000);

        Bukkit.broadcastMessage("variation " + v);

        float somme = CalculTaux.somme(j,m,d,v);
        this.pente = CalculTaux.pente(somme);

        this.taux = (float) (this.taux + this.pente + (moyenne - this.taux) * retour + Math.random()*2*ecartRandom - ecartRandom + tauxProactif());

        if (this.taux > moyenne + ecartMax) this.taux = 2*moyenne + 2*ecartMax - this.taux;
        if (this.taux < moyenne - ecartMax) this.taux = 2*moyenne - 2*ecartMax - this.taux;

        this.taux = Math.round(this.taux*1000f)/1000f;

        GetTauxParametres.resetParameters();

    }

    private float tauxProactif() {
        float penteResiduelle = 0;
        for (int i =-1; i>-Math.min(10,this.getPenteLog().size()); i--) {
            penteResiduelle += this.getPenteLog().get(this.getPenteLog().size() - 1 + i).getSolde() / Math.pow(i,2);
        }
        return penteResiduelle;
    }

    public void dailyUpdate() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(StonksExchange.getPlugin(),() -> {
            nextTaux();
            Bukkit.broadcast(Component.text(GestionPDA.PDAText + ChatColor.AQUA + "Nouveau Taux : " + ChatColor.GOLD + this.taux));
            saveData();

        },0,time*20);
    }

}
