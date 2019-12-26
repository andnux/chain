package top.andnux.chain.xrp;

import top.andnux.chain.core.AbstractChain;
import top.andnux.chain.core.AppEnv;
import top.andnux.chain.core.Callback;
import top.andnux.chain.core.MeasureCallback;

/**
 * created on 2019/12/25
 */
public class XrpChainImpl extends AbstractChain<XrpAccount, XrpTransferParams>
        implements XrpChain {

    @Override
    public String name() {
        return "XRP";
    }

    @Override
    public XrpAccount create() throws Exception {
        return null;
    }

    @Override
    public XrpAccount createByPrivateKey(String privateKey) throws Exception {
        return null;
    }

    @Override
    public String getDefaultUrl(AppEnv env) {
        String defaultUrl = "";
        switch (env) {
            case MAIN:
                defaultUrl = "https://s1.ripple.com:51234";
                break;
            case TEST:
                defaultUrl = "https://s1.ripple.com:51234";
                break;
        }
        return defaultUrl;
    }

    @Override
    public void measure(String url, int index, MeasureCallback callback) {

    }

    @Override
    public void getBalance(String account, Callback<String> callback) {

    }

    @Override
    public void getTokenBalance(String account, String token, String contract, Callback<String> callback) {

    }

    @Override
    public void transfer(XrpTransferParams params, Callback<String> callback) {

    }
}
