package top.andnux.chain.vsys;

import io.github.novacrypto.bip39.Words;
import top.andnux.chain.core.AbstractChain;
import top.andnux.chain.core.AppEnv;
import top.andnux.chain.core.AppExecutors;
import top.andnux.chain.core.Callback;
import top.andnux.chain.core.MeasureCallback;
import top.andnux.chain.core.Utils;
import v.systems.Account;
import v.systems.Blockchain;
import v.systems.contract.TokenBalance;
import v.systems.transaction.PaymentTransaction;
import v.systems.transaction.ProvenTransaction;
import v.systems.transaction.TransactionFactory;
import v.systems.type.NetworkType;

public class VsysChainImpl extends AbstractChain<VsysAccount, VsysTransferParams>
        implements VsysChain {

    @Override
    public String name() {
        return "VSYS";
    }

    @Override
    public VsysAccount create() throws Exception {
        return createByNonce(0);
    }

    @Override
    public VsysAccount createByNonce(Integer nonce) throws Exception {
        String mnemonic = Utils.createMnemonic(Words.FIFTEEN);
        return createByMnemonicAndNonce(mnemonic, nonce);
    }

    private NetworkType getNetworkType() {
        return AppEnv.getEnv() == AppEnv.MAIN ? NetworkType.Mainnet : NetworkType.Testnet;
    }

    @Override
    public VsysAccount createByPrivateKey(String privateKey) throws Exception {
        Account account = new Account(getNetworkType(), privateKey);
        return new VsysAccount(account.getPrivateKey(), account.getPublicKey(), account.getAddress());
    }

    @Override
    public String getDefaultUrl(AppEnv env) {
        String defaultUrl = "";
        switch (env) {
            case MAIN:
                defaultUrl = "http://13.55.174.115:9922";
                break;
            case TEST:
                defaultUrl = "http://test.v.systems:9922";
                break;
        }
        return defaultUrl;
    }

    @Override
    public VsysAccount createByMnemonicAndNonce(String mnemonic, Integer nonce) throws Exception {
        Account account = new Account(getNetworkType(), mnemonic, nonce);
        return new VsysAccount(account.getPrivateKey(), account.getPublicKey(),
                mnemonic, account.getAddress());
    }

    private Blockchain getBlockChain() {
        NetworkType type = getNetworkType();
        return new Blockchain(type, getUrl(AppEnv.getEnv(), ""));
    }

    @Override
    public void measure(String url, int index, MeasureCallback callback) {
        long start = System.currentTimeMillis();
        NetworkType type = getNetworkType();
        Blockchain blockchain = new Blockchain(type, url);
        AppExecutors executors = AppExecutors.getInstance();
        executors.networkIO().execute(() -> {
            try {
                blockchain.getHeight();
                long end = System.currentTimeMillis();
                executors.mainThread().execute(() -> {
                    if (callback != null) {
                        callback.onSuccess(name(), url, index, end - start);
                    }
                });
            } catch (Throwable e) {
                e.printStackTrace();
                executors.mainThread().execute(() -> {
                    if (callback != null) {
                        callback.onSuccess(name(), url, index, -1);
                    }
                });
            }
        });
    }

    @Override
    public void getBalance(String account, Callback<String> callback) {
        AppExecutors executors = AppExecutors.getInstance();
        executors.networkIO().execute(() -> {
            try {
                Blockchain blockchain = getBlockChain();
                Long balance = blockchain.getBalance(account);
                executors.mainThread().execute(() -> {
                    if (callback == null) return;
                    callback.onSuccess(balance.toString());
                });
            } catch (Exception e) {
                e.printStackTrace();
                executors.mainThread().execute(() -> {
                    if (callback == null) return;
                    callback.onError(e);
                });
            }
        });
    }

    @Override
    public void getTokenBalance(String account, String token, String contract, Callback<String> callback) {
        AppExecutors executors = AppExecutors.getInstance();
        executors.networkIO().execute(() -> {
            try {
                Blockchain blockchain = getBlockChain();
                TokenBalance balance = blockchain.getTokenBalance(account, contract);
                executors.mainThread().execute(() -> {
                    if (callback == null) return;
                    callback.onSuccess(balance.getBalance().toString());
                });
            } catch (Exception e) {
                e.printStackTrace();
                executors.mainThread().execute(() -> {
                    if (callback == null) return;
                    callback.onError(e);
                });
            }
        });
    }

    @Override
    public void transfer(VsysTransferParams params, Callback<String> callback) {
        AppExecutors executors = AppExecutors.getInstance();
        executors.networkIO().execute(() -> {
            try {
                Blockchain blockchain = getBlockChain();
                Account account = new Account(getNetworkType(), params.getPrivateKey());
                PaymentTransaction tx = TransactionFactory.buildPaymentTx(params.getTo(),
                        Long.valueOf(params.getQuantity()));
                tx.setFee(params.getFee());
                ProvenTransaction result = account.sendTransaction(blockchain, tx);
                String transactionHash = result.getId();
                executors.mainThread().execute(() -> {
                    if (callback != null) callback.onSuccess(transactionHash);
                });
            } catch (Exception e) {
                e.printStackTrace();
                executors.mainThread().execute(() -> {
                    if (callback != null) callback.onError(e);
                });
            }
        });
    }
}
