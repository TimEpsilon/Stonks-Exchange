package fr.tim.stonkexchange.items;

import fr.tim.stonkexchange.StonkExchange;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public enum CustomItems {

    MCOIN(Material.EMERALD, ChatColor.AQUA + "" + ChatColor.BOLD + "M-Coin",42),
    PDA(Material.FILLED_MAP,ChatColor.GREEN + "" + ChatColor.BOLD + "S.A.M.",96,ChatColor.GRAY+"Stonks Assistant Manager", ChatColor.GRAY + "" + ChatColor.ITALIC + "'Te ramènes chez toi quand t'es bourré!'"),
    PDA_ITEM(Material.PAPER,ChatColor.GREEN + "" + ChatColor.BOLD + "S.A.M.",96,ChatColor.GRAY+"Stonks Assistant Manager", ChatColor.GRAY + "" + ChatColor.ITALIC + "'Te ramènes chez toi quand t'es bourré!'")
    ;

    public static NamespacedKey CustomItemKey =  new NamespacedKey(StonkExchange.getPlugin(),"customitem");
    private ItemStack item;
    private String name;

    CustomItems(Material material, String name, int cmd, String... lore) {
        List<Component> Lore = new ArrayList<>();
        for (String l : lore) {
            Lore.add(Component.text(l));
        }

        this.item = new ItemStack(material,1);
        this.name = ChatColor.stripColor(name);

        ItemMeta meta = this.item.getItemMeta();
        meta.displayName(Component.text(name));
        meta.lore(Lore);
        meta.setCustomModelData(cmd);
        PersistentDataContainer data = meta.getPersistentDataContainer();
        data.set(new NamespacedKey(StonkExchange.getPlugin(),"customitem"), PersistentDataType.STRING,this.name);
        this.item.setItemMeta(meta);
    }

    public ItemStack getItem() {
        return item;
    }

    public String getName() {
        return name;
    }

}
