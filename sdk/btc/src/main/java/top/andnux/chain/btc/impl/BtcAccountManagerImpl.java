package top.andnux.chain.btc.impl;

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

import java.security.SecureRandom;
import java.util.Arrays;

import io.github.novacrypto.bip39.MnemonicGenerator;
import io.github.novacrypto.bip39.Words;
import io.github.novacrypto.bip39.wordlists.English;
import top.andnux.chain.btc.BtcAccount;
import top.andnux.chain.btc.BtcAccountManager;
import top.andnux.chain.btc.BtcAddressType;
import top.andnux.chain.core.AppEnv;

public class BtcAccountManagerImpl implements BtcAccountManager {

    @Override
    public BtcAccount createAccount() {
        return createAccount("m/44'/0'/0'/0/0");
    }

    @Override
    public BtcAccount createAccount(String path) {
        return createAccount(path, BtcAddressType.START_1);
    }

    @Override
    public BtcAccount createAccount(BtcAddressType type) {
        return createAccount("m/44'/0'/0'/0/0",type);
    }

    @Override
    public BtcAccount createAccount(String path, BtcAddressType type) {
        StringBuilder sb = new StringBuilder();
        byte[] entropy = new byte[Words.TWELVE.byteLength()];
        new SecureRandom().nextBytes(entropy);
        new MnemonicGenerator(English.INSTANCE).createMnemonic(entropy, sb::append);
        byte[] seedBytes = MnemonicCode.toSeed(Arrays.asList(sb.toString().split(" ")),
                "");
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
        account.setPrivateKey(ecKey.getPrivateKeyAsWiF(parameters));
        account.setPublicKey(ecKey.getPublicKeyAsHex());
        account.setAddress(address);
        return account;
    }
}
