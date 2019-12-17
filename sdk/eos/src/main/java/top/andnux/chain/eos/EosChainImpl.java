package top.andnux.chain.eos;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import one.block.eosiojava.implementations.ABIProviderImpl;
import one.block.eosiojava.interfaces.IABIProvider;
import one.block.eosiojava.interfaces.IRPCProvider;
import one.block.eosiojava.interfaces.ISerializationProvider;
import one.block.eosiojava.models.rpcProvider.Action;
import one.block.eosiojava.models.rpcProvider.Authorization;
import one.block.eosiojava.models.rpcProvider.response.PushTransactionResponse;
import one.block.eosiojava.session.TransactionProcessor;
import one.block.eosiojava.session.TransactionSession;
import one.block.eosiojavaabieosserializationprovider.AbiEosSerializationProviderImpl;
import one.block.eosiojavarpcprovider.implementations.EosioJavaRpcProviderImpl;
import one.block.eosiosoftkeysignatureprovider.SoftKeySignatureProviderImpl;
import top.andnux.chain.core.AbstractChain;
import top.andnux.chain.core.AppEnv;
import top.andnux.chain.core.AppExecutors;
import top.andnux.chain.core.Callback;
import top.andnux.chain.core.MeasureCallback;
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
                authorizations.add(new Authorization(params.getActor(), params.getPermission()));
                List<Action> actions = new ArrayList<>();
                actions.add(new Action(params.getAccount(), params.getName(), authorizations,
                        JSON.toJSONString(data)));
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
    public void measure(String chain, String url, int index, MeasureCallback callback) {
        long start = System.currentTimeMillis();
        final AppExecutors instance = AppExecutors.getInstance();
        instance.networkIO().execute(() -> {
            try {
                IRPCProvider rpcProvider = new EosioJavaRpcProviderImpl(url);
                rpcProvider.getInfo().getHeadBlockNum();
                long end = System.currentTimeMillis();
                instance.mainThread().execute(() -> {
                    if (callback != null) {
                        callback.onSuccess(chain, index, url, end - start);
                    }
                });
            } catch (final Exception e) {
                e.printStackTrace();
                instance.mainThread().execute(() -> {
                    if (callback != null) {
                        callback.onError(chain, index, url, e);
                    }
                });
            }
        });
    }

    @Override
    public void getBalance(String account, Callback callback) {

    }

    @Override
    public void getTokenBalance(String account, String token, String contract, Callback callback) {

    }
}
