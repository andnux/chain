package top.andnux.chain.eos.impl;

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
import top.andnux.chain.core.AppEnv;
import top.andnux.chain.core.AppExecutors;
import top.andnux.chain.core.Node;
import top.andnux.chain.eos.EosAccountManager;
import top.andnux.chain.eos.EosChain;
import top.andnux.chain.eos.EosTransferParams;

public class EosChainImpl implements EosChain {

    @Override
    public Node nodeManager() {
        return new EosNode();
    }

    @Override
    public EosAccountManager accountManager() {
        return new EosAccountManagerImpl();
    }

    @Override
    public void transfer(final EosTransferParams params, final Callback callback) {
        final AppExecutors instance = AppExecutors.getInstance();
        instance.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Node node = nodeManager();
                    String url = node.getUrl(AppEnv.getEnv(), "");
                    IRPCProvider rpcProvider = new EosioJavaRpcProviderImpl(url);
                    ISerializationProvider serializationProvider = new AbiEosSerializationProviderImpl();
                    IABIProvider abiProvider = new ABIProviderImpl(rpcProvider, serializationProvider);
                    SoftKeySignatureProviderImpl signatureProvider = new SoftKeySignatureProviderImpl();
                    signatureProvider.importKey(params.getPrivateKey());
                    TransactionSession session = new TransactionSession(
                            serializationProvider, rpcProvider, abiProvider, signatureProvider
                    );
                    TransactionProcessor processor = session.getTransactionProcessor();
                    Map<String, String> data = new HashMap<>();
                    data.put("from", params.getFrom());
                    data.put("to", params.getTo());
                    data.put("quantity", params.getValue());
                    data.put("memo", params.getMemo());
                    List<Authorization> authorizations = new ArrayList<>();
                    authorizations.add(new Authorization(params.getActor(), params.getPermission()));
                    List<Action> actions = new ArrayList<>();
                    actions.add(new Action(params.getAccount(), params.getName(), authorizations,
                            JSON.toJSONString(data)));
                    processor.prepare(actions);
                    PushTransactionResponse pushTransactionResponse = processor.signAndBroadcast();
                    final String txId = pushTransactionResponse.getTransactionId();
                    instance.mainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            if (callback != null) {
                                callback.onSuccess(txId);
                            }
                        }
                    });
                } catch (final Exception e) {
                    e.printStackTrace();
                    instance.mainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            if (callback != null) {
                                callback.onError(e);
                            }
                        }
                    });
                }
            }
        });
    }
}
