package top.andnux.chain;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import top.andnux.chain.core.Callback;
import top.andnux.chain.core.ChainFactory;
import top.andnux.chain.eos.EosAccount;
import top.andnux.chain.eos.EosChain;
import top.andnux.chain.eos.EosTransferParams;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        try {
//            BtcChain chain = ChainFactory.getChain(BtcChain.class);
//            if (chain != null) {
//                BtcAccount account = chain.createAccount();
//                Log.d("MainActivity", "account:" + account);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        try {
//            EosChain chain = ChainFactory.getChain(EosChain.class);
//            if (chain != null) {
//                chain.getBalance("zhangchun123", new Callback<String>() {
//                    @Override
//                    public void onSuccess(String data) {
//                        Toast.makeText(MainActivity.this, data, Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        try {
            EosChain chain = ChainFactory.getChain(EosChain.class);
            if (chain != null) {
                EosAccount key = chain.createAccountByPrivateKey("5Jh9NSBXPWsN77Qfp5AfhGfMCg2DLSknXwmjTmN8c9f56ML2Wne");
                Log.d("MainActivity", "key:" + key);
                EosTransferParams params = new EosTransferParams();
                params.setPermission("owner");
                params.setPrivateKey("5JNNm5t64sC6HRXT2oMDJJSULyciSHztpqKqdm62RHChvBjmMSB");
                params.setTo("eogt1itsdbll");
                params.setQuantity("0.0001 EOS");
                params.setFrom("zhangchunlin");
                chain.transfer(params, new Callback<String>() {
                    @Override
                    public void onSuccess(String data) {
                        Toast.makeText(MainActivity.this, data, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        String message = e.getMessage();
                        if (message != null) {
                          Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
