package top.andnux.chain.core;

import android.app.Application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.security.SecureRandom;

import io.github.novacrypto.bip39.MnemonicGenerator;
import io.github.novacrypto.bip39.Words;
import io.github.novacrypto.bip39.wordlists.English;

public class Utils {

    private static Application sApplication;

    public static void init(Application application) {
        sApplication = application;
    }

    public static Application getApp() {
        if (sApplication == null) {
            sApplication = getAppReflex();
        }
        return sApplication;
    }

    @SuppressWarnings("all")
    private static Application getAppReflex() {
        try {
            Class<?> ActivityThreadclz = Class.forName("android.app.ActivityThread");
            Method currentActivityThreadm = ActivityThreadclz.getMethod("currentActivityThread");
            Object currentActivity = currentActivityThreadm.invoke(null);
            Method getApplication = ActivityThreadclz.getMethod("getApplication");
            return (Application) getApplication.invoke(currentActivity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String readFileString(String fileName) {
        StringBuilder builder = new StringBuilder();
        try (FileReader reader = new FileReader(fileName);
             BufferedReader br = new BufferedReader(reader)) {
            String line;
            while ((line = br.readLine()) != null) {
                builder.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }


    public static String createMnemonic(Words words) {
        StringBuilder sb = new StringBuilder();
        byte[] entropy = new byte[words.byteLength()];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(entropy);
        new MnemonicGenerator(English.INSTANCE).createMnemonic(entropy, sb::append);
        return sb.toString();
    }

}
