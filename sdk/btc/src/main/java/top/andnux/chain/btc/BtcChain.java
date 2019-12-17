package top.andnux.chain.btc;

import top.andnux.chain.core.Chain;
import top.andnux.chain.core.Provider;

@Provider(BtcChainImpl.class)
public interface BtcChain extends Chain<BtcAccount, BtcTransferParams> {

    BtcAccount createAccount(BtcAddressType type)throws Exception;

    BtcAccount createAccountByMnemonic(String mnemonic) throws Exception;

    BtcAccount createAccountByMnemonic(String mnemonic, BtcAddressType type) throws Exception;

    BtcAccount createAccountByPath(String path)throws Exception;

    BtcAccount createAccountByMnemonicAndPath(String mnemonic,String path, BtcAddressType type)throws Exception;

    BtcAccount createAccountByPrivateKey(String privateKey,BtcAddressType type) throws Exception;
}
