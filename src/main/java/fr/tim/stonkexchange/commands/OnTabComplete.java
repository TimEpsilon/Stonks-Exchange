package fr.tim.stonkexchange.commands;

import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class OnTabComplete implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player p = (Player) sender;
        Block target = p.getTargetBlock(5);
        switch(args.length) {
            case 1:
                return Collections.singletonList(target.getX() + "");
            case 2:
                return Collections.singletonList(target.getY() + "");
            case 3:
                return Collections.singletonList(target.getZ() + "");
        }
        return null;
    }
}
