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


public class ValidateActivity extends ActionBarActivity {

    public ValidateActivity self = this;

    private Handler mHandler = new Handler(){

        public void handleMessage(Message msg) {
            switch (msg.what) {

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
        setContentView(R.layout.activity_validate);
        Button btn = (Button)findViewById(R.id.ButtonValidate);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = ((EditText)findViewById(R.id.NameValidate)).getText().toString();
                final String pass = ((EditText)findViewById(R.id.PasswordValidate)).getText().toString();
                final String token = ((EditText)findViewById(R.id.TokenValidate)).getText().toString();
                Runnable connectRunnable = new Runnable() {
                    public void run()
                    {
                        NetworkHelper helper = new NetworkHelper();
                        DynamicTokenApplication application = (DynamicTokenApplication)getApplication();

                        ValidateResponse response = helper.Validate(name, pass, token);

                        Message msg = new Message();
                        msg.what = 1;
                        Bundle bundle = new Bundle();
                        bundle.putString("err", response.msg);
                        msg.setData(bundle);
                        mHandler.sendMessage(msg);

                        return;




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
        getMenuInflater().inflate(R.menu.menu_validate, menu);
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
