package top.andnux.chain.eos;

import top.andnux.chain.core.AccountManager;

public interface EosAccountManager extends AccountManager<EosAccount> {

    EosAccount createAccount();

    EosAccount createAccountByPrivateKey(String privateKey);
}
