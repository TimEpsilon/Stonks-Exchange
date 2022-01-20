package fr.tim.smpbank.gui.pda;

import fr.tim.smpbank.StonksExchange;
import fr.tim.smpbank.bank.taux.Taux;
import fr.tim.smpbank.items.CustomItems;
import fr.tim.smpbank.listeners.taux.GetTauxParametres;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.persistence.PersistentDataType;

public class GestionPDA implements Listener {
    private static MapView map = Bukkit.createMap(Bukkit.getWorlds().get(0));
    public final static String PDAText = ChatColor.GREEN + "" + ChatColor.BOLD + "[S.A.M.] ";

    static {

        map.setTrackingPosition(false);
        map.setUnlimitedTracking(false);
        map.setScale(MapView.Scale.FARTHEST);
        map.getRenderers().clear();
    }

    @EventHandler
    public void mapInteraction(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        ItemStack item = e.getItem();

        if (item == null) return;

        if (item.getItemMeta().getPersistentDataContainer().has(CustomItems.CustomItemKey, PersistentDataType.STRING)) {
            if (!item.getItemMeta().getPersistentDataContainer().get(CustomItems.CustomItemKey,PersistentDataType.STRING).contains(ChatColor.GREEN + "" + ChatColor.BOLD + "S.A.M.")) return;

            p.sendMessage(Component.text(  ChatColor.AQUA + "Actualisation..."));

            switch (item.getType().toString()) {
                case "FILLED_MAP":

                    if (p.isSneaking()) {

                        if (e.getHand().equals(EquipmentSlot.HAND)) {
                            p.getInventory().setItemInMainHand(CustomItems.PDA_ITEM.getItem());
                        } else {
                            p.getInventory().setItemInOffHand(CustomItems.PDA_ITEM.getItem());
                        }

                        showStats(p);

                    } else {
                        showMap(item);
                    }

                    break;

                case "PAPER":

                    if (p.isSneaking()) {

                        if (e.getHand().equals(EquipmentSlot.HAND)) {
                            p.getInventory().setItemInMainHand(CustomItems.PDA.getItem());
                            showMap(p.getInventory().getItemInMainHand());
                        } else {
                            p.getInventory().setItemInOffHand(CustomItems.PDA.getItem());
                            showMap(p.getInventory().getItemInOffHand());
                        }

                    } else {
                        showStats(p);
                    }
                    break;
            }

            Bukkit.getScheduler().runTaskLaterAsynchronously(StonksExchange.getPlugin(),()->{
                p.getScoreboardTags().remove("SamOnCooldown");
            },100);

            }
        }

        private void showStats(Player p) {
            if (p.getScoreboardTags().contains("SamOnCooldown")) {
                p.sendMessage(Component.text(PDAText + ChatColor.DARK_RED + ChatColor.ITALIC + "Sous cooldown..."));
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING,SoundCategory.PLAYERS,1,0.1f);
                return;
            }
            p.getScoreboardTags().add("SamOnCooldown");

            float j = GetTauxParametres.JoinedList.size();
            float m = 0;
            float d = 0;

            for (int i : GetTauxParametres.DeadList.values()) {
                m += i;
            }

            for (int i : GetTauxParametres.DiamondList.values()) {
                d += i;
            }

            float v = GetTauxParametres.getTotalNow()
                    - GetTauxParametres.getTotalBefore(System.currentTimeMillis()- 10);//to do

            final float mort = m;
            final float diamonds = d;
            final float variation = v;

            p.sendMessage(Component.text(PDAText + ChatColor.AQUA + "Informations du jour :"));
            Bukkit.getScheduler().runTaskLaterAsynchronously(StonksExchange.getPlugin(),()->{
                p.sendMessage(Component.text(PDAText + ChatColor.LIGHT_PURPLE + "Joueurs Connectés : " + j));
            },20);
            Bukkit.getScheduler().runTaskLaterAsynchronously(StonksExchange.getPlugin(),()->{
                p.sendMessage(Component.text(PDAText + ChatColor.LIGHT_PURPLE + "Nombre de Morts : " + mort));
            },40);
            Bukkit.getScheduler().runTaskLaterAsynchronously(StonksExchange.getPlugin(),()->{
                p.sendMessage(Component.text(PDAText + ChatColor.LIGHT_PURPLE + "Diamants Minés : " + diamonds));
            },60);
            Bukkit.getScheduler().runTaskLaterAsynchronously(StonksExchange.getPlugin(),()->{
                p.sendMessage(Component.text(PDAText + ChatColor.LIGHT_PURPLE + "Variation de la Banque : " + variation));
            },80);
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
