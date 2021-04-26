package fr.tim.smpbank.commands;

import fr.tim.smpbank.files.Autosave;
import fr.tim.smpbank.files.CoeffConfig;
import fr.tim.smpbank.smpBank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import static fr.tim.smpbank.bank.Taux.newTaux;

public class Taux implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
       if (sender.isOp()) {
           Bukkit.broadcastMessage(ChatColor.AQUA + "Nouveau taux!");
           float tau = CoeffConfig.getCoeff("Taux.total")[0];
           float Tn = Autosave.getTauxDate(1);
           int jn = smpBank.getPlugin().getJoined().size();
           double x = Autosave.getTotalSolde(1);
           double dx = x - Autosave.getTotalSolde(2);
           float d = smpBank.getPlugin().getDead().size();

           Bukkit.broadcastMessage("Tn = " + Tn);
           Bukkit.broadcastMessage("jn = " + jn);
           Bukkit.broadcastMessage("x = " + x);
           Bukkit.broadcastMessage("dx = " + dx);
           float taux = newTaux(tau,Tn,jn,x,dx,d);

           smpBank.getPlugin().setTaux(taux);

           smpBank.getPlugin().getJoined().clear();
           smpBank.getPlugin().getDead().clear();
           return true;
       }
       return false;
    }

}
