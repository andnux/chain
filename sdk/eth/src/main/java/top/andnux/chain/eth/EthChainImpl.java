package top.andnux.chain.eth;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDKeyDerivation;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.MnemonicUtils;
import org.web3j.crypto.WalletFile;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import java.io.File;
import java.io.IOException;

import io.github.novacrypto.bip39.Words;
import top.andnux.chain.core.AbstractChain;
import top.andnux.chain.core.AppEnv;
import top.andnux.chain.core.AppExecutors;
import top.andnux.chain.core.Callback;
import top.andnux.chain.core.MeasureCallback;
import top.andnux.chain.core.Utils;

public class EthChainImpl extends AbstractChain<EthAccount, EthTransferParams>
        implements EthChain {

    private File mFile;

    @Override
    public String name() {
        return "ETH";
    }

    @Override
    public EthAccount createAccount() throws Exception {
        return createAccount("");
    }

    @Override
    public EthAccount createAccount(String password) throws Exception {
        return createAccountByPathAndMnemonic("m/44'/60'/0'/0/0", Utils.createMnemonic(Words.TWELVE), password);
    }
    @Override
    public EthAccount createAccountByPathAndMnemonic(String path, String mnemonics, String password) throws Exception {
        String[] pathArray = path.split("/");
        byte[] seedBytes = MnemonicUtils.generateSeed(mnemonics, "");
        if (seedBytes == null) {
            return null;
        }
        DeterministicKey dkKey = HDKeyDerivation.createMasterPrivateKey(seedBytes);
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
        ECKeyPair keyPair = ECKeyPair.create(dkKey.getPrivKeyBytes());
        EthAccount account = new EthAccount(keyPair.getPrivateKey().toString(16),
                keyPair.getPublicKey().toString(16));
        account.setMnemonic(mnemonics);
        mFile = new File(getApp().getExternalCacheDir(), "keystore");
        if (!mFile.exists())mFile.mkdirs();
        String walletFile = WalletUtils.generateWalletFile(password, keyPair, mFile, false);
        String keystore = Utils.readFileString(new File(mFile, walletFile).getAbsolutePath());
        Credentials credentials = WalletUtils.loadCredentials(password, new File(mFile, walletFile));
        String address = credentials.getAddress();
        account.setAddress(address);
        account.setKeyStore(keystore);
        return account;
    }

    @Override
    public EthAccount createAccountByKeyStore(String path, String keyStore, String password) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        WalletFile walletFile = objectMapper.readValue(keyStore, WalletFile.class);
        ECKeyPair keyPair = EthWallet.decrypt(password, walletFile);
        EthAccount account = new EthAccount(keyPair.getPrivateKey().toString(16),
                keyPair.getPublicKey().toString(16));
        mFile = new File(getApp().getExternalCacheDir(), "keystore");
        if (!mFile.exists())mFile.mkdirs();
        String walletFilePath = WalletUtils.generateWalletFile(password, keyPair, mFile, false);
        String keystore = Utils.readFileString(new File(mFile, walletFilePath).getAbsolutePath());
        Credentials credentials = WalletUtils.loadCredentials(password, new File(mFile, walletFilePath));
        String address = credentials.getAddress();
        account.setAddress(address);
        account.setKeyStore(keystore);
        return account;
    }

    @Override
    public EthAccount createAccountByPrivateKey(String privateKey) throws Exception {
        return null;
    }

    @Override
    public String getDefaultUrl(AppEnv env) {
        String defaultUrl = "";
        switch (env) {
            case MAIN:
                defaultUrl = "https://mainnet.infura.io";
                break;
            case TEST:
                defaultUrl = "https://kovan.infura.io";
                break;
        }
        return defaultUrl;
    }

    @Override
    public void measure(String url, int index, MeasureCallback callback) {
        long start = System.currentTimeMillis();
        AppExecutors instance = AppExecutors.getInstance();
        instance.networkIO().execute(() -> {
            try {
                Web3j web3j = Web3j.build(new HttpService(url));
                web3j.ethGasPrice().send();
                long end = System.currentTimeMillis();
                instance.mainThread().execute(() -> {
                    if (callback != null) {
                        callback.onSuccess(name(),url, index, end - start);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
                instance.mainThread().execute(() -> {
                    if (callback != null) {
                        callback.onError(name(),url, index, e);
                    }
                });
            }
        });
    }

    @Override
    public void getBalance(String account, Callback<String> callback) {

    }

    @Override
    public void getTokenBalance(String account, String token, String contract, Callback callback) {

    }

    @Override
    public void transfer(EthTransferParams params, Callback<String> callback) {

    }
}
