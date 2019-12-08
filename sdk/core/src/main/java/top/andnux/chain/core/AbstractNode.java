package top.andnux.chain.core;

public abstract class AbstractNode implements Node {

    @Override
    public String getUrlByUser(AppEnv env, String defaultUrl) {
        return null;
    }

    @Override
    public String getUrl(AppEnv env, String defaultUrl) {
        return null;
    }

    @Override
    public void setUrlByUser(AppEnv env, String url) {

    }

    @Override
    public void setUrl(AppEnv env, String url) {

    }
}
