package fr.tim.stonkexchange.items;

import fr.tim.stonkexchange.StonkExchange;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

public class CustomCraft {

    public CustomCraft() {
        SamCraft();
        RecallCraft();
    }

    private void SamCraft() {
        NamespacedKey key = new NamespacedKey(StonkExchange.getPlugin(),"SAM");
        ShapedRecipe SAM = new ShapedRecipe(key,CustomItems.PDA.getItem());
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

    private void RecallCraft() {
        NamespacedKey key = new NamespacedKey(StonkExchange.getPlugin(),"recall_potion");
        ShapelessRecipe recipe = new ShapelessRecipe(key,CustomItems.RECALL_POTION.getItem());

        ItemStack potion = new ItemStack(Material.POTION);
        PotionMeta meta = (PotionMeta) potion.getItemMeta();
        meta.setBasePotionData(new PotionData(PotionType.WATER));
        potion.setItemMeta(meta);

        recipe.addIngredient(potion);
        recipe.addIngredient(CustomItems.MCOIN.getItem());
        Bukkit.addRecipe(recipe);
    }

}
