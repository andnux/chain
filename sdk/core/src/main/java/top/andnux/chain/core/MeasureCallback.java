package top.andnux.chain.core;

public interface MeasureCallback {

    void onSuccess(String chain,String url,int index, long delayTime);

    void onError(String chain,String url,int index, Throwable e);
}