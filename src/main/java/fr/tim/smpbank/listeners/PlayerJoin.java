package fr.tim.smpbank.listeners;

import fr.tim.smpbank.bank.bank;
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
        HashMap<UUID, bank> liste = smpBank.getPlugin().getListeJoueurs();
        addPlayer(player,liste);

    }

    private bank creationCompte(Player player) {
        return new bank(player);
    }

    private void addPlayer(Player player, HashMap<UUID,bank> liste) {
        if (!liste.containsKey(player.getUniqueId())) liste.put(player.getUniqueId(),creationCompte(player));
    }
}
