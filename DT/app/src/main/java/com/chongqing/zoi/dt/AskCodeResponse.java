package com.chongqing.zoi.dt;

import android.util.Log;

import org.json.JSONObject;

/**
 * Created by xu on 15/4/29.
 */
public class AskCodeResponse {
    public int state;
    public String errMessage;
    public String challengeCode;
    public AskCodeResponse(){

    }

    public void ParseJson(JSONObject obj){
        try {
            this.state = obj.getInt("status");
            if (this.state == 1) {
                JSONObject obj1 = obj.getJSONObject("msg");
                challengeCode = obj1.getString("challengeCode");

            }
            else{
                errMessage = obj.getString("msg");
            }
        }catch (Exception e){
            Log.e("parseJson", e.getMessage());
        }
    }
}
