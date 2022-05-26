package fr.tim.stonkexchange.listeners.environment;

import fr.tim.stonkexchange.StonkExchange;
import fr.tim.stonkexchange.bank.Bank;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;


public class PlayerJoin implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (!player.hasDiscoveredRecipe(new NamespacedKey(StonkExchange.getPlugin(),"SAM"))) player.discoverRecipe(new NamespacedKey(StonkExchange.getPlugin(),"SAM"));
        Bank bank = Bank.bankList.get(player);

        if (bank == null) {
            bank = new Bank(player);
        } else {
            bank.loadData();
        }

        bank.getRank().getTeam().addPlayer(player);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        Bank bank = Bank.bankList.get(player.getUniqueId());

        bank.logBankState();
    }
}
