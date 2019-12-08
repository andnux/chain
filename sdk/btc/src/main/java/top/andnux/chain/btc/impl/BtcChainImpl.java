package top.andnux.chain.btc.impl;

import top.andnux.chain.btc.BtcAccountManager;
import top.andnux.chain.btc.BtcChain;
import top.andnux.chain.btc.BtcTransferParams;
import top.andnux.chain.core.Node;

public class BtcChainImpl implements BtcChain {
    @Override
    public Node nodeManager() {
        return new BtcNode();
    }

    @Override
    public BtcAccountManager accountManager() {
        return new BtcAccountManagerImpl();
    }

    @Override
    public void transfer(BtcTransferParams params, Callback callback) {

    }
}
