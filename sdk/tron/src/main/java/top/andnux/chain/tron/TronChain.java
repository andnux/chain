package top.andnux.chain.tron;

import top.andnux.chain.core.Chain;
import top.andnux.chain.core.Provider;

@Provider(TronChainImpl.class)
public interface TronChain extends Chain<TronAccount,TronTransferParams> {

    TronAccount generate() throws Exception;

    TronAccount generate(String password) throws Exception;

    TronAccount importByPrivateKey(String privateKey,String password) throws Exception;

    TronAccount importByKeyStore(String keyStore) throws Exception;

    TronAccount importByKeyStore(String keyStore,String password) throws Exception;

    TronAccount importByMnemonic(String mnemonic) throws Exception;

    TronAccount importByMnemonic(String mnemonic,String password) throws Exception;
}
