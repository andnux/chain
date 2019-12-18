package top.andnux.chain.eos.bean;

import java.io.Serializable;

public class CurrencyBalanceRequest implements Serializable {

    private String account;
    private String code;
    private String symbol;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
