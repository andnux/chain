package top.andnux.chain;

import androidx.multidex.MultiDexApplication;

import top.andnux.chain.core.AppEnv;

public class MainApp extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        AppEnv.setEnv(AppEnv.MAIN);
    }
}
