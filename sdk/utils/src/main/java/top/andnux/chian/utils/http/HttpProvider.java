package top.andnux.chian.utils.http;

import java.util.Map;

public interface HttpProvider {

    <T> T get(String url, Map<String, String> headers,
              Map<String, Object> params, Class<T> clazz) throws Exception;

    <T> T form(String url, Map<String, String> headers,
               Map<String, Object> params, Class<T> clazz)throws Exception;

    <T> T json(String url, Map<String, String> headers,
               Map<String, Object> params, Class<T> clazz)throws Exception;
}
