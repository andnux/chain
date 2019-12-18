package top.andnux.chain.core.http;

public interface HttpCallback<T> {

    void onSuccess(T data);

    void onError(Throwable e);
}
