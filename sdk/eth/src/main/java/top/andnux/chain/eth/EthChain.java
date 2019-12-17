package top.andnux.chain.eth;

import top.andnux.chain.core.Chain;
import top.andnux.chain.core.Provider;

@Provider(EthChainImpl.class)
public interface EthChain extends Chain<EthAccount, EthTransferParams> {

    EthAccount createAccount(String password) throws Exception;

    EthAccount createAccountByPathAndMnemonic(String path, String mnemonics, String password) throws Exception;

    EthAccount createAccountByKeyStore(String path, String keyStore, String password) throws Exception;

}
