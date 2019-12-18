package top.andnux.chain.core;

import java.util.HashMap;
import java.util.Map;

public class ChainFactory {

    private static Map<String, Object> sChainMap = new HashMap<>();

    @SuppressWarnings("all")
    public static <A extends Account, T extends TransferParams,
            R extends Chain<A, T>> R getChain(Class<? extends R> clazz) throws Exception {
        String key = clazz.getCanonicalName();
        Object chain = sChainMap.get(key);
        if (chain == null) {
            Provider annotation = clazz.getAnnotation(Provider.class);
            Class<?> value = annotation.value();
            if (annotation != null) {
                chain = value.newInstance();
            }
            if (chain != null) {
                sChainMap.put(key, chain);
            }
        }
        return (R) chain;
    }
}
