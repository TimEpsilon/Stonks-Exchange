package fr.tim.smpbank.commands;

import fr.tim.smpbank.files.Autosave;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Forcesave implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

            Autosave.loadConfigManager();
            Bukkit.broadcastMessage("commande forcesave");

            return true;
    }
}
