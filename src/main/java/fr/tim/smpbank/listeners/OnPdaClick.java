package fr.tim.smpbank.listeners;


import fr.tim.smpbank.files.Autosave;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class OnPdaClick implements Listener {

    @EventHandler
    public void onPdaClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        ItemStack item = e.getItem();

        if (item.getType().equals(Material.FILLED_MAP) && item.getItemMeta().getCustomModelData() == 96) {
            //récupérer sur 14 jours : joueurs, morts, total, moyenne, écart, taux
            ArrayList<ArrayList<Float>> days = new ArrayList<>(14);

            for (int i = 0; i<14; i++) {
                float taux = Autosave.getValueDate(i,"Taux");
                float total = Autosave.getValueDate(i,"Total");
                float death = Autosave.getValueDate(i,"Death");
                float joined = Autosave.getValueDate(i,"Joined");
                //float moyenne =
            }
        }


    }
}
