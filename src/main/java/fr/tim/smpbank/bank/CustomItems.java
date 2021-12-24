package fr.tim.smpbank.bank;

import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public enum CustomItems {

    MCOIN(Material.EMERALD,1, ChatColor.AQUA + "" + ChatColor.BOLD + "M-Coin",42,""),
    PDA(Material.FILLED_MAP,1,ChatColor.GREEN + "" + ChatColor.BOLD + "S.A.M.",96,ChatColor.GRAY+"Stonks Assistant Manager", ChatColor.GRAY + "" + ChatColor.ITALIC + "'Te ramènes chez toi quand t'es bourré!'")
    ;

    private ItemStack item;

    CustomItems(Material material, int n, String name, int cmd, String... lore) {
        List<Component> Lore = new ArrayList<>();
        for (String l : lore) {
            Lore.add(Component.text(l));
        }

        this.item = new ItemStack(material,n);

        ItemMeta meta = this.item.getItemMeta();
        meta.displayName(Component.text(name));
        meta.setCustomModelData(cmd);
        this.item.setItemMeta(meta);
    }

    public ItemStack getItem() {
        return this.item;
    }

}
