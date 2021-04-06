package fr.tim.smpbank.listeners;

import fr.tim.smpbank.bank.Bank;
import fr.tim.smpbank.smpBank;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;


import java.util.HashMap;
import java.util.UUID;


public class PlayerJoin implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        HashMap<UUID, Bank> liste = smpBank.getPlugin().getListeJoueurs();
        addPlayer(player,liste);

    }

    private Bank creationCompte(Player player) {
        return new Bank(player);
    }

    private void addPlayer(Player player, HashMap<UUID, Bank> liste) {
        if (!liste.containsKey(player.getUniqueId())) liste.put(player.getUniqueId(),creationCompte(player));
    }
}
