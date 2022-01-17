package fr.tim.smpbank.gui;

import fr.tim.smpbank.StonksExchange;
import fr.tim.smpbank.bank.Taux;
import fr.tim.smpbank.items.CustomItems;
import fr.tim.smpbank.listeners.GetTauxParametres;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.persistence.PersistentDataType;

public class GestionPDA implements Listener {
    private static MapView map = Bukkit.createMap(Bukkit.getWorlds().get(0));
    public static String PDAText = ChatColor.GREEN + "" + ChatColor.BOLD + "[S.A.M.] ";


    static {

        map.setTrackingPosition(false);
        map.setUnlimitedTracking(false);
        map.setScale(MapView.Scale.FARTHEST);
        map.getRenderers().clear();
    }

    @EventHandler
    public void mapInteraction(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        if (e.getMaterial().isAir()) return;

        ItemStack item = e.getItem();
        if (item.getItemMeta().getPersistentDataContainer().has(CustomItems.CustomItemKey, PersistentDataType.STRING)) {
            if (!item.getItemMeta().getPersistentDataContainer().get(CustomItems.CustomItemKey,PersistentDataType.STRING).contains(ChatColor.GREEN + "" + ChatColor.BOLD + "S.A.M.")) return;

            switch (item.getType().toString()) {
                case "FILLED_MAP":
                    if (p.isSneaking()) {
                        p.getInventory().remove(item);
                        p.getInventory().setItemInMainHand(CustomItems.PDA_ITEM.getItem());
                    } else {
                        showMap(item);
                    }
                    break;
                case "PAPER":
                    if (p.isSneaking()) {
                        p.getInventory().remove(item);
                        p.getInventory().setItemInMainHand(CustomItems.PDA.getItem());
                        showMap(p.getInventory().getItemInMainHand());
                    } else {
                        float j = GetTauxParametres.JoinedList.size();
                        float m = 0;
                        float d = 0;
                        float v = 0;

                        for (int i : GetTauxParametres.DeadList.values()) {
                            m += i;
                        }

                        for (int i : GetTauxParametres.DiamondList.values()) {
                            d += i;
                        }

                        v = GetTauxParametres.getTotalNow()
                                - GetTauxParametres.getTotalBefore(System.currentTimeMillis()- 10);//to do

                        p.sendMessage(Component.text(PDAText + ChatColor.AQUA + "Informations du jour :"));
                        p.sendMessage(Component.text(PDAText + ChatColor.LIGHT_PURPLE + "Joueurs Connectés : " + j));
                        p.sendMessage(Component.text(PDAText + ChatColor.LIGHT_PURPLE + "Nombre de Morts : " + m));
                        p.sendMessage(Component.text(PDAText + ChatColor.LIGHT_PURPLE + "Diamants Minés : " + d));
                        p.sendMessage(Component.text(PDAText + ChatColor.LIGHT_PURPLE + "Variation de la Banque : " + v));

                    }
                    break;
            }

            }
        }

        private void showMap(ItemStack itemMap) {
            for (MapRenderer render : map.getRenderers()) {
                map.removeRenderer(render);
            }
            Taux taux = StonksExchange.getPlugin().taux;

            float[] x = new float[Math.min(10,taux.getTauxLog().size())];
            float[] y = new float[Math.min(10,taux.getTauxLog().size())];

            for (int i =0; i>-x.length; i--) {
                x[-i] = i;
                y[-i] = taux.getTauxLog().get(taux.getTauxLog().size() - 1 + i).getSolde();
            }

            map.addRenderer(new MapRender(x,y,0,-9,7,3));

            MapMeta meta = (MapMeta) itemMap.getItemMeta();
            meta.setMapView(map);
            itemMap.setItemMeta(meta);
        }
    }
