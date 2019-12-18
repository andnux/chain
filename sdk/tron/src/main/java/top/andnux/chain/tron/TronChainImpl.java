package top.andnux.chain.tron;

import org.bouncycastle.util.encoders.Hex;
import org.tron.common.wallet.TronWallet;

import java.io.File;

import top.andnux.chain.core.AbstractChain;
import top.andnux.chain.core.AppEnv;
import top.andnux.chain.core.Callback;
import top.andnux.chain.core.MeasureCallback;

public class TronChainImpl extends AbstractChain<TronAccount, TronTransferParams>
        implements TronChain {

    private File mFile;

    @Override
    public String name() {
        return "TRON";
    }

    @Override
    public TronAccount createAccount() throws Exception {
        return createAccount("");
    }

    @Override
    public TronAccount createAccountByPrivateKey(String privateKey) throws Exception {
        return createAccountByPrivateKey(privateKey, "");
    }

    @Override
    public String getDefaultUrl(AppEnv env) {
        String defaultUrl = "";
        switch (env) {
            case MAIN:
                defaultUrl = "";
                break;
            case TEST:
                defaultUrl = "";
                break;
        }
        return defaultUrl;
    }

    @Override
    public TronAccount createAccount(String password) throws Exception {
        TronWallet tronWallet = TronWallet.generateWallet();
        mFile = new File(getApp().getExternalCacheDir(), "keystore");
        if (!mFile.exists())mFile.mkdirs();
        String mnemonic = tronWallet.getMnemonic();
        String keyStore = tronWallet.getKeyStore(password, mFile);
        String privateKey = Hex.toHexString(tronWallet.getPrivateKey());
        String publicKey = Hex.toHexString(tronWallet.getPublicKey());
        String address = tronWallet.getAddress();
        return new TronAccount(privateKey, publicKey, address, mnemonic, keyStore);
    }

    @Override
    public TronAccount createAccountByPrivateKey(String privateKey, String password) throws Exception {
        TronWallet tronWallet = TronWallet.generateKeyForPrivateKey(privateKey);
        mFile = new File(getApp().getExternalCacheDir(), "keystore");
        if (!mFile.exists())mFile.mkdirs();
        String mnemonic = tronWallet.getMnemonic();
        String keyStore = tronWallet.getKeyStore(password, mFile);
        String publicKey = Hex.toHexString(tronWallet.getPublicKey());
        String address = tronWallet.getAddress();
        return new TronAccount(privateKey, publicKey, address, mnemonic, keyStore);
    }

    @Override
    public TronAccount createAccountByKeyStore(String keyStore) throws Exception {
        return createAccountByKeyStore(keyStore, "");
    }

    @Override
    public TronAccount createAccountByKeyStore(String keyStore, String password) throws Exception {
        TronWallet tronWallet = TronWallet.generateKeyForKeyStore(keyStore, password);
        mFile = new File(getApp().getExternalCacheDir(), "keystore");
        if (!mFile.exists())mFile.mkdirs();
        String mnemonic = tronWallet.getMnemonic();
        String publicKey = Hex.toHexString(tronWallet.getPublicKey());
        String address = tronWallet.getAddress();
        String privateKey = Hex.toHexString(tronWallet.getPrivateKey());
        return new TronAccount(privateKey, publicKey, address, mnemonic, keyStore);
    }

    @Override
    public TronAccount createAccountByMnemonic(String mnemonic) throws Exception {
        return createAccountByMnemonic(mnemonic, "");
    }

    @Override
    public TronAccount createAccountByMnemonic(String mnemonic, String password) throws Exception {
        TronWallet tronWallet = TronWallet.generateKeyForMnemonic(mnemonic);
        mFile = new File(getApp().getExternalCacheDir(), "keystore");
        if (!mFile.exists())mFile.mkdirs();
        String publicKey = Hex.toHexString(tronWallet.getPublicKey());
        String address = tronWallet.getAddress();
        String privateKey = Hex.toHexString(tronWallet.getPrivateKey());
        String keyStore = tronWallet.getKeyStore(password, mFile);
        return new TronAccount(privateKey, publicKey, address, mnemonic, keyStore);
    }

    @Override
    public void measure(String url, int index, MeasureCallback callback) {

    }

    @Override
    public void getBalance(String account, Callback<String> callback) {

    }

    @Override
    public void getTokenBalance(String account, String token, String contract, Callback callback) {

    }

    @Override
    public void transfer(TronTransferParams params, Callback<String> callback) {

    }
}
