package v.systems;

import android.util.Log;

import com.google.gson.Gson;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import io.github.novacrypto.bip39.wordlists.English;
import v.systems.utils.Base58;
import v.systems.utils.HashUtil;
import v.systems.utils.JsonUtil;

public class VsysWallet {

    private String seed;
    private List<String> accountSeeds;
    private long nonce;
    private String agent;
    private byte chainId;

    public static final String PROTOCOL = "v.systems";
    public static final int API_VERSION = 3;
    public static final int SEED_API_VERSION = 1;
    public static final int TX_API_VERSION = 1;
    public static final int ADDRESS_API_VERSION = 1;


    private static final String TAG = "Winston";
    private static final String WALLET_SPECIFICATION = "1.0";

    public VsysWallet() {
        seed = "";
        accountSeeds = new ArrayList<>();
        nonce = 0;
        chainId = Chain.MAIN_NET;
        setAgent();
    }

    public VsysWallet(byte chainId, String seed, List<String> accountSeeds, long nonce) {
        this.chainId = chainId;
        this.seed = seed;
        this.accountSeeds = accountSeeds;
        this.nonce = nonce;

        setAgent();
    }

    @SuppressWarnings("all")
    public VsysWallet(byte chainId, String json) {
        HashMap<String, Object> jsonMap = JsonUtil.getJsonAsMap(json);
        String[] keys = {"seed", "accountSeeds", "nonce", "agent"};

        this.chainId = chainId;

        if (JsonUtil.containsKeys(jsonMap, keys)) {
            if (jsonMap != null) {
                seed = (String) jsonMap.get("seed");
            }
            accountSeeds = (ArrayList<String>) jsonMap.get("accountSeeds");
            nonce = Double.valueOf((double) jsonMap.get("nonce")).longValue();
            agent = (String) jsonMap.get("agent");
        }
    }

    public String getSeed() {
        return seed;
    }

    public List<String> getAccountSeeds() {
        return accountSeeds;
    }

    public long getNonce() {
        return nonce;
    }

    public String getAgent() {
        return agent;
    }

    public byte getChainId() {
        return chainId;
    }

    public static VsysWallet recover(byte chainId, String seed, long num) {
        String accountSeed;
        List<String> newAccountSeeds = new ArrayList<>();

        if (seed != null && num > 0) {
            for (long i = 0; i < num; i++) {
                accountSeed = generateAccountSeed(seed, i);
                newAccountSeeds.add(accountSeed);
            }
            return new VsysWallet(chainId, seed, newAccountSeeds, num);
        }
        Log.d(TAG, "Invalid recover");
        return null;
    }

    public void append(long num) {
        String accountSeed;

        if (num > 0) {
            for (long i = nonce; i < nonce + num; i++) {
                accountSeed = generateAccountSeed(seed, i);
                accountSeeds.add(accountSeed);
            }
            nonce += num;
        } else {
            Log.d(TAG, "Invalid append");
        }
    }

    public String getJson() {
        HashMap<String, Object> toJson = new HashMap<String, Object>();
        toJson.put("seed", seed);
        toJson.put("accountSeeds", accountSeeds);
        toJson.put("nonce", nonce);
        toJson.put("agent", agent);
        try {
            return new Gson().toJson(toJson);
        } catch (Exception e) {
            // not expected to ever happen
            return null;
        }
    }

    public static boolean validateSeedPhrase(String seed) {
        if (seed != null) {
            String[] words = seed.split(" ");
            if (Arrays.asList(English.values()).containsAll(Arrays.asList(words))
                    && (words.length == 12 || words.length == 15 || words.length == 18)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Generates a 15-word random seed. This method implements the BIP-39 algorithm with 160 bits of entropy.
     *
     * @return the seed as a String
     */
    public static String generateSeed() {
        byte[] bytes = new byte[21];
        new SecureRandom().nextBytes(bytes);
        byte[] rhash = HashUtil.hash(bytes, 0, 20, HashUtil.SHA256);
        bytes[20] = rhash[0];
        BigInteger rand = new BigInteger(bytes);
        BigInteger mask = new BigInteger(new byte[]{0, 0, 7, -1}); // 11 lower bits
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 15; i++) {
            sb.append(i > 0 ? ' ' : "").append(English.INSTANCE.getWord(rand.and(mask).intValue()));
            rand = rand.shiftRight(11);
        }
        return sb.toString();
    }

    private static String generateAccountSeed(String seed, long nonce) {
        // account seed from seed & nonce
        String noncedSecret = String.valueOf(nonce) + seed;
        ByteBuffer buf = ByteBuffer.allocate(noncedSecret.getBytes().length);
        buf.put(noncedSecret.getBytes());
        byte[] accountSeed = HashUtil.secureHash(buf.array(), 0, buf.array().length);
        return Base58.encode(accountSeed);
    }

    private void setAgent() {
        String chain = "";
        switch (chainId) {
            case Chain.MAIN_NET:
                chain = "mainnet";
                break;
            case Chain.TEST_NET:
                chain = "testnet";
        }
        agent = "V Systems VsysWallet Specification:" + WALLET_SPECIFICATION + "/" + chain;
    }
}
