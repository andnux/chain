package top.andnux.chain.eos;

import org.junit.Test;

import java.security.SecureRandom;

import io.github.novacrypto.bip39.MnemonicGenerator;
import io.github.novacrypto.bip39.SeedCalculator;
import io.github.novacrypto.bip39.Words;
import io.github.novacrypto.bip39.wordlists.English;
import top.andnux.chain.eos.crypto.ec.EosPrivateKey;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        StringBuilder sb = new StringBuilder();
        byte[] entropy = new byte[Words.TWELVE.byteLength()];
        new SecureRandom().nextBytes(entropy);
        new MnemonicGenerator(English.INSTANCE).createMnemonic(entropy, sb::append);
        byte[] seed = new SeedCalculator().calculateSeed(sb.toString(), "");
        EosPrivateKey eosPrivateKey = new EosPrivateKey(seed);
        System.out.println(eosPrivateKey.toWif());
        System.out.println(eosPrivateKey.getPublicKey().toString());
    }
}