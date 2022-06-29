package fr.tim.stonkexchange.commands;

import fr.tim.stonkexchange.bank.Bank;
import fr.tim.stonkexchange.bank.group.Group;
import fr.tim.stonkexchange.gui.pda.GestionPDA;
import fr.tim.stonkexchange.items.CustomItems;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CreateGroup implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player p)) return false;
        if (args.length == 0) return false;

        switch (args[0]) {
            case "create" -> {
                if (args.length != 2) return false;
                String name = args[1];

                List<Group> groups = Group.getByPlayer(p);
                boolean isInGroup = false;

                for (Group group : groups) {
                    if (group.getOwner().equals(p.getUniqueId())) {
                        isInGroup = true;
                        break;
                    }
                }

                if (isInGroup) {
                    p.sendMessage(GestionPDA.PDAText + ChatColor.RED + "Vous êtes déjà chef de groupe");
                    return true;
                }

                for (Group group : Group.incList.values()) {
                    if (group.getName().equals(name)) {
                        p.sendMessage(GestionPDA.PDAText + ChatColor.RED + "Ce nom existe déjà");
                        return true;
                    }
                }

                new Group(name,p);
                p.sendMessage(GestionPDA.PDAText + ChatColor.GREEN + "Votre groupe " + name + " a bien été créé");
                return true;
            }

            case "delete" -> {
                List<Group> groups = Group.getByPlayer(p);
                if (args.length != 2) return false;
                String name = args[1];

                if (groups.size() == 0) {
                    p.sendMessage(GestionPDA.PDAText + ChatColor.RED + "Vous n'appartenez pas à un groupe");
                    return true;
                }

                for (Group group : groups) {
                    if (!group.getOwner().equals(p.getUniqueId())) {
                        p.sendMessage(GestionPDA.PDAText + ChatColor.RED + "Vous n'êtes pas le chef de ce groupe");
                        return true;
                    }

                    giveRemainingToOwner(group,p);
                    group.deleteData();
                    Group.incList.remove(group.getName());
                    group.setName(null);
                    group.setSolde(0);
                    p.sendMessage(GestionPDA.PDAText + ChatColor.GREEN + "Votre groupe " + name + " a bien été supprimé");
                    return true;
                }


            }

            case "list" -> {
                p.sendMessage(ChatColor.GOLD + "--------------- Groupes ---------------");
                for (Group group : Group.incList.values()) {
                    p.sendMessage(ChatColor.GOLD + "● " + group.getName());
                    for (String name : group.getMembers().values()) {
                        p.sendMessage(ChatColor.YELLOW + "     - " + name);
                    }
                }
            }
            case "modify" -> {
                List<Group> groups  = Group.getByPlayer(p);
                if (groups.size() == 0) {
                    p.sendMessage(GestionPDA.PDAText + ChatColor.RED + "Vous n'appartenez pas à un groupe");
                    return true;
                }

                Group group = null;
                for (Group temp : groups) {
                    if (temp.getOwner().equals(p.getUniqueId())) {
                        group = temp;
                        break;
                    }
                }
                if (group == null) {
                    p.sendMessage(GestionPDA.PDAText + ChatColor.RED + "Vous n'êtes pas le chef de ce groupe");
                    return true;
                }

                if (args.length < 2) return true;
                String modify = args[1];

                switch (modify) {
                    case "name" -> {
                        if (args.length != 3) {
                            p.sendMessage(GestionPDA.PDAText + ChatColor.RED + "Merci d'entrer un nom valide");
                            return true;
                        }
                        String name = args[2];
                        p.sendMessage(GestionPDA.PDAText + ChatColor.GREEN + group.getName() + " a bien été renommé en " + name);
                        group.setName(name);
                    }

                    case "emblem" -> {
                        ItemStack emblem = p.getInventory().getItemInMainHand();
                        if (emblem.getType().equals(Material.AIR)) {
                            p.sendMessage(GestionPDA.PDAText + ChatColor.RED + "Merci de tenir un item en main");
                            return true;
                        }
                        group.setEmblem(emblem);
                        p.sendMessage(GestionPDA.PDAText + ChatColor.GREEN + "Emblème mis à jour");
                    }

                    case "add" -> {
                        if (args.length != 3) {
                            p.sendMessage(GestionPDA.PDAText + ChatColor.RED + "Merci d'entrer un pseudo valide");
                            return true;
                        }
                        String pseudo = args[2];

                        Player player = null;

                        for (Player temp : Bukkit.getOnlinePlayers()) {
                            if (temp.getName().equals(pseudo)) {
                                temp.sendMessage(GestionPDA.PDAText + ChatColor.GREEN + "Vous avez été ajouté au groupe " + group.getName());
                                player = temp;
                                break;
                            }
                        }

                        if (player == null) {
                            p.sendMessage(GestionPDA.PDAText + ChatColor.RED + "Merci d'entrer un pseudo valide");
                            return true;
                        }

                        p.sendMessage(GestionPDA.PDAText + ChatColor.GREEN + pseudo + " a bien été ajouté à " + group.getName());
                        group.addMember(player);
                    }

                    case "owner" -> {
                        if (args.length < 3) {
                            p.sendMessage(GestionPDA.PDAText + ChatColor.RED + "Merci d'entrer un pseudo valide");
                            return true;
                        }

                        String pseudo = args[2];

                        if (args.length != 4 || !args[3].equals("confirm")) {
                            p.sendMessage(GestionPDA.PDAText + ChatColor.YELLOW + "Vous êtes sur le point de transmettre le titre de chef du groupe à " + pseudo + ". Pour confirmer : " + ChatColor.WHITE + "/group modify owner <Pseudo> confirm");
                            return true;
                        }

                        Player player = null;

                        for (Player temp : Bukkit.getOnlinePlayers()) {
                            if (temp.getName().equals(pseudo)) {
                                temp.sendMessage(GestionPDA.PDAText + ChatColor.GREEN + "Vous êtes à présent chef du groupe " + group.getName());
                                player = temp;
                                break;
                            }
                        }

                        if (player == null) {
                            p.sendMessage(GestionPDA.PDAText + ChatColor.RED + "Merci d'entrer un pseudo valide");
                            return true;
                        }

                        p.sendMessage(GestionPDA.PDAText + ChatColor.GREEN + pseudo + " a bien été promu chef de " + group.getName());
                        group.setOwner(player);
                    }

                    case "remove" -> {
                        if (args.length != 3) {
                            p.sendMessage(GestionPDA.PDAText + ChatColor.RED + "Merci d'entrer un pseudo valide");
                            return true;
                        }
                        String pseudo = args[2];

                        for (Map.Entry<UUID,String> entry : group.getMembers().entrySet()) {
                            if (entry.getValue().equals(pseudo)) {
                                if (entry.getKey().equals(group.getOwner())) {
                                    p.sendMessage(GestionPDA.PDAText + ChatColor.RED + "Le chef de groupe ne peut pas être supprimé");
                                    return true;
                                }
                                group.removeMember(entry.getKey());
                                p.sendMessage(GestionPDA.PDAText + ChatColor.GREEN + pseudo + " a bien été retiré de " + group.getName());
                                return true;
                            }
                        }
                        p.sendMessage(GestionPDA.PDAText + ChatColor.RED + "Merci d'entrer un pseudo valide");
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void giveRemainingToOwner(Group group, Player p) {
        float solde = group.getSolde();
        Bank b = Bank.bankList.get(p.getUniqueId());

        b.add(solde);
        float reste = b.getSolde() - b.getRank().getMaxStorage();

        if (reste <= 0) return;

        ItemStack mcoin = CustomItems.MCOIN.getItem();
        mcoin.setAmount((int) Math.floor(reste));
        HashMap<Integer,ItemStack> drops =  p.getInventory().addItem(mcoin);

        for (ItemStack drop : drops.values()) {
            p.getWorld().dropItem(p.getLocation(),drop);
        }

    }
}
