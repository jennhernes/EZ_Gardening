package com.example.pd_p4_app;

import android.app.Application;
import android.os.AsyncTask;
import android.view.View;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FetchData extends AsyncTask<Activity,Void,Activity> {
    String data ="";
    @Override
    protected Activity doInBackground(Activity... args) {
        try {
            URL url = new URL("https://api.myjson.com/bins/i1cii");//insert url here
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while(line != null){
                line = bufferedReader.readLine();
                data = data + line;
            }

            JSONArray JA = new JSONArray(data);
            for(int i = 0; i < JA.length(); i++){
                JSONObject JO = (JSONObject) JA.get(i);

                User updatedUser = new User();
                updatedUser.setUid(Integer.parseInt(JO.get("plantId").toString()));
                User oldUser = MainActivity.db.userDao().getUserById(updatedUser.getUid());
                if (oldUser != null) {
                    updatedUser.setPlantName(oldUser.getPlantName());
                    updatedUser.setPlantCurrentHumidity(JO.get("plantHumidity").toString());
                    updatedUser.setPlantMinHumidity(oldUser.getPlantMinHumidity());
                    MainActivity.db.userDao().updateUser(updatedUser);
                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return args[0];
    }

    @Override
    protected void onPostExecute(Activity activity) {
        activity.recreate();
    }
}
