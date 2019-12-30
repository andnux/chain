package top.andnux.chain.eth;

import top.andnux.chain.core.Chain;
import top.andnux.chain.core.Provider;

@Provider(EthChainImpl.class)
public interface EthChain extends Chain<EthAccount, EthTransferParams> {

    EthAccount generate(String password) throws Exception;

    EthAccount importByMnemonic(String mnemonics, String password) throws Exception;

    EthAccount importByPathAndMnemonic(String path, String mnemonics, String password) throws Exception;

    EthAccount importByKeyStore(String keyStore, String password) throws Exception;

    EthAccount importByKeyStore(String path, String keyStore, String password) throws Exception;

}
