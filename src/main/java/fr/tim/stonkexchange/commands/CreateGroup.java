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

import java.util.*;
import java.util.stream.Collectors;

public class CreateGroup implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player p)) return false;
        if (args.length == 0) return false;

        switch (args[0]) {
            case "quit" -> {
                if (args.length == 1) return false;

                List<Group> groups = Group.getByPlayer(p);
                if (groups.size() == 0) {
                    p.sendMessage(GestionPDA.PDAText + ChatColor.RED + "Vous n'appartenez pas à un groupe");
                    return true;
                }

                Group group = Group.incList.get(args[1]);
                if (group == null) {
                    p.sendMessage(GestionPDA.PDAText + ChatColor.RED + "Ce groupe n'existe pas");
                    return true;
                }

                if (!group.getMembers().containsKey(p.getUniqueId())) {
                    p.sendMessage(GestionPDA.PDAText + ChatColor.RED + "Vous n'appartenez pas à ce groupe");
                    return true;
                }

                if (group.getOwner().equals(p.getUniqueId())) {
                    p.sendMessage(GestionPDA.PDAText + ChatColor.RED + "Vous ne pouvez pas quitter votre propre groupe");
                    return true;
                }

                group.getMembers().remove(p.getUniqueId());
                p.sendMessage(GestionPDA.PDAText + ChatColor.GREEN + "Vous avez quitté " + group.getName());
                return true;
            }

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
                    p.sendMessage(GestionPDA.PDAText + ChatColor.RED + "Vous n'êtes pas chef de groupe");
                    return true;
                }

                if (args.length < 2 || !args[1].equals("confirm")) {
                    p.sendMessage(GestionPDA.PDAText + ChatColor.YELLOW + "Vous êtes sur le point de supprimer votre groupe. Pour confirmer : " + ChatColor.RESET + "/group delete confirm");
                    return true;
                }

                giveRemainingToOwner(group,p);
                group.deleteData();
                Group.incList.remove(group.getName());
                group.setSolde(0);
                p.sendMessage(GestionPDA.PDAText + ChatColor.GREEN + "Votre groupe " + group.getName() + " a bien été supprimé");
                return true;
            }

            case "list" -> {
                p.sendMessage(ChatColor.GOLD + "--------------- Groupes ---------------");
                HashMap<Group,Float> solde = new HashMap<>();
                for (Group group : Group.incList.values()) {
                    solde.put(group, group.getSolde());
                }

                Map<Group, Float> result = solde.entrySet()
                        .stream()
                        .sorted(Map.Entry.comparingByValue())
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                (oldValue, newValue) -> oldValue, LinkedHashMap::new));

                Object[] groups = result.keySet().toArray();


                for (int i = groups.length; i > 0; i--) {
                    p.sendMessage(ChatColor.GOLD + "● " + ((Group) groups[i-1]).getName());
                    for (String name : ((Group) groups[i-1]).getMembers().values()) {
                        p.sendMessage(ChatColor.YELLOW + "     - " + name);
                    }
                }
                p.sendMessage(ChatColor.GOLD + "---------------------------------------");
                return true;
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
                    /*case "name" -> {
                        if (args.length != 3) {
                            p.sendMessage(GestionPDA.PDAText + ChatColor.RED + "Merci d'entrer un nom valide");
                            return true;
                        }
                        String name = args[2];
                        p.sendMessage(GestionPDA.PDAText + ChatColor.GREEN + group.getName() + " a bien été renommé en " + name);
                        group.setName(name);
                    }*/

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

                        boolean isOwner = false;
                        for (Group temp : Group.getByPlayer(p)) {
                            if (temp.getOwner().equals(p.getUniqueId())) {
                                isOwner = true;
                                break;
                            }
                        }

                        if (isOwner) {
                            p.sendMessage(GestionPDA.PDAText + ChatColor.RED + "Le joueur est déjà chef de groupe");
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
                return true;
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
