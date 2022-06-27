package fr.tim.stonkexchange.bank.group;

import com.google.gson.Gson;
import fr.tim.stonkexchange.bank.Bank;
import fr.tim.stonkexchange.bank.BankLog;
import fr.tim.stonkexchange.files.FileManager;
import fr.tim.stonkexchange.gui.pda.GestionPDA;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Group implements Serializable {
    public transient static HashMap<String, Group> incList = new HashMap<>();

    private String name;
    private float solde;
    private UUID owner;
    private HashMap<UUID,String> members = new HashMap<>();
    private List<BankLog> bankLogList = new ArrayList<>();
    private ItemStack emblem;

    public Group(String name, Player owner) {
        if (incList.containsKey(name) && (owner != null)) {
            owner.sendMessage(GestionPDA.PDAText + ChatColor.RED + " [ERROR] Ce nom est déjà pris.");
            return;
        }

        solde = 0;
        this.name = name;
        this.owner = (owner != null) ? owner.getUniqueId() : null;
        members.put(this.owner,(owner != null) ? owner.getName() : null);

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
            System.out.println("An error has occurred saving " + name + "'s group account. Current Amount : " + solde);
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
            Group inc = gson.fromJson(reader, Group.class);

            bankLogList = inc.getBankLogList();
            solde = inc.getSolde();
            name = inc.getName();
            owner = inc.getOwner();
            members = inc.getMembers();
            emblem = inc.getEmblem();

        } catch (IOException e) {
            System.out.println("An error has occurred loading " + name + "'s group account.");
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public HashMap<UUID,String> getMembers() {
        return members;
    }

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(@NotNull Player p) {
        owner = p.getUniqueId();
    }

    public void addMember(@NotNull Player p) {
        members.put(p.getUniqueId(),p.getName());
    }

    public void setEmblem(ItemStack item) {
        emblem = item;
    }

    public ItemStack getEmblem() {
        return emblem;
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

    public static Group getByPlayer(Player p) {
        for (Group g : incList.values()) {
            if (g.getMembers().containsKey(p.getUniqueId())) return g;
        }
        return null;
    }

    public int getMax() {
        int max = 50;
        for (UUID uuid : members.keySet()) {
            max += Bank.bankList.get(uuid).getRank().getMaxStorage();
        }
        return max;
    }

    public static void loadGroups() {
        File folder = new File(FileManager.INC_PATH);

        for (File file : folder.listFiles()) {
            String name = file.getName().replace(".bank","");

            new Group(name,null);
        }
    }

}
