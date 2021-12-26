package fr.tim.smpbank.bank;

import fr.tim.smpbank.files.FileManager;
import fr.tim.smpbank.listeners.GetTauxParametres;
import fr.tim.smpbank.smpBank;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Taux implements Serializable {
    private float taux;
    private List<BankLog> tauxLog = new ArrayList<>();

    private static float moyenne = 5;
    private static float ecart = 0.3f;
    private static float ecartMax = 2;


    public Taux() {
        this.taux = 5;
        loadData();
        logTaux(System.currentTimeMillis());
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

            for (int i = taux.tauxLog.size(); i > -1; i--) {
                if (System.currentTimeMillis() > taux.tauxLog.get(i).getTime()) this.taux = taux.tauxLog.get(i).getSolde();
            }
            this.tauxLog = taux.tauxLog;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
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
                - GetTauxParametres.getTotalBefore(System.currentTimeMillis()- 86400000);

        float somme = CalculTaux.somme(j,m,d,v);
        float pente = CalculTaux.pente(somme);
        int dt = CalculTaux.temps(pente);

        for (int i = 0; i<dt; i++) {
            this.taux = (float) (this.taux + pente + (moyenne - this.taux) * 0.1f + Math.random()*2*ecart - ecart);

            if (this.taux > moyenne + ecartMax) this.taux = 2*moyenne + 2*ecartMax - this.taux;
            if (this.taux < moyenne - ecartMax) this.taux = 2*moyenne - 2*ecartMax - this.taux;

            logTaux(System.currentTimeMillis()+ i* 86400000L);
        }

        GetTauxParametres.resetParameters();

        loadData();

    }

    public void dailyUpdate() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(smpBank.getPlugin(),() -> {
            nextTaux();
            Bukkit.broadcast(Component.text(ChatColor.AQUA + "Nouveau Taux"));
        },0,1728000);
    }

}
