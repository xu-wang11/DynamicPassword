package com.chongqing.zoi.dt;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


import org.apaches.commons.codec.binary.Hex;

public class MainActivity extends ActionBarActivity {

    public NetworkHelper helper = new NetworkHelper();
    private DynamicTokenApplication application = null;
    private final MainActivity self = this;
    private Handler mHandler = new Handler(){

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    //获取传递的数据
                    Bundle data = msg.getData();
                    //int count = data.getInt("COUNT");
                    //处理UI更新等操作
                    Intent intent = new Intent();
                    intent.putExtra("token", data.getString("token"));
                    intent.setClass(MainActivity.this, AskCodeActivity.class);
                    startActivity(intent);
                    break;
                case 1:
                    Bundle data1 = msg.getData();
                    String err = data1.getString("err");
                    new AlertDialog.Builder(self)
                            .setTitle("Warning")
                            .setMessage(err)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    break;

            }
        };
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn = (Button)findViewById(R.id.ButtonLogin);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        });
        btn = (Button) findViewById(R.id.GoRegisterButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, RegisterActivity.class);
                startActivity(intent);


            }
        });
        btn = (Button) findViewById(R.id.ValidateButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, ValidateActivity.class);
                startActivity(intent);


            }
        });
        btn = (Button) findViewById(R.id.AskCodeButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(application.userKey == null){
                    new AlertDialog.Builder(self)
                            .setTitle("Warning")
                            .setMessage("请先登录")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete

                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    return;
                }
                else {
                    Runnable connectRunnable = new Runnable() {
                        public void run() {
                            AskCodeResponse response = helper.AskForCode(application.UserName, application.Password);
                            if (response.state == 1) {
                                String challengeCode = response.challengeCode;
                                try {
                                    String r1 = encrypt_des_ecb(application.userKey, challengeCode);
                                    String r2 = AeSimpleSHA1.SHA1(r1);

                                    String r3 = encrypt_des_ecb(application.userKey, application.IMEICode);
                                    String r4 = AeSimpleSHA1.SHA1(r3);
                                    char[] token = new char[8];
                                    for (int i = 0; i < 8; i++) {
                                        int x = Integer.parseInt(r2.substring(i * 5, (i + 1) * 5), 16);
                                        int y = Integer.parseInt(r4.substring(i * 5, (i + 1) * 5), 16);
                                        int z = x ^ y;
                                        token[i] = (char) ((z % 10) + '0');
                                    }
                                    Bundle bundle = new Bundle();
                                    bundle.putString("token", new String(token));
                                    Message msg = new Message();
                                    msg.what = 0;
                                    msg.setData(bundle);
                                    mHandler.sendMessage(msg);


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            } else {
                                Message msg = new Message();
                                msg.what = 1;
                                Bundle bundle = new Bundle();
                                bundle.putString("err", response.errMessage);
                                msg.setData(bundle);
                                mHandler.sendMessage(msg);

                            }

                        }

                    };
                    //handler.post(connectRunnable);
                    new Thread(connectRunnable).start();
                }

            }
        });
        application = (DynamicTokenApplication)getApplication();
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = telephonyManager.getDeviceId();

        application.IMEICode = deviceId;
    }

    public String encrypt_des_ecb(String keys, String text) throws Exception {
        byte[] key = keys.getBytes();
        byte[] plainText = text.getBytes();

        SecretKey secretKey = new SecretKeySpec(key, "DES");
        //encrypt
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedData = cipher.doFinal(plainText);
        return org.apaches.commons.codec.binary.Hex.encodeHexString(encryptedData);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
