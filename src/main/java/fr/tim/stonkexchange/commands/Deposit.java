package fr.tim.stonkexchange.commands;

import fr.tim.stonkexchange.bank.Trader;
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

            if (args.length != 1) {
                player.sendMessage(ChatColor.RED + "Veuillez entrer un unique argument!");
                return false;
            } else {
                try {
                    int n = Integer.parseInt(args[0]);
                    if (n<0) {
                        player.sendMessage(ChatColor.RED +"Veuillez entrer un entier positif!");
                        return false;
                    }
                    Trader.deposit(n,player,player.getUniqueId());
                    return true;
                } catch (NumberFormatException e) {
                    player.sendMessage(ChatColor.RED +"L'argument n'est pas un entier!");
                    return false;
                }
            }
        }
        return false;
    }
}
