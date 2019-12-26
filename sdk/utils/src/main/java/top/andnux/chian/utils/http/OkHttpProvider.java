package top.andnux.chian.utils.http;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * created on 2019/12/24
 */
public class OkHttpProvider implements HttpProvider {

    private OkHttpClient mClient;

    public OkHttpProvider() {
        this(new OkHttpClient.Builder().build());
    }

    public OkHttpProvider(OkHttpClient client) {
        mClient = client;
    }

    private String mapToGet(Map<String, Object> params) {
        if (params == null || params.isEmpty()) return "";
        StringBuilder stringBuilder = new StringBuilder();
        Set<Map.Entry<String, Object>> entries = params.entrySet();
        int i = 0;
        for (Map.Entry<String, Object> entry : entries) {
            String key = entry.getKey();
            String value = entry.getValue().toString();
            if (i > 0) {
                stringBuilder.append("&");
            }
            stringBuilder.append(key);
            stringBuilder.append("=");
            stringBuilder.append(value);
            i++;
        }
        return stringBuilder.toString();
    }

    @Override
    @SuppressWarnings("all")
    public <T> T get(String url, Map<String, String> headers,
                     Map<String, Object> params, Class<T> clazz) throws Exception {
        String s = mapToGet(params);
        if (!TextUtils.isEmpty(s)) {
            if (!url.contains("?")) {
                url = url + "?" + s;
            } else {
                url = url + s;
            }
        }
        Request.Builder builder = new Request.Builder().url(url).get();
        if (headers != null && !headers.isEmpty()) {
            Set<String> strings = headers.keySet();
            for (String string : strings) {
                builder.addHeader(string, Objects.requireNonNull(headers.get(string)));
            }
        }
        Call call = mClient.newCall(builder.build());
        Response response = call.execute();
        if (response.isSuccessful()){
            String content = response.body().string();
            if (clazz == String.class) {
                return (T) content;
            } else {
                return JSON.parseObject(content, clazz);
            }
        }else {
            throw new IOException(response.message());
        }
    }

    @Override
    public <T> T form(String url, Map<String, String> headers,
                      Map<String, Object> params, Class<T> clazz) throws Exception {
        return null;
    }

    @Override
    public <T> T json(String url, Map<String, String> headers,
                      Map<String, Object> params, Class<T> clazz) throws Exception {
        return null;
    }
}
