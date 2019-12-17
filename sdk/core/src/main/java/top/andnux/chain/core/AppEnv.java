package top.andnux.chain.core;

public enum AppEnv {

    MAIN(),
    TEST();

    private static AppEnv sAppEnv = AppEnv.MAIN;

    public static void setEnv(AppEnv env) {
        sAppEnv = env;
    }

    public static AppEnv getEnv() {
        if (sAppEnv == null) sAppEnv = AppEnv.MAIN;
        return sAppEnv;
    }
}
