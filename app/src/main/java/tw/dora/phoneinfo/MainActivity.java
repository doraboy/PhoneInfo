package tw.dora.phoneinfo;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
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

        //android 7(包含7)以前版本
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.N){
            amgr = AccountManager.get(this);
            Account[] accounts = amgr.getAccounts();
            Log.v("brad","count: "+ accounts.length);
            for(Account account :accounts){
                Log.v("brad",account.type+" : "+account.name);
            }

        } else{ //android 8(包含8)以後版本
            //amgr = (AccountManager) getSystemService(Context.ACCOUNT_SERVICE);
            Log.v("brad","android 8以後暫時不支援");
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        init();
    }

    public void test1(View view) {

    }
}
