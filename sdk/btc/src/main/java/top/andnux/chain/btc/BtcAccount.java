package top.andnux.chain.btc;

import top.andnux.chain.core.Account;

public class BtcAccount extends Account {

    private String mnemonic;
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMnemonic() {
        return mnemonic;
    }

    public void setMnemonic(String mnemonic) {
        this.mnemonic = mnemonic;
    }
}
