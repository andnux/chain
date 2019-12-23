package top.andnux.chain;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import top.andnux.chain.core.ChainFactory;
import top.andnux.chain.eos.EosChain;
import top.andnux.chain.eos.EosChainImpl;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            ChainFactory.putChain("eos", new EosChainImpl());
            EosChain chain = ChainFactory.getChain("eos");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
