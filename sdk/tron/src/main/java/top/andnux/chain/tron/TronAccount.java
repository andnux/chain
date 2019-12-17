package top.andnux.chain.tron;

import top.andnux.chain.core.Account;

public class TronAccount extends Account {

    private String address;
    private String mnemonic;
    private String keyStore;

    public TronAccount() {

    }

    public TronAccount(String privateKey, String publicKey, String address, String keyStore) {
        super(privateKey, publicKey);
        this.address = address;
        this.keyStore = keyStore;
    }

    public TronAccount(String privateKey, String publicKey, String address,
                       String mnemonic, String keyStore) {
        super(privateKey, publicKey);
        this.address = address;
        this.mnemonic = mnemonic;
        this.keyStore = keyStore;
    }

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

    public String getKeyStore() {
        return keyStore;
    }

    public void setKeyStore(String keyStore) {
        this.keyStore = keyStore;
    }
}
