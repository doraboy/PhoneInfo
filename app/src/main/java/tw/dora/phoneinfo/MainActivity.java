package tw.dora.phoneinfo;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private TelephonyManager tmgr;
    private AccountManager amgr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCOUNT_MANAGER) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED
                ) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_NUMBERS,Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.ACCOUNT_MANAGER,Manifest.permission.GET_ACCOUNTS},
                    123);

        }else{
            init();
        }


    }

    private void init() {
        tmgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String num = tmgr.getLine1Number();
        Log.v("brad","num: "+num);

        String did = tmgr.getDeviceId(); //IMEI:國際行動裝置辨識碼:綁定實體手機用這個
        Log.v("brad","IMEI: "+did);

        String imsi = tmgr.getSubscriberId(); //IMSI:國際移動用戶辨識碼:綁定sim卡(電話號碼)用這個
        Log.v("brad","IMSI: "+imsi); //466(國別台灣),92(中華電信)

        amgr = AccountManager.get(this);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        init();
    }

    public void test1(View view) {
        //android 7(包含7)以前版本
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.N){
            Account[] accounts = amgr.getAccounts();
            Log.v("brad","count: "+ accounts.length);
            for(Account account :accounts){
                Log.v("brad",account.type+" : "+account.name);
            }

        } else{ //android 8(包含8)以後版本
            Log.v("brad","android 8以後暫時不支援");
            Intent googlePicker = AccountManager.newChooseAccountIntent(
                    null,
                    null,
                    new String[]{"com.google", "com.facebook.auth.login"},
                    true,
                    null,
                    null,
                    null,
                    null
            );
            startActivityForResult(googlePicker, 0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK){
            String type = data.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE);
            String name = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            Log.v("brad", type + ":" + name);
        }

    }
}
