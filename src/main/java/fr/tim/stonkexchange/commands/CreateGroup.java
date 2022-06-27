package fr.tim.stonkexchange.commands;

import fr.tim.stonkexchange.bank.group.Group;
import fr.tim.stonkexchange.gui.pda.GestionPDA;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CreateGroup implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player p)) return false;
        if (args.length == 0) return false;

        switch (args[0]) {
            case "create" -> {
                if (args.length != 2) return false;
                String name = args[1];

                if (Group.getByPlayer(p) != null) {
                    p.sendMessage(GestionPDA.PDAText + ChatColor.RED + "Vous appartenez déjà à un groupe");
                    return true;
                }

                new Group(name,p);
                p.sendMessage(GestionPDA.PDAText + ChatColor.GREEN + "Votre groupe " + name + " a bien été créé");
                return true;
            }
        }
        return false;
    }
}
