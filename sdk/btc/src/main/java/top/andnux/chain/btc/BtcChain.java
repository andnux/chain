package top.andnux.chain.btc;

import top.andnux.chain.core.Chain;
import top.andnux.chain.core.Provider;

@Provider(BtcChainImpl.class)
public interface BtcChain extends Chain<BtcAccount, BtcTransferParams> {

    BtcAccount create(BtcAddressType type)throws Exception;

    BtcAccount createByMnemonic(String mnemonic) throws Exception;

    BtcAccount createByMnemonic(String mnemonic, BtcAddressType type) throws Exception;

    BtcAccount createByPath(String path)throws Exception;

    BtcAccount createByMnemonicAndPath(String mnemonic,String path, BtcAddressType type)throws Exception;

    BtcAccount createByPrivateKey(String privateKey,BtcAddressType type) throws Exception;
}
