package top.andnux.chain.eos;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import one.block.eosiojava.implementations.ABIProviderImpl;
import one.block.eosiojava.interfaces.IABIProvider;
import one.block.eosiojava.interfaces.IRPCProvider;
import one.block.eosiojava.interfaces.ISerializationProvider;
import one.block.eosiojava.models.rpcProvider.Action;
import one.block.eosiojava.models.rpcProvider.Authorization;
import one.block.eosiojava.models.rpcProvider.response.PushTransactionResponse;
import one.block.eosiojava.session.TransactionProcessor;
import one.block.eosiojava.session.TransactionSession;
import one.block.eosiojava.utilities.EOSFormatter;
import one.block.eosiojavaabieosserializationprovider.AbiEosSerializationProviderImpl;
import one.block.eosiojavarpcprovider.implementations.EosioJavaRpcProviderImpl;
import one.block.eosiosoftkeysignatureprovider.SoftKeySignatureProviderImpl;
import top.andnux.chain.core.AbstractChain;
import top.andnux.chain.core.AppEnv;
import top.andnux.chain.core.AppExecutors;
import top.andnux.chain.core.Callback;
import top.andnux.chain.core.MeasureCallback;
import top.andnux.chain.eos.bean.CurrencyBalanceRequest;
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
                String url = getUrl(AppEnv.getEnv(), "");
                IRPCProvider rpcProvider = new EosioJavaRpcProviderImpl(url);
                ISerializationProvider serializationProvider = new AbiEosSerializationProviderImpl();
                IABIProvider abiProvider = new ABIProviderImpl(rpcProvider, serializationProvider);
                SoftKeySignatureProviderImpl signatureProvider = new SoftKeySignatureProviderImpl();
                signatureProvider.importKey(params.getPrivateKey());
                TransactionSession session = new TransactionSession(
                        serializationProvider, rpcProvider, abiProvider, signatureProvider);
                TransactionProcessor processor = session.getTransactionProcessor();
                Map<String, String> data = new HashMap<>();
                data.put("from", params.getFrom());
                data.put("to", params.getTo());
                data.put("quantity", params.getQuantity());
                data.put("memo", params.getMemo());
                List<Authorization> authorizations = new ArrayList<>();
                authorizations.add(new Authorization(params.getFrom(), params.getPermission()));
                List<Action> actions = new ArrayList<>();
                actions.add(new Action(params.getAccount(), params.getName(), authorizations, JSON.toJSONString(data)));
                processor.prepare(actions);
                PushTransactionResponse pushTransactionResponse = processor.signAndBroadcast();
                final String txId = pushTransactionResponse.getTransactionId();
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
                IRPCProvider rpcProvider = new EosioJavaRpcProviderImpl(url);
                rpcProvider.getInfo().getHeadBlockNum();
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
                String url = getUrl(AppEnv.getEnv(), "");
                EosioJavaRpcProviderImpl rpcProvider = new EosioJavaRpcProviderImpl(url);
                MediaType contentType = MediaType.parse("application/json");
                CurrencyBalanceRequest request = new CurrencyBalanceRequest();
                request.setCode(contract);
                request.setAccount(account);
                request.setSymbol(token);
                String json = JSON.toJSONString(request);
                String balance = rpcProvider.getCurrencyBalance(RequestBody.create(contentType, json));
                JSONArray jsonArray = JSONArray.parseArray(balance);
                balance = (String) jsonArray.get(0);
                balance = balance.split(" ")[0];
                String finalBalance = balance;
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
