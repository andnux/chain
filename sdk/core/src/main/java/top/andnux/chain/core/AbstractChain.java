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
    public String getUrlByUser(AppEnv env, String defaultUrl) {
        String key = name().toLowerCase() + "_" + env.name().toLowerCase() + "_user";
        return mPreferences.getString(key, defaultUrl);
    }

    @Override
    public String getUrl(AppEnv env, String defaultUrl) {
        String key = name().toLowerCase() + "_" + env.name().toLowerCase() + "_url";
        String value = getUrlByUser(env, defaultUrl);
        if (TextUtils.isEmpty(value)) {
            value = mPreferences.getString(key, defaultUrl);
        }
        if (TextUtils.isEmpty(value)) {
            value = getDefaultUrl(env);
        }
        if (!value.endsWith("/")) {
            value += "/";
        }
        return value;
    }

    @Override
    public void setUrlByUser(AppEnv env, String url) {
        String key = name().toLowerCase() + "_" + env.name().toLowerCase() + "_user";
        mPreferences.edit().putString(key, url).apply();
    }

    @Override
    public void setUrl(AppEnv env, String url) {
        String key = name().toLowerCase() + "_" + env.name().toLowerCase() + "_url";
        mPreferences.edit().putString(key, url).apply();
    }
}
