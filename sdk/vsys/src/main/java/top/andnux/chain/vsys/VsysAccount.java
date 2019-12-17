package top.andnux.chain.vsys;

import top.andnux.chain.core.Account;

public class VsysAccount extends Account {

    private String mnemonic;
    private String address;

    public VsysAccount() {

    }

    public VsysAccount(String privateKey, String publicKey, String address) {
        super(privateKey, publicKey);
        this.address = address;
    }

    public VsysAccount(String privateKey, String publicKey, String mnemonic, String address) {
        super(privateKey, publicKey);
        this.mnemonic = mnemonic;
        this.address = address;
    }

    public String getMnemonic() {
        return mnemonic;
    }

    public void setMnemonic(String mnemonic) {
        this.mnemonic = mnemonic;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
