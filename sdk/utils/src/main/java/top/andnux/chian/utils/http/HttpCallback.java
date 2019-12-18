package top.andnux.chian.utils.http;

public interface HttpCallback<T> {

    void onSuccess(T data);

    void onError(Throwable e);
}
