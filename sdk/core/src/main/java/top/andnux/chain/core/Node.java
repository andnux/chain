package top.andnux.chain.core;

public interface Node {

    String name();

    String getUrlByUser(AppEnv env, String defaultUrl);

    String getUrl(AppEnv env, String defaultUrl);

    void setUrlByUser(AppEnv env, String url);

    void setUrl(AppEnv env, String url);

    Measure getMeasure();
}
