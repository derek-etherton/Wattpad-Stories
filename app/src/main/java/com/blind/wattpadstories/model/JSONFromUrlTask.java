package com.blind.wattpadstories.model;

import android.os.AsyncTask;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Connects to the given URL and tries to use the data there to create a JSONObject
 */
class JSONFromUrlTask extends AsyncTask<String, Void, JSONObject> {
    private Callback<JSONObject>  callback;

    JSONFromUrlTask(Callback<JSONObject> callback){
        this.callback = callback;
    }

    protected JSONObject doInBackground(String... urls) {
        JSONObject data = null;
        InputStream is = null;

        try {
            URL url = new URL(urls[0]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            is = conn.getInputStream();
            String value = IOUtils.toString(is, "UTF-8");
            data = new JSONObject(value);
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ioe){
                    ioe.printStackTrace();
                }
            }
        }

        return data;
    }

    @Override
    protected void onPostExecute(JSONObject result) {
        super.onPostExecute(result);
        callback.onResult(result);
    }
}
