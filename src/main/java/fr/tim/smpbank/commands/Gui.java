package fr.tim.smpbank.commands;

import fr.tim.smpbank.gui.BankInterface;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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

        if (sender instanceof Player) {
            Player player = (Player) sender;
            BankInterface gui = new BankInterface(player);

            return true;
        }
        return false;
    }
}
