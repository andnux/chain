package top.andnux.chain.btc;

import org.bitcoinj.core.DumpedPrivateKey;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.LegacyAddress;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.SegwitAddress;
import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDKeyDerivation;
import org.bitcoinj.crypto.MnemonicCode;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.params.TestNet3Params;
import org.json.JSONObject;

import java.util.Collections;

import io.github.novacrypto.bip39.Words;
import top.andnux.chain.core.AbstractChain;
import top.andnux.chain.core.AppEnv;
import top.andnux.chain.core.Callback;
import top.andnux.chain.core.MeasureCallback;
import top.andnux.chain.core.Utils;
import top.andnux.chian.utils.http.HttpCallback;
import top.andnux.chian.utils.http.HttpRequest;

public class BtcChainImpl extends AbstractChain<BtcAccount, BtcTransferParams> implements BtcChain {

    @Override
    public BtcAccount create() throws Exception {
        return create(BtcAddressType.START_1);
    }

    @Override
    public BtcAccount createByPrivateKey(String privateKey) throws Exception {
        return createByPrivateKey(privateKey, BtcAddressType.START_1);
    }

    @Override
    public String getDefaultUrl(AppEnv env) {
        String defaultUrl = "";
        switch (env) {
            case MAIN:
                defaultUrl = "https://blockchain.info";
                break;
            case TEST:
                defaultUrl = "https://testnet.blockchain.info";
                break;
        }
        return defaultUrl;
    }

    @Override
    public BtcAccount createByPath(String path) throws Exception {
        return createByMnemonicAndPath(Utils.createMnemonic(Words.TWELVE), path, BtcAddressType.START_1);
    }

    @Override
    public BtcAccount create(BtcAddressType type) throws Exception {
        return createByMnemonicAndPath(Utils.createMnemonic(Words.TWELVE), getPath(), type);
    }

    @Override
    public BtcAccount createByMnemonic(String mnemonic) throws Exception {
        return createByMnemonicAndPath(Utils.createMnemonic(Words.TWELVE), getPath(), BtcAddressType.START_1);
    }

    private String getPath() {
        return AppEnv.getEnv() == AppEnv.MAIN ? "m/44'/1'/0'/0/0" : "m/44'/0'/0'/0/0";
    }

    @Override
    public BtcAccount createByMnemonic(String mnemonic, BtcAddressType type) throws Exception {
        return createByMnemonicAndPath(mnemonic, getPath(), type);
    }

    @Override
    public BtcAccount createByMnemonicAndPath(String mnemonic, String path, BtcAddressType type) throws Exception {
        byte[] seedBytes = MnemonicCode.toSeed(Collections.singletonList(mnemonic), "");
        DeterministicKey dkKey = HDKeyDerivation.createMasterPrivateKey(seedBytes);
        String[] pathArray = path.split("/");
        for (int i = 1; i < pathArray.length; i++) {
            ChildNumber childNumber;
            if (pathArray[i].endsWith("'")) {
                int number = Integer.parseInt(pathArray[i].substring(0,
                        pathArray[i].length() - 1));
                childNumber = new ChildNumber(number, true);
            } else {
                int number = Integer.parseInt(pathArray[i]);
                childNumber = new ChildNumber(number, false);
            }
            dkKey = HDKeyDerivation.deriveChildKey(dkKey, childNumber);
        }
        ECKey ecKey = ECKey.fromPrivate(dkKey.getPrivKeyBytes());
        NetworkParameters parameters;
        if (AppEnv.getEnv().equals(AppEnv.MAIN)) {
            parameters = MainNetParams.get();
        } else {
            parameters = TestNet3Params.get();
        }
        String address = "";
        switch (type) {
            case START_1:
                address = LegacyAddress.fromKey(parameters, ecKey).toBase58();
                break;
            case START_3:
                address = LegacyAddress.fromScriptHash(parameters, ecKey.getPubKeyHash()).toBase58();
                break;
            case START_BC:
                address = SegwitAddress.fromKey(parameters, ecKey).toBech32();
                break;
        }
        BtcAccount account = new BtcAccount();
        account.setMnemonic(mnemonic);
        account.setPrivateKey(ecKey.getPrivateKeyAsWiF(parameters));
        account.setPublicKey(ecKey.getPublicKeyAsHex());
        account.setAddress(address);
        return account;
    }

    @Override
    public BtcAccount createByPrivateKey(String privateKey, BtcAddressType type) throws Exception {
        BtcAccount account = new BtcAccount();
        NetworkParameters params = AppEnv.getEnv() == AppEnv.MAIN ? MainNetParams.get() : TestNet3Params.get();
        ECKey ecKey = DumpedPrivateKey.fromBase58(params, privateKey).getKey();
        account.setPrivateKey(ecKey.getPrivateKeyAsWiF(params));
        account.setPublicKey(ecKey.getPublicKeyAsHex());
        String address = "";
        switch (type) {
            case START_1:
                address = LegacyAddress.fromKey(params, ecKey).toBase58();
                break;
            case START_3:
                address = LegacyAddress.fromScriptHash(params, ecKey.getPubKeyHash()).toBase58();
                break;
            case START_BC:
                address = SegwitAddress.fromKey(params, ecKey).toBech32();
                break;
        }
        account.setAddress(address);
        return account;
    }

    @Override
    public void transfer(BtcTransferParams params, Callback callback) {

    }

    @Override
    public String name() {
        return "BTC";
    }

    @Override
    public void measure(String url, int index, MeasureCallback callback) {
        long start = System.currentTimeMillis();
        HttpRequest.with(String.class)
                .url(url + "/latestblock")
                .execute(new HttpCallback<String>() {
                    @Override
                    public void onSuccess(String data) {
                        long end = System.currentTimeMillis();
                        if (callback != null) callback.onSuccess(name(),url, index, end - start);
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (callback != null) callback.onError(name(),url, index, e);
                    }
                });
    }

    @Override
    public void getBalance(String account, Callback<String> callback) {
        String url = getUrl(AppEnv.getEnv(), "");
        HttpRequest.with(String.class)
                .url(url + "/balance?active=" + account)
                .execute(new HttpCallback<String>() {
                    @Override
                    public void onSuccess(String data) {
                        try {
                            JSONObject jsonObject = new JSONObject(data);
                            jsonObject = jsonObject.getJSONObject(account);
                            String balance = jsonObject.optString("final_balance");
                            if (callback != null) callback.onSuccess(balance);
                        } catch (Exception e) {
                            e.printStackTrace();
                            if (callback != null) callback.onError(e);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (callback != null) callback.onError(e);
                    }
                });

    }

    @Override
    public void getTokenBalance(String account, String token, String contract, Callback<String> callback) {
        getBalance(account, callback);
    }
}
