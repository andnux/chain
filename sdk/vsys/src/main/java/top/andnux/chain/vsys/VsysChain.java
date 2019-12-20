package top.andnux.chain.vsys;

import top.andnux.chain.core.Chain;
import top.andnux.chain.core.Provider;

@Provider(VsysChainImpl.class)
public interface VsysChain extends Chain<VsysAccount,VsysTransferParams> {

    VsysAccount createByNonce(Integer nonce) throws Exception;

    VsysAccount createByMnemonicAndNonce(String mnemonic,Integer nonce) throws Exception;
}
