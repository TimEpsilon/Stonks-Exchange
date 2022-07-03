package fr.tim.stonkexchange.commands;

import fr.tim.stonkexchange.items.CustomItems;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Items implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        Player p = (Player) sender;
        if (!p.isOp()) return false;

        for (CustomItems ci : CustomItems.values()) {
            p.getInventory().addItem(ci.getItem());
        }
        return true;
    }
}
