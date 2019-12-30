package top.andnux.chain.vsys;

import top.andnux.chain.core.Chain;
import top.andnux.chain.core.Provider;

@Provider(VsysChainImpl.class)
public interface VsysChain extends Chain<VsysAccount,VsysTransferParams> {

    VsysAccount generate() throws Exception;

    VsysAccount generate(Integer nonce) throws Exception;

    VsysAccount importByMnemonic(String mnemonic) throws Exception;

    VsysAccount importByMnemonic(String mnemonic, Integer nonce) throws Exception;
}
