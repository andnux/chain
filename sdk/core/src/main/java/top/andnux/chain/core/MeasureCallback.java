package top.andnux.chain.core;

public interface MeasureCallback {

    void onSuccess(String chain, int index, String url, long delayTime);

    void onError(String chain, int index, String url, Throwable e);
}