package top.andnux.chain.core;

public interface Callback<T> {

    void onSuccess(T data);

    void onError(Throwable e);
}