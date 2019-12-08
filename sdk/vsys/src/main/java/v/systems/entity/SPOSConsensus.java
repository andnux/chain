package v.systems.entity;

import java.io.Serializable;

public class SPOSConsensus implements Serializable {
    private Long mintTime;
    private Long mintBalance;

    public Long getMintTime() {
        return mintTime;
    }

    public void setMintTime(Long mintTime) {
        this.mintTime = mintTime;
    }

    public Long getMintBalance() {
        return mintBalance;
    }

    public void setMintBalance(Long mintBalance) {
        this.mintBalance = mintBalance;
    }
}
