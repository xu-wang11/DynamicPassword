package com.chongqing.zoi.dt;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class RegisterActivity extends ActionBarActivity {

    private final RegisterActivity self = this;

    private Handler mHandler = new Handler(){

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    //获取传递的数据
                    //Bundle data = msg.getData();
                    //int count = data.getInt("COUNT");
                    //处理UI更新等操作
                    new AlertDialog.Builder(self)
                            .setTitle("Info")
                            .setMessage("Register Success.")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    break;
                case 1:
                    Bundle data = msg.getData();
                    String err = data.getString("err");
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
        setContentView(R.layout.activity_register);
        Button btn = (Button)findViewById(R.id.GoRegisterButton);
        final RegisterActivity that = this;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nameText = (EditText)findViewById(R.id.NameRegister);
                EditText passwordText = (EditText)findViewById(R.id.PasswordRegister);
                EditText confirmText = (EditText)findViewById(R.id.ConfirmPasswordRegister);
                final String name = nameText.getText().toString();
                final String pass = passwordText.getText().toString();
                String confirm = confirmText.getText().toString();
                if(name.length() == 0){
                    new AlertDialog.Builder(that)
                            .setTitle("Warning")
                            .setMessage("Name cannot be empty.")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    return;
                }
                if(pass.length() < 6 || pass.length() > 12){
                    new AlertDialog.Builder(that)
                            .setTitle("Warning")
                            .setMessage("Password length must between 6 to 12.")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    return;
                }
                if(pass.compareTo(confirm) != 0){
                    new AlertDialog.Builder(that)
                            .setTitle("Warning")
                            .setMessage("Password is not the same with confirm.")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    return;
                }
                Runnable connectRunnable = new Runnable() {
                    public void run()
                    {
                        NetworkHelper helper = new NetworkHelper();
                        DynamicTokenApplication application = (DynamicTokenApplication)getApplication();

                        RegisterResponse response = helper.Register(name, pass, application.IMEICode);
                        if(response.state == 1){
                            mHandler.sendEmptyMessage(0);
                            application.userKey = response.keyCode;
                            application.UserName = name;
                            application.Password = pass;
                            return;
                        }
                        else{
                            Message msg = new Message();
                            msg.what = 1;
                            Bundle bundle = new Bundle();
                            bundle.putString("err", response.errMessage);
                            msg.setData(bundle);
                            mHandler.sendMessage(msg);

                            return;

                        }


                    }
                };
                //handler.post(connectRunnable);
                new Thread(connectRunnable).start();




            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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
