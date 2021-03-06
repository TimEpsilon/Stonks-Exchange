package fr.tim.stonkexchange.bank;

import com.google.gson.Gson;
import fr.tim.stonkexchange.bank.rank.BankRank;
import fr.tim.stonkexchange.files.FileManager;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.*;

public class Bank implements Serializable {
    public transient static HashMap<UUID,Bank> bankList = new HashMap<>();

    private final String uuid;
    private String name;
    private float solde;
    private BankRank rank;
    private List<BankLog> bankLogList = new ArrayList<>();

    public Bank(Player player) {
        this(player.getUniqueId().toString(),player.getName());
    }

    public Bank(String uuid,String name) {
        this.solde = 0;
        this.uuid = uuid;
        this.rank = BankRank.GREEN;
        this.name = name;
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

            Gson gson = new Gson();
            Writer writer = new FileWriter(file);
            gson.toJson(this,writer);
            writer.flush();
            writer.close();

        } catch (IOException e) {
            System.out.println("An error has occurred saving " + uuid + "'s bank account. Current Amount : " + solde);
            e.printStackTrace();
        }
    }

    public void loadData() {
        try {
            File file = new File(FileManager.BANK_PATH + this.uuid + ".bank");

            if (!file.exists()) {
                saveData();
                return;
            }
            Gson gson = new Gson();
            Reader reader = new FileReader(file);
            Bank bank = gson.fromJson(reader,Bank.class);

            this.name = bank.getName();
            this.bankLogList = bank.getBankLogList();
            this.solde = bank.getSolde();
            this.rank = bank.getRank();
        } catch (IOException e) {
            System.out.println("An error has occurred loading " + uuid + "'s bank account.");
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
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
        this.solde =newSolde;
        roundSolde();
    }

    private void roundSolde() {
        this.solde = Math.round(this.solde*1000f)/1000f;
    }

    public void add(float n) {
        this.solde += n;
        roundSolde();
    }

    public static void loadBanks() {
        File folder = new File(FileManager.BANK_PATH);

        for (File file : folder.listFiles()) {
            if (file.getName().contains("Taux")) continue;
            String uuid = file.getName().replace(".bank","");

            new Bank(uuid,uuid);
        }
    }

}
