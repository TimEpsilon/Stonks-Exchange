package fr.tim.smpbank.bank.rank;

import fr.tim.smpbank.StonksExchange;
import fr.tim.smpbank.bank.Bank;
import fr.tim.smpbank.gui.pda.GestionPDA;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

public abstract class RankManager {

    public static NamespacedKey RankKey = new NamespacedKey(StonksExchange.getPlugin(), "BankRank");

    public static void upggradeAccount(Player p) {
        Bank b = Bank.bankList.get(p.getUniqueId());
        BankRank rank = getRank(p);
        BankRank next = BankRank.getNextRank(rank);

        if (b.getSolde() >= next.getPrice()) {
            b.add(-next.getPrice());
            setRank(p,next);
            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS,1,1);
            p.sendMessage(Component.text(GestionPDA.PDAText+ "§4F§ce§6l§ei§2c§ai§bt§3a§1t§9i§do§5n" + ChatColor.GREEN + "Vous êtes à présent rang " + ChatColor.valueOf(next.toString()) + next.toString()));
        } else {
            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_HURT, SoundCategory.PLAYERS,1,0.5f);
            p.sendMessage(Component.text(GestionPDA.PDAText+ ChatColor.RED + "Pas assez de M-Coins pour effectuer cette action"));
        }

    }

    private static BankRank getRank(Player p) {
        if (p.getPersistentDataContainer().has(RankKey, PersistentDataType.STRING)) {
            String name = p.getPersistentDataContainer().get(RankKey,PersistentDataType.STRING);
            return BankRank.getRankByName(name);
        }
        return null;
    }

    private static void setRank(Player p,BankRank rank) {
        p.getPersistentDataContainer().set(RankKey,PersistentDataType.STRING,rank.toString());
    }
}
