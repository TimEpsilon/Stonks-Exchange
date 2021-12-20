package fr.tim.smpbank.listeners;

import fr.tim.smpbank.bank.Bank;
import fr.tim.smpbank.smpBank;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;


import java.util.HashMap;
import java.util.UUID;


public class PlayerJoin implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        Bank bank = Bank.bankList.get(player);

        if (bank == null) {
            new Bank(player);
        } else {
            bank.loadData();
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        Bank bank = Bank.bankList.get(player);

        bank.logBankState();
    }
}
