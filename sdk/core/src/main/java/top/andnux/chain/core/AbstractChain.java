package top.andnux.chain.core;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

public abstract class AbstractChain<A extends Account, T extends TransferParams> implements Chain<A, T> {

    private Application mApp;
    private SharedPreferences mPreferences;

    public Application getApp() {
        return mApp;
    }

    public SharedPreferences getPreferences() {
        return mPreferences;
    }

    public AbstractChain() {
        mApp = Utils.getApp();
        mPreferences = mApp.getSharedPreferences("chain", Context.MODE_PRIVATE);
    }

    @Override
    public String getUrlByUser(String defaultUrl) {
        AppEnv env = AppEnv.getEnv();
        String key = name().toLowerCase() + "_" + env.name().toLowerCase() + "_user";
        return mPreferences.getString(key, defaultUrl);
    }

    @Override
    public String getUrl(String defaultUrl) {
        AppEnv env = AppEnv.getEnv();
        String key = name().toLowerCase() + "_" + env.name().toLowerCase() + "_url";
        String value = getUrlByUser(defaultUrl);
        if (TextUtils.isEmpty(value)) {
            value = mPreferences.getString(key, defaultUrl);
        }
        if (TextUtils.isEmpty(value)) {
            value = getDefaultUrl();
        }
        if (!value.endsWith("/")) {
            value += "/";
        }
        return value;
    }

    @Override
    public void setUrlByUser(String url) {
        AppEnv env = AppEnv.getEnv();
        String key = name().toLowerCase() + "_" + env.name().toLowerCase() + "_user";
        mPreferences.edit().putString(key, url).apply();
    }

    @Override
    public void setUrl(String url) {
        AppEnv env = AppEnv.getEnv();
        String key = name().toLowerCase() + "_" + env.name().toLowerCase() + "_url";
        mPreferences.edit().putString(key, url).apply();
    }
}
