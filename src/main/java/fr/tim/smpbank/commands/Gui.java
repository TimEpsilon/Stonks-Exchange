package fr.tim.smpbank.commands;

import fr.tim.smpbank.gui.Interface;
import fr.tim.smpbank.smpBank;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Gui implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            Interface gui = smpBank.getPlugin().getListeJoueurs().get(player.getUniqueId()).getGUI();

            player.openInventory(gui.getGUI());

            return true;
        }
        return false;
    }
}
