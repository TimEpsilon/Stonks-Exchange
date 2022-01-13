package fr.tim.smpbank.bank;

import fr.tim.smpbank.StonksExchange;
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

    MCOIN(Material.EMERALD,1, ChatColor.AQUA + "" + ChatColor.BOLD + "M-Coin",42),
    PDA(Material.FILLED_MAP,1,ChatColor.GREEN + "" + ChatColor.BOLD + "S.A.M.",96,ChatColor.GRAY+"Stonks Assistant Manager", ChatColor.GRAY + "" + ChatColor.ITALIC + "'Te ramènes chez toi quand t'es bourré!'")
    ;

    public static NamespacedKey CustomItemKey =  new NamespacedKey(StonksExchange.getPlugin(),"customItem");
    private ItemStack item;

    CustomItems(Material material, int n, String name, int cmd, String... lore) {
        List<Component> Lore = new ArrayList<>();
        for (String l : lore) {
            Lore.add(Component.text(l));
        }

        this.item = new ItemStack(material,n);

        ItemMeta meta = this.item.getItemMeta();
        meta.displayName(Component.text(name));
        meta.lore(Lore);
        meta.setCustomModelData(cmd);
        PersistentDataContainer data = meta.getPersistentDataContainer();
        data.set(new NamespacedKey(StonksExchange.getPlugin(),"customItem"), PersistentDataType.STRING,name);
        this.item.setItemMeta(meta);
    }

    public ItemStack getItem() {
        return this.item;
    }

}
