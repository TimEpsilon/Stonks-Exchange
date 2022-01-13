package fr.tim.smpbank.commands;

import fr.tim.smpbank.bank.CustomItems;
import fr.tim.smpbank.files.Autosave;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Forcesave implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;
            p.getInventory().addItem(CustomItems.PDA.getItem());
            return true;
        }
        return false;
    }
}
