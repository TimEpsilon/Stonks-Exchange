package fr.tim.stonkexchange.commands;

import fr.tim.stonkexchange.bank.vault.Vault;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class VaultSpawn implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender.isOp() && sender instanceof Player && args.length == 3) {
            int x = Integer.parseInt(args[0]);
            int y = Integer.parseInt(args[1]);
            int z = Integer.parseInt(args[2]);

            Player p = (Player) sender;
            Location loc = new Location(p.getWorld(),x,y,z);
            new Vault(loc,p);

        }
        return false;
    }
}
