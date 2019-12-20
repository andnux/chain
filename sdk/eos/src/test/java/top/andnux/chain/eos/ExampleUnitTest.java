package top.andnux.chain.eos;

import org.junit.Test;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        DecimalFormat format = new DecimalFormat("###.0000");
        String s = format.format(new BigDecimal("999999999999999.555555555"));
        System.out.println(s);
    }
}