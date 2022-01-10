package fr.tim.smpbank.bank;

import fr.tim.smpbank.files.FileManager;
import fr.tim.smpbank.listeners.GetTauxParametres;
import fr.tim.smpbank.StonksExchange;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Taux implements Serializable {
    private float taux;
    private List<BankLog> tauxLog = new ArrayList<>();

    private transient static float moyenne = 5;
    private transient static float ecart = 0.05f;
    private transient static float ecartMax = 2;
    public transient static final long time = 60;
    private transient static float retour =0.1f;


    public Taux() {
        this.taux = 5;
        loadData();
    }

    public float getTaux() {
        return taux;
    }

    private void logTaux(long time) {
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
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
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

        v = GetTauxParametres.getTotalBefore(System.currentTimeMillis())
                - GetTauxParametres.getTotalBefore(System.currentTimeMillis()- time*1000);

        float somme = CalculTaux.somme(j,m,d,v);
        float pente = CalculTaux.pente(somme);

        Bukkit.broadcastMessage(pente + "");

        this.taux = (float) (this.taux + pente + (moyenne - this.taux) * retour + Math.random()*2*ecart - ecart);

        if (this.taux > moyenne + ecartMax) this.taux = 2*moyenne + 2*ecartMax - this.taux;
        if (this.taux < moyenne - ecartMax) this.taux = 2*moyenne - 2*ecartMax - this.taux;

        this.taux = Math.round(this.taux*1000f)/1000f;

        GetTauxParametres.resetParameters();

    }

    public void dailyUpdate() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(StonksExchange.getPlugin(),() -> {
            nextTaux();
            Bukkit.broadcast(Component.text(ChatColor.AQUA + "Nouveau Taux"));
            saveData();

        },time*20,time*20);
    }

}
