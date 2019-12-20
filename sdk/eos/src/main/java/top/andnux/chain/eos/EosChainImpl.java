package top.andnux.chain.eos;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import top.andnux.chain.core.AbstractChain;
import top.andnux.chain.core.AppEnv;
import top.andnux.chain.core.AppExecutors;
import top.andnux.chain.core.Callback;
import top.andnux.chain.core.MeasureCallback;
import top.andnux.chain.eos.api.result.PushTransactionResults;
import top.andnux.chain.eos.crypto.ec.EosPrivateKey;

public class EosChainImpl extends AbstractChain<EosAccount, EosTransferParams> implements EosChain {

    @Override
    public EosAccount createAccount() throws Exception{
        EosPrivateKey privateKey = new EosPrivateKey();
        EosAccount eosAccount = new EosAccount();
        eosAccount.setPrivateKey(privateKey.toString());
        eosAccount.setPublicKey(privateKey.getPublicKey().toString());
        return eosAccount;
    }

    @Override
    public EosAccount createAccountByPrivateKey(String privateKey) throws Exception{
        EosPrivateKey eosPrivateKey = new EosPrivateKey(privateKey);
        EosAccount eosAccount = new EosAccount();
        eosAccount.setPrivateKey(eosPrivateKey.toString());
        eosAccount.setPublicKey(eosPrivateKey.getPublicKey().toString());
        return eosAccount;
    }

    @Override
    public String getDefaultUrl(AppEnv env) {
        String defaultUrl = "";
        switch (env) {
            case MAIN:
                defaultUrl = "http://eos.newdex.one";
                break;
            case TEST:
                defaultUrl = "http://jungle2.cryptolions.io:80";
                break;
        }
        return defaultUrl;
    }

    @Override
    public void transfer(final EosTransferParams params, final Callback<String> callback) {
        final AppExecutors instance = AppExecutors.getInstance();
        instance.networkIO().execute(() -> {
            try {
                Eos4j eos4j = new Eos4j(getUrl(AppEnv.getEnv(), ""));
                PushTransactionResults transfer = eos4j.transfer(params.getPrivateKey(),
                        params.getContract(), params.getFrom(),
                        params.getTo(), params.getQuantity(), params.getMemo());
                String txId = transfer.getTransactionId();
                instance.mainThread().execute(() -> {
                    if (callback != null) {
                        callback.onSuccess(txId);
                    }
                });
            } catch (final Exception e) {
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
    public String name() {
        return "EOS";
    }

    @Override
    public void measure(String url, int index, MeasureCallback callback) {
        long start = System.currentTimeMillis();
        final AppExecutors instance = AppExecutors.getInstance();
        instance.networkIO().execute(() -> {
            try {
                Eos4j eos4j = new Eos4j(getUrl(AppEnv.getEnv(), ""));
                eos4j.getChainInfo();
                long end = System.currentTimeMillis();
                instance.mainThread().execute(() -> {
                    if (callback != null) {
                        callback.onSuccess(name(),url, index, end - start);
                    }
                });
            } catch (final Exception e) {
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
        getTokenBalance(account, "EOS", "eosio.token", callback);
    }

    @Override
    public void getTokenBalance(String account, String token, String contract, Callback<String> callback) {
        final AppExecutors instance = AppExecutors.getInstance();
        instance.networkIO().execute(() -> {
            try {
                Eos4j eos4j = new Eos4j(getUrl(AppEnv.getEnv(), ""));
                BigDecimal balance = eos4j.getCurrencyBalance(account, contract, token);
                DecimalFormat format = new DecimalFormat("###.0000");
                String finalBalance = format.format(balance);
                instance.mainThread().execute(() -> {
                    if (callback != null) {
                        callback.onSuccess(finalBalance);
                    }
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
}
