package top.andnux.chain.core;

public enum AppEnv {

    MAIN(),
    TEST();

    public static AppEnv getEnv() {
        return AppEnv.MAIN;
    }
}
