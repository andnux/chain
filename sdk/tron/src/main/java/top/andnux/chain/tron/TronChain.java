package top.andnux.chain.tron;

import top.andnux.chain.core.Chain;
import top.andnux.chain.core.Provider;

@Provider(TronChainImpl.class)
public interface TronChain extends Chain<TronAccount,TronTransferParams> {

    TronAccount create(String password) throws Exception;

    TronAccount createByPrivateKey(String privateKey,String password) throws Exception;

    TronAccount createByKeyStore(String keyStore) throws Exception;

    TronAccount createByKeyStore(String keyStore,String password) throws Exception;

    TronAccount createByMnemonic(String mnemonic) throws Exception;

    TronAccount createByMnemonic(String mnemonic,String password) throws Exception;
}
