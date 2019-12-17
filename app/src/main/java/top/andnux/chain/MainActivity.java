package top.andnux.chain;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import top.andnux.chain.btc.BtcAccount;
import top.andnux.chain.btc.BtcChain;
import top.andnux.chain.core.AppEnv;
import top.andnux.chain.core.ChainFactory;
import top.andnux.chain.eth.EthAccount;
import top.andnux.chain.eth.EthChain;
import top.andnux.chain.tron.TronAccount;
import top.andnux.chain.tron.TronChain;
import top.andnux.chain.vsys.VsysAccount;
import top.andnux.chain.vsys.VsysChain;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            BtcChain chain = ChainFactory.getChain(BtcChain.class);
            if (chain != null) {
                BtcAccount account = chain.createAccount();
                System.out.println(account);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
