package top.andnux.chain.core.http;

import android.annotation.SuppressLint;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

public class HttpsX509TrustManager implements X509TrustManager {

    // 检查客户端证书
    @SuppressLint("TrustAllX509TrustManager")
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

    }

    // 检查服务器端证书
    @SuppressLint("TrustAllX509TrustManager")
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

    }

    // 返回受信任的X509证书数组
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }
}