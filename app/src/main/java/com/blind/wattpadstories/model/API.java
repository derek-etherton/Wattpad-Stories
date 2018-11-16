package com.blind.wattpadstories.model;
import org.json.JSONObject;

/**
 * Class for connecting with the Wattpad API
 */
public class API {
    private static final String DEFAULT_URL = "https://www.wattpad.com/api/v3/stories?offset=0&limit=30&fields=stories(id,title,cover,user)";

    public static void retrieveStories(String url, Callback<JSONObject>  callback) {
        JSONFromUrlTask task = new JSONFromUrlTask(callback);
        if (url == null){
            task.execute(DEFAULT_URL);
        } else {
            task.execute(url);
        }
    }
}
