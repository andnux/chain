package top.andnux.chain.core.http;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSON;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import top.andnux.chain.core.BuildConfig;

public class DefaultHttpProvider implements HttpProvider {

    private String getStringContent(InputStream is) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new
                InputStreamReader(is, StandardCharsets.UTF_8));
        StringBuilder stringBuilder = new StringBuilder();
        String line = "";
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        return stringBuilder.toString().trim();
    }

    private static final HostnameVerifier NOT_VERYFY = (s, sslSession) -> true;

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
        URL mUrl = new URL(url.trim().replace("\r\n", ""));
        if (BuildConfig.DEBUG) Log.d("DefaultHttpProvider", url);
        HttpURLConnection connection = (HttpURLConnection) mUrl.openConnection();
        if (url.startsWith("https")) {
            ((HttpsURLConnection) connection).setDefaultHostnameVerifier(NOT_VERYFY);
            TrustManager[] tm = {new HttpsX509TrustManager()};
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tm, new SecureRandom());
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            ((HttpsURLConnection) connection).setSSLSocketFactory(ssf);
        }
        connection.setRequestMethod("GET");
        if (headers != null && !headers.isEmpty()) {
            Set<Map.Entry<String, String>> entries = headers.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                String key = entry.getKey();
                String value = entry.getValue();
                connection.addRequestProperty(key, value);
            }
        }
        connection.connect();
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            InputStream inputStream = connection.getInputStream();
            String content = getStringContent(inputStream);
            if (clazz == String.class) {
                return (T) content;
            } else {
                return JSON.parseObject(content, clazz);
            }
        }
        InputStream errorStream = connection.getErrorStream();
        if (errorStream != null) {
            String content = getStringContent(errorStream);
            throw new IOException(content);
        }
        return null;
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
    public <T> T form(String url, Map<String, String> headers,
                      Map<String, Object> params, Class<T> clazz) throws Exception {
        return post(url, headers, mapToGet(params), clazz);
    }

    @Override
    public <T> T json(String url, Map<String, String> headers,
                      Map<String, Object> params, Class<T> clazz) throws Exception {
        return post(url, headers, JSON.toJSONString(params), clazz);
    }

    @Nullable
    @SuppressWarnings("all")
    private <T> T post(String url, Map<String, String> headers, String data, Class<T> clazz) throws Exception {
        URL mUrl = new URL(url.trim().replace("\r\n", ""));
        if (BuildConfig.DEBUG) Log.d("DefaultHttpProvider", url);
        HttpURLConnection connection = (HttpURLConnection) mUrl.openConnection();
        if (url.startsWith("https")) {
            ((HttpsURLConnection) connection).setDefaultHostnameVerifier(NOT_VERYFY);
            TrustManager[] tm = {new HttpsX509TrustManager()};
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tm, new SecureRandom());
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            ((HttpsURLConnection) connection).setSSLSocketFactory(ssf);
        }
        connection.setRequestMethod("POST");//设置请求方式为POST
        if (headers != null && !headers.isEmpty()) {
            Set<Map.Entry<String, String>> entries = headers.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                String key = entry.getKey();
                String value = entry.getValue();
                connection.addRequestProperty(key, value);
            }
        }
        connection.setDoOutput(true);//允许写出
        connection.setDoInput(true);//允许读入
        connection.setUseCaches(false);//不使用缓存
        connection.connect();//连接
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                connection.getOutputStream(), StandardCharsets.UTF_8));
        writer.write(data);
        writer.close();
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            InputStream inputStream = connection.getInputStream();
            String content = getStringContent(inputStream);
            if (clazz == String.class) {
                return (T) content;
            } else {
                return JSON.parseObject(content, clazz);
            }
        }
        InputStream errorStream = connection.getErrorStream();
        if (errorStream != null) {
            String content = getStringContent(errorStream);
            throw new IOException(content);
        }
        return null;
    }
}
