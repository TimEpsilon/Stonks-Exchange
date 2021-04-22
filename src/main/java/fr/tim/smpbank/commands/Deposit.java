package fr.tim.smpbank.commands;

import fr.tim.smpbank.bank.Trader;
import fr.tim.smpbank.gui.Interface;
import fr.tim.smpbank.smpBank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Deposit implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) { //pas de check si le string est un int
                player.sendMessage(ChatColor.RED + "Veuillez entrer une valeur positive!");
                return false;
            }
            int n = Integer.parseInt(args[0]);
            Trader.deposit(n,player);
            return true;
        }
        return false;
    }
}
