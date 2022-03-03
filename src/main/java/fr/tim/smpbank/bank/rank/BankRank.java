package fr.tim.smpbank.bank.rank;

import org.bukkit.ChatColor;

import java.io.Serializable;

public enum BankRank implements Serializable  {

    GREEN(25,0, ChatColor.GREEN),
    BLUE(100,5,ChatColor.AQUA),
    PURPLE(500,25,ChatColor.LIGHT_PURPLE),
    RED(1000,100,ChatColor.RED),
    YELLOW(5000,500,ChatColor.YELLOW),
    GOLD(1000000,1000,ChatColor.GOLD);

    private int maxStorage;
    private int price;
    private ChatColor color;

    BankRank(int maxStorage, int price,ChatColor color) {
        this.maxStorage = maxStorage;
        this.price = price;
        this.color = color;
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
