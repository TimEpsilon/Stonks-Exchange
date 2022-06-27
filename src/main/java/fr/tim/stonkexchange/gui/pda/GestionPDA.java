package fr.tim.stonkexchange.gui.pda;

import fr.tim.stonkexchange.StonkExchange;
import fr.tim.stonkexchange.bank.taux.CalculTaux;
import fr.tim.stonkexchange.bank.taux.Taux;
import fr.tim.stonkexchange.files.ConfigManager;
import fr.tim.stonkexchange.items.CustomItems;
import fr.tim.stonkexchange.listeners.taux.GetTauxParametres;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
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
            if (!item.getItemMeta().getPersistentDataContainer().get(CustomItems.CustomItemKey,PersistentDataType.STRING).contains(CustomItems.PDA.getName())) return;

            p.sendMessage(Component.text(  ChatColor.AQUA + "Actualisation..."));

            showStats(p);
            showMap(item);
            }
        }

        private void showStats(Player p) {

            float[] coeff = ConfigManager.getCoeff("Taux.Somme");
            float aJoueurs = coeff[0];
            float aVariation = coeff[1];
            float aMort = coeff[2];
            float aDiamonds = coeff[3];
            float aBoss = coeff[4];
            float aAdvancement = coeff[5];
            float aOres = coeff[6];

            float j = GetTauxParametres.JoinedList.size();
            float m = 0;
            int d = 0;
            int a = 0;
            int b = 0;
            int o = 0;

            for (int i : GetTauxParametres.DeadList.values()) {
                m += i;
            }

            for (int i : GetTauxParametres.DiamondList.values()) {
                d += i;
            }

            for (int i : GetTauxParametres.AdvancementCount.values()) {
                a += i;
            }

            for (int i : GetTauxParametres.BossCount.values()) {
                b += i;
            }

            for (int i : GetTauxParametres.OreCount.values()) {
                o += i;
            }

            float v = GetTauxParametres.getTotalNow()
                    - GetTauxParametres.getTotalBefore(System.currentTimeMillis()- 10);//to do

            p.sendMessage(Component.text(PDAText + ChatColor.AQUA + "Informations du jour :"));
            p.sendMessage(Component.text(PDAText + ChatColor.LIGHT_PURPLE + "Joueurs Connectés : " + j + ChatColor.GRAY + " - Influence : " + Math.round(aJoueurs*CalculTaux.joueurs(j)*1000f)/1000f));
            p.sendMessage(Component.text(PDAText + ChatColor.LIGHT_PURPLE + "Nombre de Morts : " + m + ChatColor.GRAY + " - Influence : " + Math.round(aMort*CalculTaux.morts(m)*1000f)/1000f));
            p.sendMessage(Component.text(PDAText + ChatColor.LIGHT_PURPLE + "Diamants Minés : " + d + ChatColor.GRAY + " - Influence : " + Math.round(aDiamonds*CalculTaux.diamonds(d)*1000f)/1000f));
            p.sendMessage(Component.text(PDAText + ChatColor.LIGHT_PURPLE + "Variation de la Banque : " + v + ChatColor.GRAY + " - Influence : " + Math.round(aVariation*CalculTaux.variation(v)*1000f)/1000f));
            p.sendMessage(Component.text(PDAText + ChatColor.LIGHT_PURPLE + "Boss vaincus : " + b + ChatColor.GRAY + " - Influence : " + Math.round(aBoss*CalculTaux.boss(b)*1000f)/1000f));
            p.sendMessage(Component.text(PDAText + ChatColor.LIGHT_PURPLE + "Advancements obtenus : " + a + ChatColor.GRAY + " - Influence : " + Math.round(aAdvancement*CalculTaux.advancement(a)*1000f)/1000f));
            p.sendMessage(Component.text(PDAText + ChatColor.LIGHT_PURPLE + "Minerais Rares Minés : " + o + ChatColor.GRAY + " - Influence : " + Math.round(aOres*CalculTaux.ores(o)*1000f)/1000f));
        }

        private void showMap(ItemStack itemMap) {
            for (MapRenderer render : map.getRenderers()) {
                map.removeRenderer(render);
            }
            Taux taux = StonkExchange.getPlugin().taux;

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
