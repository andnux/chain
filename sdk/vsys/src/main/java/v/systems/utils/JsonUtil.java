package top.andnux.vsys.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.LongSerializationPolicy;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;


public class JsonUtil {
    private static final String TAG = "JsonUtil";
    public static HashMap<String,Object> getJsonAsMap(String str){
        if (isJsonString(str)){
            try{
                Gson gsonBuilder = new GsonBuilder().setLongSerializationPolicy(LongSerializationPolicy.STRING).create();
                str = str.replaceAll("\"amount\":(\\d+)", "\"amount\":\"$1\"");
                HashMap<String,Object> gson =  gsonBuilder.fromJson(str, new TypeToken<HashMap<String, Object>>(){}.getType());
                Log.d(TAG, gson.toString());
                return gson;
            }
            catch(Exception e){
                return null;
            }
        }
        return null;
    }

    public static boolean containsKeys(HashMap<String,Object> jsonMap, String[] keys){
        for (String key:keys){
            if (!jsonMap.containsKey(key)) return false;
        }
        return true;
    }

    public static boolean isJsonString(String str){
        try {
            Object o = new Gson().fromJson(str, Object.class);
            return true;
        } catch (Exception e) {
            Log.d(TAG,"e.message" + e.getMessage());
            return false;
        }
    }
}
