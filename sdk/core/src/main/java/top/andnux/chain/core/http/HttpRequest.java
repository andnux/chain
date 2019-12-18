package top.andnux.chain.core.http;

import java.util.Map;
import java.util.WeakHashMap;

public class HttpRequest<T> {

    private String mUrl;
    private final Class<T> mClazz;
    private final Map<String, String> mHeaders = new WeakHashMap<>();
    private final Map<String, Object> mParams = new WeakHashMap<>();
    private RequestMethod mRequestMethod = RequestMethod.GET;
    private HttpProvider mProvider = new DefaultHttpProvider();

    private HttpRequest(Class<T> clazz) {
        this.mClazz = clazz;
    }

    public static <T> HttpRequest<T> with(Class<T> clazz) {
        return new HttpRequest<>(clazz);
    }

    public HttpRequest<T> url(String url) {
        mUrl = url;
        return this;
    }

    public HttpRequest<T> addHeader(String key, String value) {
        mHeaders.put(key, value);
        return this;
    }

    public HttpRequest<T> addHeaders(Map<String, String> params) {
        mHeaders.putAll(params);
        return this;
    }

    public HttpRequest<T> addParam(String key, Object value) {
        mParams.put(key, value);
        return this;
    }

    public HttpRequest<T> addParams(Map<String, Object> params) {
        mParams.putAll(params);
        return this;
    }

    public HttpRequest<T> requestMethod(RequestMethod requestMethod) {
        mRequestMethod = requestMethod;
        return this;
    }

    public HttpRequest<T> provider(HttpProvider provider) {
        if (provider == null) {
            throw new IllegalArgumentException("provider is null");
        }
        mProvider = provider;
        return this;
    }

    public String getUrl() {
        return mUrl;
    }

    public Class<T> getClazz() {
        return mClazz;
    }

    public Map<String, String> getHeaders() {
        return mHeaders;
    }

    public Map<String, Object> getParams() {
        return mParams;
    }

    public RequestMethod getRequestMethod() {
        return mRequestMethod;
    }

    public HttpProvider getProvider() {
        return mProvider;
    }

    public T execute() throws Exception {
        return HttpDispatcher.getInstance().execute(this);
    }

    public void execute(HttpCallback<T> callback) {
        HttpDispatcher.getInstance().execute(this,callback);
    }
}
