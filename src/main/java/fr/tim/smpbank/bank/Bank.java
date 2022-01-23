package fr.tim.smpbank.bank;

import fr.tim.smpbank.bank.rank.BankRank;
import fr.tim.smpbank.files.FileManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.*;

public class Bank implements Serializable {
    public transient static HashMap<UUID,Bank> bankList = new HashMap<>();

    private final String uuid;
    private float solde;
    private BankRank rank;
    private List<BankLog> bankLogList = new ArrayList<>();

    public Bank(Player player) {
        this(player.getUniqueId().toString());
    }

    public Bank(String uuid) {
        this.solde = 0;
        this.uuid = uuid;
        this.rank = BankRank.GREEN;
        loadData();
        logBankState();

        if (!bankList.containsKey(uuid)) bankList.put(UUID.fromString(uuid),this);
    }

    private void saveData() {
        try {
            File file = new File(FileManager.BANK_PATH + this.uuid + ".bank");

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
        File file = new File(FileManager.BANK_PATH + this.uuid + ".bank");

        if (!file.exists()) {
            saveData();
            return;
        }

        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
            Bank bank = (Bank) ois.readObject();
            ois.close();

            this.bankLogList = bank.getBankLogList();
            this.solde = bank.getSolde();
            this.rank = bank.getRank();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public BankRank getRank() {
        return this.rank;
    }

    public void setRank(BankRank rank) {
        this.rank = rank;
    }

    public void logBankState() {
        bankLogList.add(new BankLog(this.solde));
        saveData();
    }

    public List<BankLog> getBankLogList() {
        return this.bankLogList;
    }


    public float getSolde() {
        return this.solde;
    }

    public String getUuid() {
        return this.uuid;
    }

    public void setSolde(float newSolde) {
        this.solde = Math.round(newSolde*1000.0f)/1000.0f;
    }

    public void add(float n) {
        this.solde += Math.round(n*1000f)/1000f;
    }

}
