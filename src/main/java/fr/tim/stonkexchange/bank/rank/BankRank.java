package fr.tim.stonkexchange.bank.rank;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Team;

import java.io.Serializable;

public enum BankRank implements Serializable  {

    GREEN(25,0, ChatColor.GREEN,NamedTextColor.GREEN,"\u9000"),
    BLUE(100,5,ChatColor.AQUA,NamedTextColor.AQUA,"\u9001"),
    PURPLE(500,25,ChatColor.LIGHT_PURPLE,NamedTextColor.LIGHT_PURPLE,"\u9002"),
    RED(1000,100,ChatColor.RED,NamedTextColor.RED,"\u9003"),
    YELLOW(5000,500,ChatColor.YELLOW,NamedTextColor.YELLOW,"\u9004"),
    GOLD(1000000,1000,ChatColor.GOLD,NamedTextColor.GOLD,"\u9005");

    private final int maxStorage;
    private final int price;
    private final ChatColor color;
    private transient Team team;

    BankRank(int maxStorage, int price,ChatColor color,NamedTextColor teamColor,String prefix) {
        this.maxStorage = maxStorage;
        this.price = price;
        this.color = color;


        if (Bukkit.getScoreboardManager().getMainScoreboard().getTeam("RANK_" + this) == null) {
            team = Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam("RANK_" + this);
        } else {
            team = Bukkit.getScoreboardManager().getMainScoreboard().getTeam("RANK_" + this);
        }
        team.color(teamColor);
        team.prefix(Component.text(prefix));
        team.setAllowFriendlyFire(true);
        team.setCanSeeFriendlyInvisibles(false);
    }

    public int getMaxStorage() {
        return maxStorage;
    }

    public int getPrice() {
        return price;
    }

    public ChatColor getColor() {
        return color;
    }

    public Team getTeam() {
        return team;
    }

    public static BankRank getRankByName(String name) {
        switch (name) {
            case "GREEN":
                return BankRank.GREEN;
            case "BLUE":
                return BankRank.BLUE;
            case "PURPLE":
                return BankRank.PURPLE;
            case "RED":
                return BankRank.RED;
            case "YELLOW":
                return BankRank.YELLOW;
            case "GOLD":
                return BankRank.GOLD;
        }
        return null;
    }

    public static BankRank getNextRank(BankRank current) {
        switch (current) {
            case GREEN:
                return BankRank.BLUE;
            case BLUE:
                return BankRank.PURPLE;
            case PURPLE:
                return BankRank.RED;
            case RED:
                return BankRank.YELLOW;
            case YELLOW:
                return BankRank.GOLD;
        }
        return null;
    }
}
