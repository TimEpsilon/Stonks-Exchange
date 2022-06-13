package fr.tim.stonkexchange.commands;

import fr.tim.stonkexchange.bank.Bank;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ShowRank implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        ArrayList<String> green = new ArrayList<>();
        ArrayList<String> blue = new ArrayList<>();
        ArrayList<String> purple = new ArrayList<>();
        ArrayList<String> red = new ArrayList<>();
        ArrayList<String> yellow = new ArrayList<>();
        ArrayList<String> gold = new ArrayList<>();
        for (Bank b : Bank.bankList.values()) {
            switch (b.getRank()) {
                case GREEN -> green.add(b.getName());
                case BLUE -> blue.add(b.getName());
                case PURPLE -> purple.add(b.getName());
                case RED -> red.add(b.getName());
                case YELLOW -> yellow.add(b.getName());
                case GOLD -> gold.add(b.getName());
            }
        }
        sender.sendMessage(ChatColor.GOLD + "\n----------- Ranks -----------\n ");
        gold.forEach(String -> sender.sendMessage(ChatColor.GOLD + "⦁ " + "\u9005 " +String));
        yellow.forEach(String -> sender.sendMessage(ChatColor.GOLD + "⦁ " + ChatColor.YELLOW + "\u9004 " +String));
        red.forEach(String -> sender.sendMessage(ChatColor.GOLD + "⦁ " + ChatColor.RED + "\u9003 " +String));
        purple.forEach(String -> sender.sendMessage(ChatColor.GOLD + "⦁ " + ChatColor.LIGHT_PURPLE + "\u9002 " +String));
        blue.forEach(String -> sender.sendMessage(ChatColor.GOLD + "⦁ " + ChatColor.BLUE + "\u9001 " +String));
        green.forEach(String -> sender.sendMessage(ChatColor.GOLD + "⦁ " + ChatColor.GREEN +"\u9000 " + String));
        sender.sendMessage(ChatColor.GOLD + "\n-----------------------------\n ");
        return true;
    }
}
