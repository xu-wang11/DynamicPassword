package com.chongqing.zoi.dt;

import android.util.Log;

import org.json.JSONObject;

/**
 * Created by xu on 15/4/29.
 */
public class RegisterResponse {

    public int state;
    public String errMessage;
    public String keyCode;
    public RegisterResponse(){

    }

    public void ParseJson(JSONObject obj){
        try {
            this.state = obj.getInt("status");
            if (this.state == 1) {
                JSONObject obj1 = obj.getJSONObject("msg");
                keyCode = obj1.getString("ka");

            }
            else{
                errMessage = obj.getString("msg");
            }
        }catch (Exception e){
            Log.e("parseJson", e.getMessage());
        }
    }
}
