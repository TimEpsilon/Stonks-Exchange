package fr.tim.smpbank.items;

import fr.tim.smpbank.StonksExchange;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;

public class CustomCraft {

    public CustomCraft() {
        NamespacedKey key = new NamespacedKey(StonksExchange.getPlugin(),"SAM");
        ShapedRecipe SAM = new ShapedRecipe(key,CustomItems.PDA_ITEM.getItem());
        SAM.shape("012","*3*","4*5");
        SAM.setIngredient('0', Material.EMERALD);
        SAM.setIngredient('1',Material.NETHERITE_INGOT);
        SAM.setIngredient('2',Material.DIAMOND);
        SAM.setIngredient('3',Material.HEART_OF_THE_SEA);
        SAM.setIngredient('*',Material.IRON_INGOT);
        SAM.setIngredient('4',Material.COPPER_INGOT);
        SAM.setIngredient('5',Material.GOLD_INGOT);
        Bukkit.addRecipe(SAM);
    }

}
