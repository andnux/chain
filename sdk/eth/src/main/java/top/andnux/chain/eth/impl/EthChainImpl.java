package top.andnux.chain.eth.impl;

import top.andnux.chain.core.Node;
import top.andnux.chain.eth.EthAccountManager;
import top.andnux.chain.eth.EthChain;
import top.andnux.chain.eth.EthTransferParams;

public class EthChainImpl implements EthChain {
    @Override
    public Node nodeManager() {
        return new EthNode();
    }

    @Override
    public EthAccountManager accountManager() {
        return new EthAccountManagerImpl();
    }

    @Override
    public void transfer(EthTransferParams params, Callback callback) {

    }
}
