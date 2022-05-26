package fr.tim.stonkexchange.misc;

import fr.tim.stonkexchange.StonkExchange;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class PlayerHeadPersistent implements Listener {

    @EventHandler
    public void onHeadPlace(BlockPlaceEvent e) {
        if (!e.getBlock().getType().equals(Material.PLAYER_HEAD)) return;

        ItemStack head = e.getItemInHand();
        Block block = e.getBlock();
        Component name = head.getItemMeta().displayName();

        if (!head.getItemMeta().hasDisplayName()) return;

        PersistentDataContainer chunkData = block.getChunk().getPersistentDataContainer();
        chunkData.set(keyAtLocation(block.getLocation()), PersistentDataType.STRING, PlainTextComponentSerializer.plainText().serialize(name));
    }

    @EventHandler
    public void onHeadBreak(BlockBreakEvent e) {
        if (!e.getBlock().getType().equals(Material.PLAYER_HEAD)) return;

        Block block = e.getBlock();
        PersistentDataContainer chunkData = block.getChunk().getPersistentDataContainer();
        ItemStack item = block.getDrops().stream().toList().get(0);

        if (!chunkData.has(keyAtLocation(block.getLocation()))) return;


        Component name = PlainTextComponentSerializer.plainText().deserialize(chunkData.get(keyAtLocation(block.getLocation()), PersistentDataType.STRING));
        ItemMeta meta = item.getItemMeta();
        meta.displayName(name);
        item.setItemMeta(meta);

        e.setDropItems(false);
        block.getWorld().dropItem(block.getLocation(),item);

        chunkData.remove(keyAtLocation(block.getLocation()));
    }

    private static NamespacedKey keyAtLocation(Location loc) {
        return new NamespacedKey(StonkExchange.getPlugin(),loc.getBlockX() + "_" + loc.getBlockY() + "_" + loc.getBlockZ());
    }
}
