package top.andnux.chain;

import org.junit.Test;

import top.andnux.chain.btc.BtcAccount;
import top.andnux.chain.btc.BtcChain;
import top.andnux.chain.btc.BtcChainImpl;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        BtcChain btcChain =  new BtcChainImpl();
        BtcAccount account = btcChain.accountManager().createAccount();
        System.out.println(account.getPrivateKey());
        System.out.println(account.getPublicKey());
        System.out.println(account.getAddress());
    }
}