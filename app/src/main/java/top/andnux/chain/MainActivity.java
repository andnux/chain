package top.andnux.chain;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import top.andnux.chain.btc.BtcAccount;
import top.andnux.chain.btc.BtcChain;
import top.andnux.chain.core.ChainFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            BtcChain chain = ChainFactory.getChain(BtcChain.class);
            if (chain != null) {
                BtcAccount account = chain.create();
                Log.d("MainActivity", "account:" + account);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
