package com.chongqing.zoi.dt;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by xu on 15/4/29.
 */
public class NetworkHelper {
    public HttpClient client;
    public NetworkHelper()
    {
        client = new DefaultHttpClient();
        ip = "http://101.5.219.233:3000";
    }
    public String ip;
    public RegisterResponse Login(String userName, String password)
    {
        HttpPost post = new HttpPost(ip + "/login/phone");
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("userName", userName));
        nameValuePairs.add(new BasicNameValuePair("password", password));

        try{
            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = client.execute(post);
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                Log.i("network", "success");
                InputStream inStream = response.getEntity().getContent();
                String resultString = convertStreamToString(inStream);
                JSONObject obj = new JSONObject(resultString);
                RegisterResponse reg = new RegisterResponse();
                reg.ParseJson(obj);
                return reg;
            }
        }
        catch (Exception e){
            Log.d("network", e.getMessage());
        }
        return null;
    }

    public RegisterResponse Register(String userName, String password, String deviceId){
        HttpPost post = new HttpPost(ip + "/register");
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("userName", userName));
        nameValuePairs.add(new BasicNameValuePair("password", password));
        nameValuePairs.add(new BasicNameValuePair("deviceId", deviceId));
        try{
            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = client.execute(post);
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                Log.i("network", "success");
                InputStream inStream = response.getEntity().getContent();
                String resultString = convertStreamToString(inStream);
                JSONObject obj = new JSONObject(resultString);
                RegisterResponse reg = new RegisterResponse();
                reg.ParseJson(obj);
                return reg;
            }
        }
        catch (Exception e){
            Log.d("network", e.getMessage());
        }
        return null;
    }


    public AskCodeResponse AskForCode(String userName, String password){
        HttpPost post = new HttpPost(ip + "/login/code");
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("userName", userName));
        nameValuePairs.add(new BasicNameValuePair("password", password));

        try{
            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = client.execute(post);
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                Log.i("network", "success");
                InputStream inStream = response.getEntity().getContent();
                String resultString = convertStreamToString(inStream);
                JSONObject obj = new JSONObject(resultString);
                AskCodeResponse reg = new AskCodeResponse();
                reg.ParseJson(obj);
                return reg;
            }
        }
        catch (Exception e){
            Log.d("network", e.getMessage());
        }
        return null;
    }

    public ValidateResponse Validate(String name, String pass, String token){
        HttpPost post = new HttpPost(ip + "/login/web");
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("userName", name));
        nameValuePairs.add(new BasicNameValuePair("password", pass));
        nameValuePairs.add(new BasicNameValuePair("token", token));

        try{
            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = client.execute(post);
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                Log.i("network", "success");
                InputStream inStream = response.getEntity().getContent();
                String resultString = convertStreamToString(inStream);
                JSONObject obj = new JSONObject(resultString);
                ValidateResponse reg = new ValidateResponse();
                reg.ParseJson(obj);
                return reg;
            }
        }
        catch (Exception e){
            Log.d("network", e.getMessage());
        }
        return null;
    }



    private static String convertStreamToString(InputStream is) {
		/*
		 * To convert the InputStream to String we use the BufferedReader.readLine()
		 * method. We iterate until the BufferedReader return null which means
		 * there's no more data to read. Each line will appended to a StringBuilder
		 * and returned as String.
		 *
		 * (c) public domain: http://senior.ceng.metu.edu.tr/2009/praeda/2009/01/11/a-simple-restful-client-at-android/
		 */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

}
