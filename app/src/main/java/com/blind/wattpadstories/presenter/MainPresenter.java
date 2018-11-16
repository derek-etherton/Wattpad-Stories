package com.blind.wattpadstories.presenter;

import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.View;

import com.blind.wattpadstories.model.API;
import com.blind.wattpadstories.model.Callback;
import com.blind.wattpadstories.model.JSONHelper;
import com.blind.wattpadstories.model.data.Story;
import com.blind.wattpadstories.view.LoadMoreListener;
import com.blind.wattpadstories.view.StoriesAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainPresenter implements SearchView.OnQueryTextListener, SearchView.OnClickListener,
        LoadMoreListener, SearchView.OnCloseListener {

    private StoriesAdapter mAdapter;
    // 'visible' stories, tied to mAdapter
    private ArrayList<Story> stories;

    // unfiltered list of available 'stories'
    private ArrayList<Story> searchList;
    // url to receive next batch of 30 stories, provided by the API
    private String nextUrl;

    public void onCreate(Bundle savedInstanceState, StoriesAdapter adapter,
                         ArrayList<Story> stories) {

        this.mAdapter = adapter;
        this.stories = stories;

        if (savedInstanceState != null) {
            if (savedInstanceState.getBoolean("isLoading", false)){
                mAdapter.setLoading();
            }
            searchList = (ArrayList<Story>) savedInstanceState.getSerializable("searchList");
            nextUrl = savedInstanceState.getString("nextURL");
            mAdapter.notifyDataSetChanged();
        } else {
            updateData();
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public void loadMoreStories() {
        updateData();
    }

    private void updateData() {
        mAdapter.setLoading();

        final Callback<List<Story>> getStoriesCallback = new Callback<List<Story>>() {
            @Override
            public void onResult(List<Story> result) {
                stories.addAll(result);
                mAdapter.setLoaded();
                mAdapter.notifyDataSetChanged();
            }
        };

        API.retrieveStories(nextUrl, new Callback<JSONObject>() {
            @Override
            public void onResult(JSONObject result) {
                try {
                    JSONHelper.getStories(result, getStoriesCallback);
                    nextUrl = JSONHelper.getNextURL(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Filters the story list according to the given query, notifying the adapter to update
     * @param query - query entered into SearchView by user
     */
    private void filterStories(String query){
        if(searchList == null) {
            return;
        }
        query = query.toLowerCase();

        // Start with empty list every time; simplest but suboptimal solution
        stories.clear();

        // For later APIs, could use Java8 filtering...
        // stories.addAll(stories.stream().filter(story -> story.getTitle()
        // .contains(newText)).collect(Collectors.toList()));

        // stores all titles which contain but do not start with the query
        List<Story> halfMatches = new ArrayList<>();

        for (Story story : searchList) {
            String title = story.getTitle().toLowerCase();
            if (title.startsWith(query)){
                stories.add(story);
            }
            else if (story.getTitle().toLowerCase().contains(query.toLowerCase())) {
                halfMatches.add(story);
            }
        }

        // 'halfMatches' will appear below all matches which start with the query
        stories.addAll(halfMatches);

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onQueryTextChange(final String newText) {
        if (stories != null) {
            filterStories(newText);
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        // prevents new data being pulled from the API
        mAdapter.setLoading();

        // clone 'stories' to retain unfiltered list
        searchList = new ArrayList<>(stories);
    }

    @Override
    public boolean onClose() {
        // reset the story list to unfiltered state
        if (searchList != null) {
            stories.clear();
            stories.addAll(searchList);
            searchList.clear();
            mAdapter.notifyDataSetChanged();
        }

        // free up adapter to be able to pull data again
        mAdapter.setLoaded();
        return false;
    }

    public void onSaveInstanceState(Bundle outState){
        outState.putBoolean("isLoading", mAdapter.isLoading());
        if (searchList != null && !searchList.isEmpty()) {
            outState.putSerializable("searchList", searchList);
        }
        outState.putString("nextURL", nextUrl);
    }
}
