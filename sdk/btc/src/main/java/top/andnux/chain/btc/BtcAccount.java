package top.andnux.chain.btc;

import top.andnux.chain.core.Account;

public class BtcAccount extends Account {

    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
