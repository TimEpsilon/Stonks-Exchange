package fr.tim.stonkexchange.bank;

import java.io.Serializable;

public class BankLog implements Serializable {

    private final long time;
    private final float solde;

    public BankLog(float solde) {
        this.time = System.currentTimeMillis();
        this.solde = solde;
    }

    public BankLog(float solde, long time) {
        this.time = time;
        this.solde = solde;
    }

    public float getSolde() {
        return this.solde;
    }

    public long getTime() {
        return this.time;
    }
}
