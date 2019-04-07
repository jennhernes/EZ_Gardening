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
    public static String urlString;
    String data ="";
    @Override
    protected Activity doInBackground(Activity... args) {
        try {
            if (urlString == null || urlString.equals("")) {
                urlString = "https://api.myjson.com/bins/1aivno";
            }
            URL url = new URL(urlString);//insert url here
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            line = bufferedReader.readLine();
            while(line != null){
                data = data + line;
                line = bufferedReader.readLine();
            }

            JSONObject JO = new JSONObject(data);

            User updatedUser = new User();
            updatedUser.setUid(Integer.parseInt(JO.get("plantID").toString()));
            User oldUser = MainActivity.db.userDao().getUserById(updatedUser.getUid());
            if (oldUser != null) {
                updatedUser.setPlantName(oldUser.getPlantName());
                updatedUser.setPlantCurrentHumidity(Math.round(Float.parseFloat(JO.get("soilMoisture").toString())));
                updatedUser.setPlantMinHumidity(oldUser.getPlantMinHumidity());
                MainActivity.db.userDao().updateUser(updatedUser);
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
