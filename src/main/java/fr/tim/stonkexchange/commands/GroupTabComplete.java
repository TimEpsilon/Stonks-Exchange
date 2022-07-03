package fr.tim.stonkexchange.commands;

import fr.tim.stonkexchange.bank.group.Group;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GroupTabComplete implements TabCompleter {

    public final static ArrayList<String> argumentList = new ArrayList<>(List.of(
            "create",
            "delete",
            "modify",
            "list",
            "quit"));
    public final static ArrayList<String> modifyList = new ArrayList<>(List.of(
            "add",
            "remove",
            "emblem",
            "owner"));

    public static List<String> dynamicTab(List<String> list, String arg) {
        List<String> finalList = new ArrayList<>(list);
        for (String s : list) {
            if (!s.toLowerCase().startsWith(arg.toLowerCase())) {
                finalList.remove(s);
            }
        }
        Collections.sort(finalList);
        return finalList;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) return dynamicTab(argumentList,args[0]);
        if (args.length == 2 && args[0].equals("modify")) return dynamicTab(modifyList,args[1]);

        List<Group> groups = Group.getByPlayer((Player) sender);
        List<String> names = new ArrayList<>();
        groups.forEach(Group -> names.add(Group.getName()));
        if (args.length == 2 && args[0].equals("quit")) return dynamicTab(names,args[1]);
        return null;
    }
}
