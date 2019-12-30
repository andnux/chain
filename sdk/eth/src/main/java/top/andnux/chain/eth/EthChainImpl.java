package top.andnux.chain.eth;

import android.text.TextUtils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDKeyDerivation;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.MnemonicUtils;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.WalletFile;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;

import io.github.novacrypto.bip39.Words;
import top.andnux.chain.core.AbstractChain;
import top.andnux.chain.core.AppEnv;
import top.andnux.chain.core.AppExecutors;
import top.andnux.chain.core.Callback;
import top.andnux.chain.core.MeasureCallback;
import top.andnux.chain.core.Utils;

public class EthChainImpl extends AbstractChain<EthAccount, EthTransferParams>
        implements EthChain {


    private static final String PATH = "m/44'/60'/0'/0/0";
    private File mFile;

    @Override
    public String name() {
        return "ETH";
    }

    @Override
    public EthAccount importPrivateKey(String privateKey) throws Exception {
        return null;
    }

    @Override
    public EthAccount generate(String password) throws Exception {
        return importByPathAndMnemonic(PATH, Utils.generateMnemonic(Words.TWELVE), password);
    }

    @Override
    public EthAccount importByMnemonic(String mnemonics, String password) throws Exception {
        return importByPathAndMnemonic(PATH, mnemonics, password);
    }

    @Override
    public EthAccount importByPathAndMnemonic(String path, String mnemonics, String password) throws Exception {
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
    public EthAccount importByKeyStore(String keyStore, String password) throws Exception {
        return importByKeyStore(PATH, keyStore, password);
    }

    @Override
    public EthAccount importByKeyStore(String path, String keyStore, String password) throws Exception {
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
    public String getDefaultUrl() {
        AppEnv env = AppEnv.getEnv();
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
        AppExecutors instance = AppExecutors.getInstance();
        instance.networkIO().execute(() -> {
            try {
                Web3j web3j = Web3j.build(new HttpService(getUrl("")));
                BigInteger balance = web3j.ethGetBalance(account,
                        DefaultBlockParameterName.LATEST).send().getBalance();
                instance.mainThread().execute(() -> {
                    if (callback != null) {
                        callback.onSuccess(balance.toString());
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
                instance.mainThread().execute(() -> {
                    if (callback != null) {
                        callback.onError(e);
                    }
                });
            }
        });
    }

    @Override
    public void getTokenBalance(String account, String token, String contract, Callback<String> callback) {
        if (TextUtils.isEmpty(token) || TextUtils.isEmpty(contract)) {
            getBalance(account, callback);
            return;
        }
        AppExecutors instance = AppExecutors.getInstance();
        instance.networkIO().execute(() -> {
            try {
                BigInteger balance;
                String end = account;
                if (account.startsWith("0x")) {
                    end = account.substring(2);
                }
                String data = "0x70a08231000000000000000000000000" + end;
                Transaction transaction = Transaction.createEthCallTransaction(account, contract, data);
                Web3j web3j = Web3j.build(new HttpService(getUrl("")));
                String value = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).send().getValue();
                if (!TextUtils.isEmpty(value)) {
                    balance = Numeric.toBigInt(value);
                } else {
                    balance = BigInteger.ZERO;
                }
                BigInteger finalBalance = balance;
                instance.mainThread().execute(() -> {
                    if (callback == null) return;
                    callback.onSuccess(finalBalance.toString());
                });
            } catch (Exception e) {
                e.printStackTrace();
                instance.mainThread().execute(() -> {
                    if (callback != null) {
                        callback.onError(e);
                    }
                });
            }
        });

    }

    @Override
    public void transfer(EthTransferParams params, Callback<String> callback) {
        AppExecutors instance = AppExecutors.getInstance();
        instance.networkIO().execute(() -> {
            Web3j web3j = Web3j.build(new HttpService(getUrl("")));
            try {
                if (params.getNonce() == null) {
                    BigInteger nonce = web3j.ethGetTransactionCount(params.getFrom(),
                            DefaultBlockParameterName.LATEST).send().getTransactionCount();
                    params.setNonce(nonce);
                }
                RawTransaction rawTransaction;
                BigInteger gasLimit = params.getGasLimit();
                if (TextUtils.isEmpty(params.getContract())) {
                    if (params.getGasLimit().longValue() <= 0) {
                        gasLimit = BigInteger.valueOf(21000);
                    }
                    rawTransaction = RawTransaction.createTransaction(params.getNonce(), params.getGasPrice(),
                            gasLimit, params.getTo(), BigInteger.valueOf(Long.valueOf(params.getQuantity())), "");
                } else {
                    if (params.getGasLimit().longValue() <= 0) {
                        gasLimit = BigInteger.valueOf(60000);
                    }
                    Function function = new Function(
                            "transfer",
                            Arrays.asList(new Address(params.getTo()), new Uint256(Long.valueOf(params.getQuantity()))),
                            Collections.singletonList(new TypeReference<Type>() {
                            }));
                    String encodedFunction = FunctionEncoder.encode(function);
                    rawTransaction = RawTransaction.createTransaction(params.getNonce(), params.getGasPrice(), gasLimit,
                            params.getContract(), encodedFunction);
                }
                if (params.getPrivateKey().startsWith("0x")) {
                    params.setPrivateKey(params.getPrivateKey().substring(2));
                }
                ECKeyPair ecKeyPair = ECKeyPair.create(new BigInteger(params.getPrivateKey(), 16));
                Credentials credentials = Credentials.create(ecKeyPair);
                byte[] signMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
                String signData = Numeric.toHexString(signMessage);
                EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(signData).send();
                if (ethSendTransaction.hasError()) {
                    String s = ethSendTransaction.getError().getMessage();
                    instance.mainThread().execute(() -> {
                        if (callback != null) callback.onError(new RuntimeException(s));
                    });
                } else {
                    String transactionHash = ethSendTransaction.getTransactionHash();
                    if (!TextUtils.isEmpty(transactionHash)) {
                        instance.mainThread().execute(() -> {
                            if (callback != null) callback.onSuccess(transactionHash);
                        });
                    } else {
                        instance.mainThread().execute(() -> {
                            if (callback != null)
                                callback.onError(new RuntimeException("no hash"));
                        });
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                instance.mainThread().execute(() -> {
                    if (callback != null) callback.onError(e);
                });
            }
        });
    }
}
