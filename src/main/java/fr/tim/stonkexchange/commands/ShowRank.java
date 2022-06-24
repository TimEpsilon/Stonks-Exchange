package fr.tim.stonkexchange.commands;

import fr.tim.stonkexchange.bank.Bank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class ShowRank implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        HashMap<String,Float> solde = new HashMap<>();

        for (Bank b : Bank.bankList.values()) {
            switch (b.getRank()) {
                case GREEN -> solde.put(ChatColor.GREEN +"\u9000 " + b.getName(), b.getSolde());
                case BLUE -> solde.put(ChatColor.BLUE + "\u9001" + b.getName(), b.getSolde());
                case PURPLE -> solde.put(ChatColor.LIGHT_PURPLE +"\u9002" + b.getName(), b.getSolde());
                case RED -> solde.put(ChatColor.RED +"\u9003" + b.getName(), b.getSolde());
                case YELLOW -> solde.put(ChatColor.YELLOW +"\u9004" + b.getName(), b.getSolde());
                case GOLD -> solde.put(ChatColor.GOLD +"\u9005" + b.getName(), b.getSolde());
            }
        }

        Map<String, Float> result = solde.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        sender.sendMessage(ChatColor.GOLD + "\n----------- Ranks -----------\n ");
        for (String name : result.keySet()) {
            sender.sendMessage(ChatColor.GOLD + "● " + name);
        }
        sender.sendMessage(ChatColor.GOLD + "\n-----------------------------\n ");
        return true;
    }
}
