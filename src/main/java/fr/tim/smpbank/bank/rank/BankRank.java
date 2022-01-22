package fr.tim.smpbank.bank.rank;

public enum BankRank {

    GREEN(10,0),
    BLUE(100,5),
    PURPLE(500,25),
    RED(1000,100),
    ORANGE(5000,500),
    GOLD(1000000,1000);

    private int maxStorage;
    private  int price;

    BankRank(int maxStorage, int price) {
        this.maxStorage = maxStorage;
        this.price = price;
    }

    public int getMaxStorage() {
        return maxStorage;
    }

    public int getPrice() {
        return price;
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
            case "ORANGE":
                return BankRank.ORANGE;
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
                return BankRank.ORANGE;
            case ORANGE:
                return BankRank.GOLD;
        }
        return null;
    }
}
