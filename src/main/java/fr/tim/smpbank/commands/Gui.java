package fr.tim.smpbank.commands;

import fr.tim.smpbank.bank.Bank;
import fr.tim.smpbank.gui.BankInterface;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Gui implements CommandExecutor {

    /**Commande Gui (en fait c'est la commande bank mais osef)
     * Affiche l'interface de la banque.
     * @param sender
     * @param command
     * @param label
     * @param args
     * @return
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!sender.isOp()) return false;

        if (sender instanceof Player) {
            if (args.length == 0) {
                Player player = (Player) sender;
                new BankInterface(player);

                return true;
            } else {
                if (!Bank.bankList.containsKey(UUID.fromString(args[0]))) new Bank(UUID.fromString(args[0]).toString());
                new BankInterface((Player) sender,UUID.fromString(args[0]));
                return true;
            }

        }
        return false;
    }
}
