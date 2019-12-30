package top.andnux.chain.btc;

import top.andnux.chain.core.Chain;
import top.andnux.chain.core.Provider;

@Provider(BtcChainImpl.class)
public interface BtcChain extends Chain<BtcAccount, BtcTransferParams> {

    BtcAccount generate()throws Exception;

    BtcAccount generate(BtcAddressType type)throws Exception;

    BtcAccount importByMnemonic(String mnemonic, BtcAddressType type)throws Exception;

    BtcAccount importByMnemonic(String mnemonic,String path, BtcAddressType type)throws Exception;

    BtcAccount importByPrivateKey(String privateKey,BtcAddressType type) throws Exception;
}
