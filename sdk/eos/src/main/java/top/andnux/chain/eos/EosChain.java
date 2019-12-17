package top.andnux.chain.eos;


import top.andnux.chain.core.Chain;
import top.andnux.chain.core.Provider;

@Provider(EosChainImpl.class)
public interface EosChain extends Chain<EosAccount, EosTransferParams> {

    EosAccount createAccount() throws Exception;

    EosAccount createAccountByPrivateKey(String privateKey) throws Exception;

}
