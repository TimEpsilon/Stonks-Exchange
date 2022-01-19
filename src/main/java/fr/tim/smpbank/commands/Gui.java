package fr.tim.smpbank.commands;

import fr.tim.smpbank.StonksExchange;
import fr.tim.smpbank.bank.Bank;
import fr.tim.smpbank.bank.BankLog;
import fr.tim.smpbank.bank.taux.Taux;
import fr.tim.smpbank.files.FileManager;
import fr.tim.smpbank.files.Utils;
import fr.tim.smpbank.gui.bank.BankInterface;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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
            Player player = (Player) sender;

            if (args.length == 0) {
                new BankInterface(player);
                return true;

            } else {

                Bank b;
                if (args[0].length() == 36) {
                    UUID uuid = UUID.fromString(args[0]);

                    if (!Bank.bankList.containsKey(uuid)) new Bank(uuid.toString());
                    new BankInterface((Player) sender,uuid);
                    b = Bank.bankList.get(uuid);
                } else {
                    if (args[0].equalsIgnoreCase("taux")) {
                        saveToFile(StonksExchange.getPlugin().getTaux(),player);
                        return true;
                    }

                    OfflinePlayer op = Bukkit.getOfflinePlayerIfCached(args[0]);
                    if (op == null) {
                        player.sendMessage(ChatColor.RED+"Invalid uuid or username");
                        return false;
                    }

                    UUID uuid = op.getUniqueId();
                    if (!Bank.bankList.containsKey(uuid)) new Bank(uuid.toString());
                    new BankInterface((Player) sender,uuid);
                    b = Bank.bankList.get(uuid);
                }

                if (args.length == 2) {
                    if (args[1].equalsIgnoreCase("save")) {
                        saveToFile(b,player,args[0]);
                    }
                }
                return true;
            }
        }
        return false;
    }

    private void saveToFile(Bank b,Player player,String name) {
        BufferedWriter br;
        try {
            br = new BufferedWriter(new FileWriter(FileManager.BANK_PATH+name+".csv"));
            StringBuilder sb = new StringBuilder();
            for (BankLog bl : b.getBankLogList()) {
                sb.append(Utils.UnixToDate(bl.getTime()));
                sb.append(';');
                sb.append(String.valueOf(bl.getSolde()).replace(".",","));
                sb.append("\n");
            }
            br.write(sb.toString());
            br.close();
            player.sendMessage(ChatColor.GREEN + "File saved at " + FileManager.BANK_PATH+name+".csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveToFile(Taux t, Player p) {
        BufferedWriter br;
        try {
            br = new BufferedWriter(new FileWriter(FileManager.BANK_PATH+"taux.csv"));
            StringBuilder sb = new StringBuilder();
            for (BankLog bl : t.getTauxLog()) {
                sb.append(Utils.UnixToDate(bl.getTime()));
                sb.append(';');
                sb.append(String.valueOf(bl.getSolde()).replace(".",","));
                sb.append("\n");
            }
            br.write(sb.toString());
            br.close();
            p.sendMessage(ChatColor.GREEN + "File saved at " + FileManager.BANK_PATH+"taux.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
