package fr.tim.smpbank.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

public class GraphRender implements Listener {
    private static MapView map = Bukkit.createMap(Bukkit.getWorlds().get(0));


    static {

        map.setTrackingPosition(false);
        map.setUnlimitedTracking(false);
        map.setScale(MapView.Scale.FARTHEST);
        map.getRenderers().clear();
    }

    @EventHandler
    public void showGraph(PlayerInteractEvent e) {


        if (e.getMaterial().isAir()) return;

        ItemStack item = e.getItem();
        if (item.getType().equals(Material.FILLED_MAP) && item.getItemMeta().getCustomModelData() == 96) {
            for (MapRenderer render : map.getRenderers()) {
                map.removeRenderer(render);
            }
            float[] x = {0,1,2,3,4,5,6,7,8,9};
            float[] y = {(int)(Math.random()*10),(int)(Math.random()*10),(int)(Math.random()*10),3,(int)(Math.random()*10),(int)(Math.random()*10),(int)(Math.random()*10),(int)(Math.random()*10),(int)(Math.random()*10),6};

            //float[] x = {0,1,2};
            //float[] y = {2,-3,4};
            map.addRenderer(new MapRender(x,y));

            MapMeta meta = (MapMeta) e.getItem().getItemMeta();
            meta.setMapView(map);
            e.getItem().setItemMeta(meta);
            }
        }
    }
