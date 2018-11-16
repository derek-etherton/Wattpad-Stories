package com.blind.wattpadstories.model;

import android.os.AsyncTask;

import com.blind.wattpadstories.model.data.Story;
import com.blind.wattpadstories.model.data.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for parsing data out of the JSON returned by Wattpad's API
 * Format must be formatted as as provided, or a JSONException could be thrown
 *
 * { 'stories': Array, 'nextURL': String }
 *
 * Where each element of the "stories" array matches the format
 * { "id": String, "title": String, "user": Array, "cover": String }
 *
 * and each element of the "user" array matches the format
 * { "name": String, "avatar": String, "fullname": String }
 *
 */

public class JSONHelper {

    /**
     * @param storyJSON - JSON data for a Story in the format
     *                  { "id": String, "title": String, "user": Array, "cover": String }
     * @return a Story object constructed out of the given JSON data
     */
    private static Story createStory(JSONObject storyJSON) throws JSONException {
        Story story = new Story();

        story.setId(storyJSON.getString("id"));
        story.setTitle(storyJSON.getString("title"));
        story.setUser(createUser(storyJSON.getJSONObject("user")));
        story.setCover(storyJSON.getString("cover"));

        return story;
    }

    /**
     * @param userJSON - JSON data for a User in the format
     *                  { "name": String, "avatar": String, "fullname": String }
     * @return a User object constructed out of the given JSON data
     */
    private static User createUser(JSONObject userJSON) throws JSONException {
        User user = new User();

        user.setName(userJSON.getString("name"));
        user.setAvatar(userJSON.getString("avatar"));
        user.setFullname(userJSON.getString("fullname"));

        return user;
    }

    /**
     * Creates a list of Story objects out of the given JSONObject asynchronously
     * and returns it through the callback's onResult method
     *
     * @param data Stories data in the format
     * { 'stories': Array, 'nextURL': String }
     * @param callback - the callback to deliver the results through
     */
    public static void getStories(JSONObject data, Callback<List<Story>> callback) {
        ParseStoriesTask task = new ParseStoriesTask(callback);
        task.execute(data);
    }

    /**
     * @param data Stories data in the format
     * { 'stories': Array, 'nextURL': String }
     * @return 'nexturl' field from JSON data
     */
    public static String getNextURL(JSONObject data) throws JSONException {
        return data.getString("nextUrl");
    }

    /** Asynchronous task for 'getStories' method */
    private static class ParseStoriesTask extends AsyncTask<JSONObject, Void, List<Story>>{
        private Callback<List<Story>> callback;

        ParseStoriesTask(Callback<List<Story>> callback){
            this.callback = callback;
        }

        @Override
        protected List<Story> doInBackground(JSONObject... data) {
            List<Story> stories = new ArrayList<>();
            try {
                JSONArray storiesJSON = data[0].getJSONArray("stories");
                for (int i = 0; i < storiesJSON.length(); i++) {
                    Story story = createStory(storiesJSON.getJSONObject(i));
                    stories.add(story);
                }
            } catch(JSONException jse){
                jse.printStackTrace();
            }

            return stories;
        }

        @Override
        protected void onPostExecute(List<Story> stories) {
            callback.onResult(stories);
            super.onPostExecute(stories);
        }
    }
}
