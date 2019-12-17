package top.andnux.chain.vsys;

import io.github.novacrypto.bip39.Words;
import top.andnux.chain.core.AbstractChain;
import top.andnux.chain.core.AppEnv;
import top.andnux.chain.core.Callback;
import top.andnux.chain.core.MeasureCallback;
import top.andnux.chain.core.Utils;
import v.systems.Account;
import v.systems.type.NetworkType;

public class VsysChainImpl extends AbstractChain<VsysAccount, VsysTransferParams>
        implements VsysChain {

    @Override
    public String name() {
        return "VSYS";
    }

    @Override
    public VsysAccount createAccount() throws Exception {
        return createAccountByNonce(0);
    }

    @Override
    public VsysAccount createAccountByNonce(Integer nonce) throws Exception {
        String mnemonic = Utils.createMnemonic(Words.FIFTEEN);
        return createAccountByMnemonicAndNonce(mnemonic, nonce);
    }

    private NetworkType getNetworkType() {
        return AppEnv.getEnv() == AppEnv.MAIN ? NetworkType.Mainnet : NetworkType.Testnet;
    }

    @Override
    public VsysAccount createAccountByPrivateKey(String privateKey) throws Exception {
        Account account = new Account(getNetworkType(), privateKey);
        return new VsysAccount(account.getPrivateKey(), account.getPublicKey(), account.getAddress());
    }

    @Override
    public VsysAccount createAccountByMnemonicAndNonce(String mnemonic, Integer nonce) throws Exception {
        Account account = new Account(getNetworkType(), mnemonic, nonce);
        return new VsysAccount(account.getPrivateKey(), account.getPublicKey(),
                mnemonic, account.getAddress());
    }

    @Override
    public void measure(String chain, String url, int index, MeasureCallback callback) {

    }

    @Override
    public void getBalance(String account, Callback<String> callback) {

    }

    @Override
    public void getTokenBalance(String account, String token, String contract, Callback callback) {

    }

    @Override
    public void transfer(VsysTransferParams params, Callback<String> callback) {

    }
}
