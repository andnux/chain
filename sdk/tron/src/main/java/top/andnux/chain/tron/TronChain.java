package top.andnux.chain.tron;

import top.andnux.chain.core.Chain;
import top.andnux.chain.core.Provider;

@Provider(TronChainImpl.class)
public interface TronChain extends Chain<TronAccount,TronTransferParams> {

    TronAccount createAccount(String password) throws Exception;

    TronAccount createAccountByPrivateKey(String privateKey,String password) throws Exception;

    TronAccount createAccountByKeyStore(String keyStore) throws Exception;

    TronAccount createAccountByKeyStore(String keyStore,String password) throws Exception;

    TronAccount createAccountByMnemonic(String mnemonic) throws Exception;

    TronAccount createAccountByMnemonic(String mnemonic,String password) throws Exception;
}
