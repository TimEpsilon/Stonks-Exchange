package fr.tim.stonkexchange.bank.rank;

import fr.tim.stonkexchange.bank.Bank;
import fr.tim.stonkexchange.gui.pda.GestionPDA;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

public abstract class RankManager {

    public static void upggradeAccount(Player p) {
        Bank b = Bank.bankList.get(p.getUniqueId());
        BankRank rank = b.getRank();
        BankRank next = BankRank.getNextRank(rank);

        if (next == null) {
            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_HURT, SoundCategory.PLAYERS,1,0.5f);
            p.sendMessage(Component.text(GestionPDA.PDAText+ ChatColor.RED + "Rang maximal atteint"));
            return;
        }
        if (b.getSolde() >= next.getPrice()) {
            b.add(-next.getPrice());
            b.setRank(next);
            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS,1,1);
            p.sendMessage(Component.text(GestionPDA.PDAText+ "§4F§ce§6l§ei§2c§ai§bt§3a§1t§9i§do§5n " + ChatColor.GREEN + "Vous êtes à présent rang " + next.getColor() + next));
            b.getRank().getTeam().addPlayer(p);
        } else {
            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_HURT, SoundCategory.PLAYERS,1,0.5f);
            p.sendMessage(Component.text(GestionPDA.PDAText+ ChatColor.RED + "Pas assez de M-Coins pour effectuer cette action"));
        }

    }
}
