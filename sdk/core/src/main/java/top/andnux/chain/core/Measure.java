package top.andnux.chain.core;

public interface Measure {

    void measure(String chain, String url, int index, Callback callback);

    /**
     * 回调
     */
    interface Callback {

        void onSuccess(String chain, int index, String url, long delayTime);

        void onError(String chain, int index, String url, Throwable e);
    }
}