package com.chongqing.zoi.dt;

import android.util.Log;

import org.json.JSONObject;

/**
 * Created by xu on 15/4/29.
 */
public class ValidateResponse {
    public int state;
    public String msg;

    public  ValidateResponse(){

    }

    public void ParseJson(JSONObject obj){
        try {
            this.state = obj.getInt("status");

            msg = obj.getString("msg");

        }catch (Exception e){
            Log.e("parseJson", e.getMessage());
        }
    }
}
