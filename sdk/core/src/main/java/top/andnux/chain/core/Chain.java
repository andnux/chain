package top.andnux.chain.core;

public interface Chain<A extends Account, T extends TransferParams> {

    String name();

    A importPrivateKey(String privateKey) throws Exception;

    String getUrlByUser(String defaultUrl);

    String getUrl(String defaultUrl);

    void setUrlByUser(String url);

    void setUrl(String url);

    String getDefaultUrl();

    void measure(String url, int index, MeasureCallback callback);

    void getBalance(String account, Callback<String> callback);

    void getTokenBalance(String account, String token, String contract, Callback<String> callback);

    void transfer(T params, Callback<String> callback);

}
