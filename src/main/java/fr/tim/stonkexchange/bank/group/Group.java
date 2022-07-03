package fr.tim.stonkexchange.bank.group;

import com.google.gson.Gson;
import fr.tim.stonkexchange.bank.Bank;
import fr.tim.stonkexchange.bank.BankLog;
import fr.tim.stonkexchange.files.FileManager;
import fr.tim.stonkexchange.gui.bank.VisualItems;
import fr.tim.stonkexchange.gui.pda.GestionPDA;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
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
    private transient ItemStack emblem;

    private String name;
    private float solde;
    private UUID owner;
    private HashMap<UUID,String> members = new HashMap<>();
    private List<BankLog> bankLogList = new ArrayList<>();


    public Group(String name, Player owner) {
        if (incList.containsKey(name) && (owner != null)) {
            owner.sendMessage(GestionPDA.PDAText + ChatColor.RED + " [ERROR] Ce nom est déjà pris.");
            return;
        }

        solde = 0;
        this.name = name;
        this.owner = (owner != null) ? owner.getUniqueId() : null;
        members.put(this.owner,(owner != null) ? owner.getName() : null);
        emblem = VisualItems.EMBLEM.getItem();


        loadData();
        logBankState();

        if (!incList.containsKey(name)) incList.put(name,this);
    }

    private void saveData() {
        try {
            File file = new File(FileManager.INC_PATH + name + ".bank");
            file.createNewFile();

            Gson gson = new Gson();
            Writer writer = new FileWriter(file);
            gson.toJson(this,writer);
            writer.flush();
            writer.close();

            file = new File(FileManager.INC_PATH + name + "-EMBLEM.yml");
            file.createNewFile();

            FileConfiguration conf = new YamlConfiguration();
            conf.load(file);

            conf.set("Item",emblem);

            conf.save(file);

        } catch (IOException | InvalidConfigurationException e) {
            System.out.println("An error has occurred saving " + name + "'s group account. Current Amount : " + solde);
            e.printStackTrace();
        }
    }

    public void deleteData() {
        File file = new File(FileManager.INC_PATH + name + ".bank");
        file.delete();
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

            file = new File(FileManager.INC_PATH + name + "-EMBLEM.yml");
            if (file.exists()) {
                FileConfiguration conf = new YamlConfiguration();
                conf.load(file);
                emblem = conf.getItemStack("Item");
            }

        } catch (IOException | InvalidConfigurationException e) {
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
        if (p.getUniqueId().equals(owner)) return;
        members.remove(p.getUniqueId());
    }

    public void removeMember(UUID uuid) {
        if (uuid.equals(owner)) return;
        members.remove(uuid);
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

    public static List<Group> getByPlayer(Player p) {
        List<Group> groups = new ArrayList<>();
        for (Group g : incList.values()) {
            if (g.getMembers().containsKey(p.getUniqueId())) groups.add(g);
        }
        return groups;
    }

    public int getMax() {
        int max = 0;
        for (UUID uuid : members.keySet()) {
            max += Bank.bankList.get(uuid).getRank().getMaxStorage();
        }
        return (int) Math.round(max*1.1);
    }

    public static void loadGroups() {
        File folder = new File(FileManager.INC_PATH);

        for (File file : folder.listFiles()) {
            if (!file.getName().contains(".bank")) continue;
            String name = file.getName().replace(".bank","");

            new Group(name,null);
        }
    }

    public void setName(String name) {
        this.name = name;
    }

}
