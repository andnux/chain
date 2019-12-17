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

import java.util.Collections;

import io.github.novacrypto.bip39.Words;
import top.andnux.chain.core.AbstractChain;
import top.andnux.chain.core.AppEnv;
import top.andnux.chain.core.Callback;
import top.andnux.chain.core.MeasureCallback;
import top.andnux.chain.core.Utils;

public class BtcChainImpl extends AbstractChain<BtcAccount, BtcTransferParams> implements BtcChain {

    @Override
    public BtcAccount createAccount() throws Exception {
        return createAccount(BtcAddressType.START_1);
    }

    @Override
    public BtcAccount createAccountByPrivateKey(String privateKey) throws Exception {
        return createAccountByPrivateKey(privateKey, BtcAddressType.START_1);
    }

    @Override
    public BtcAccount createAccountByPath(String path) throws Exception {
        return createAccountByMnemonicAndPath(Utils.createMnemonic(Words.TWELVE), path, BtcAddressType.START_1);
    }

    @Override
    public BtcAccount createAccount(BtcAddressType type) throws Exception {
        return createAccountByMnemonicAndPath(Utils.createMnemonic(Words.TWELVE), getPath(), type);
    }

    @Override
    public BtcAccount createAccountByMnemonic(String mnemonic) throws Exception {
        return createAccountByMnemonicAndPath(Utils.createMnemonic(Words.TWELVE), getPath(), BtcAddressType.START_1);
    }

    private String getPath() {
        return AppEnv.getEnv() == AppEnv.MAIN ? "m/44'/1'/0'/0/0" : "m/44'/0'/0'/0/0";
    }

    @Override
    public BtcAccount createAccountByMnemonic(String mnemonic, BtcAddressType type) throws Exception {
        return createAccountByMnemonicAndPath(mnemonic, getPath(), type);
    }

    @Override
    public BtcAccount createAccountByMnemonicAndPath(String mnemonic, String path, BtcAddressType type) throws Exception {
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
    public BtcAccount createAccountByPrivateKey(String privateKey, BtcAddressType type) throws Exception {
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
    public void measure(String chain, String url, int index, MeasureCallback callback) {

    }

    @Override
    public void getBalance(String account, Callback<String> callback) {

    }

    @Override
    public void getTokenBalance(String account, String token, String contract, Callback callback) {

    }
}
