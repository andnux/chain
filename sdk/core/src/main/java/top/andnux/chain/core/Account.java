package top.andnux.chain.core;
import com.alibaba.fastjson.JSONArray;

import java.io.Serializable;

public class Account implements Serializable {

    private String privateKey;
    private String publicKey;

    public Account() {
    }

    public Account(String privateKey, String publicKey) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    @Override
    public String toString() {
        return JSONArray.toJSONString(this);
    }
}
