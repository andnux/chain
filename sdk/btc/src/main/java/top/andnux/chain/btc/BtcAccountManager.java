package top.andnux.chain.btc;

import top.andnux.chain.core.AccountManager;

public interface BtcAccountManager extends AccountManager<BtcAccount> {

    BtcAccount createAccount();

    BtcAccount createAccount(String path);

    BtcAccount createAccount(BtcAddressType type);

    BtcAccount createAccount(String path, BtcAddressType type);
}
