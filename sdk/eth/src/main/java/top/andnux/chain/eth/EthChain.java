package top.andnux.chain.eth;

import top.andnux.chain.core.Chain;
import top.andnux.chain.core.Provider;

@Provider(EthChainImpl.class)
public interface EthChain extends Chain<EthAccount, EthTransferParams> {

    EthAccount create(String password) throws Exception;

    EthAccount createByPathAndMnemonic(String path, String mnemonics, String password) throws Exception;

    EthAccount createByKeyStore(String path, String keyStore, String password) throws Exception;

}
