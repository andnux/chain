package top.andnux.chain.core;

public interface Chain<A extends Account, T extends TransferParams> {

    String name();

    A createAccount() throws Exception;

    A createAccountByPrivateKey(String privateKey) throws Exception;

    String getUrlByUser(AppEnv env, String defaultUrl);

    String getUrl(AppEnv env, String defaultUrl);

    void setUrlByUser(AppEnv env, String url);

    void setUrl(AppEnv env, String url);

    void measure(String chain, String url, int index, MeasureCallback callback);

    void getBalance(String account, Callback<String> callback);

    void getTokenBalance(String account, String token, String contract, Callback callback);

    void transfer(T params, Callback<String> callback);

}
