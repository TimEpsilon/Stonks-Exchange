package fr.tim.smpbank.bank;

import java.io.Serializable;

public class BankLog implements Serializable {

    private String uuid;
    private long time;
    private float solde;

    public BankLog(float solde) {
        this.time = System.currentTimeMillis();
        this.solde = solde;
    }
}
