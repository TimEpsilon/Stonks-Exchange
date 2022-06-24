package fr.tim.stonkexchange.bank.corporate;

import com.google.gson.Gson;
import fr.tim.stonkexchange.bank.BankLog;
import fr.tim.stonkexchange.bank.rank.BankRank;
import fr.tim.stonkexchange.files.FileManager;
import fr.tim.stonkexchange.gui.pda.GestionPDA;
import fr.tim.stonkexchange.items.CustomItems;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Corporation implements Serializable {
    public transient static HashMap<String, Corporation> incList = new HashMap<>();

    private String name;
    private float solde;
    private UUID owner;
    private List<UUID> members;
    private List<BankLog> bankLogList = new ArrayList<>();

    public Corporation(String name, Player owner) {
        if (incList.containsKey(name)) {
            owner.sendMessage(GestionPDA.PDAText + ChatColor.RED + " [ERROR] Ce nom est déjà pris.");
            return;
        }

        solde = 0;
        this.name = name;
        this.owner = owner.getUniqueId();
        members.add(this.owner);

        loadData();
        logBankState();

        if (!incList.containsKey(name)) incList.put(name,this);
    }

    private void saveData() {
        try {
            File file = new File(FileManager.INC_PATH + name + ".bank");

            if (!file.exists()) {
                file.createNewFile();
            }

            Gson gson = new Gson();
            Writer writer = new FileWriter(file);
            gson.toJson(this,writer);
            writer.flush();
            writer.close();

        } catch (IOException e) {
            System.out.println("An error has occurred saving " + name + "'s corporation account. Current Amount : " + solde);
            e.printStackTrace();
        }
    }

    public void loadData() {
        try {
            File file = new File(FileManager.INC_PATH + name + ".bank");

            if (!file.exists()) {
                saveData();
                return;
            }
            Gson gson = new Gson();
            Reader reader = new FileReader(file);
            Corporation inc = gson.fromJson(reader, Corporation.class);

            bankLogList = inc.getBankLogList();
            solde = inc.getSolde();
            name = inc.getName();

        } catch (IOException e) {
            System.out.println("An error has occurred loading " + name + "'s corporation account.");
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public List<UUID> getMembers() {
        return members;
    }

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(@NotNull Player p) {
        owner = p.getUniqueId();
    }

    public void addMember(@NotNull Player p) {
        members.add(p.getUniqueId());
    }

    public void removeMember(Player p) {
        if (p.getUniqueId().equals(owner)) {
            p.sendMessage(GestionPDA.PDAText + ChatColor.RED + "[ERROR] Impossible de retirer le propriétaire.");
            return;
        }
        members.remove(p.getUniqueId());
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

    public void setSolde(float newSolde) {
        solde = newSolde;
        roundSolde();
    }

    private void roundSolde() {
        solde = Math.round(solde*1000f)/1000f;
    }

    public void soldeAdd(float n) {
        solde += n;
        roundSolde();
    }

}
