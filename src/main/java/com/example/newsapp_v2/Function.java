package com.example.newsapp_v2;

import android.content.Context;
import android.net.ConnectivityManager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Function {
    public static boolean isNetworkAvailable(Context context){
        return ((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo()!=null;
    }
    public static String executeGet(String targetUrl,String urlparams){
        URL url;
        HttpURLConnection connection=null;
        try {
            url=new URL(targetUrl);
            connection= (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("content-type","application/json;  charset=utf-8");
            connection.setRequestProperty("Content-Language", "en-US");
            connection.setUseCaches (true);
            connection.setDoInput(true);
            connection.setDoOutput(false);

            InputStream is;
            int status=connection.getResponseCode();
            if(status!=HttpURLConnection.HTTP_OK)
                is=connection.getErrorStream();
            else
                is=connection.getInputStream();

            BufferedReader br=new BufferedReader(new InputStreamReader(is));
            String l;
            StringBuffer response=new StringBuffer();
            while((l = br.readLine()) != null) {
                response.append(l);
                response.append('\r');
            }
            br.close();
            return response.toString();
        }
        catch (Exception e){
            return null;
        }
        /*finally {
            if(connection!=null)
                connection.disconnect();
        }*/
    }
}
