package top.andnux.chain.eos.impl;
import top.andnux.chain.eos.EosAccount;
import top.andnux.chain.eos.EosAccountManager;
import top.andnux.chain.eos.crypto.ec.EosPrivateKey;

public class EosAccountManagerImpl implements EosAccountManager {

    @Override
    public EosAccount createAccount() {
        EosPrivateKey privateKey = new EosPrivateKey();
        EosAccount eosAccount = new EosAccount();
        eosAccount.setPrivateKey(privateKey.toString());
        eosAccount.setPublicKey(privateKey.getPublicKey().toString());
        return eosAccount;
    }

    @Override
    public EosAccount createAccountByPrivateKey(String privateKey) {
        EosPrivateKey eosPrivateKey = new EosPrivateKey(privateKey);
        EosAccount eosAccount = new EosAccount();
        eosAccount.setPrivateKey(eosPrivateKey.toString());
        eosAccount.setPublicKey(eosPrivateKey.getPublicKey().toString());
        return eosAccount;
    }
}
