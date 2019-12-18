package top.andnux.chain.core.http;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class HttpDispatcher {

    private static final HttpDispatcher ourInstance = new HttpDispatcher();
    private Executor mExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private Handler mHandler = new Handler(Looper.getMainLooper());

    public static HttpDispatcher getInstance() {
        return ourInstance;
    }

    private HttpDispatcher() {

    }

    public <T> T execute(HttpRequest<T> request) throws Exception {
        if (request.getProvider() == null) {
            throw new IllegalArgumentException("provider is null");
        }
        if (request.getRequestMethod() == RequestMethod.GET) {
            return request.getProvider().get(request.getUrl(), request.getHeaders(),
                    request.getParams(), request.getClazz());
        } else if (request.getRequestMethod() == RequestMethod.FORM) {
            return request.getProvider().form(request.getUrl(), request.getHeaders(),
                    request.getParams(), request.getClazz());
        } else if (request.getRequestMethod() == RequestMethod.JSON) {
            return request.getProvider().json(request.getUrl(), request.getHeaders(),
                    request.getParams(), request.getClazz());
        }
        return null;
    }

    public <T> void execute(HttpRequest<T> request, HttpCallback<T> callback) {
        mExecutor.execute(() -> {
            try {
                T data = execute(request);
                mHandler.post(() -> {
                    if (callback != null) {
                        callback.onSuccess(data);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                mHandler.post(() -> {
                    if (callback != null) {
                        callback.onError(e);
                    }
                });
            }
        });
    }
}
