package com.blind.wattpadstories.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;

import com.blind.wattpadstories.R;
import com.blind.wattpadstories.model.data.Story;
import com.blind.wattpadstories.presenter.MainPresenter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private MainPresenter presenter;
    private ArrayList<Story> stories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null){
            stories = (ArrayList<Story>) savedInstanceState.getSerializable("stories");
        }

        if (stories == null){
            stories = new ArrayList<>();
        }

        presenter = new MainPresenter();

        RecyclerView rv = findViewById(R.id.rv_stories);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(layoutManager);

        StoriesAdapter mAdapter = new StoriesAdapter(stories, rv, presenter);

        rv.setAdapter(mAdapter);

        presenter.onCreate(savedInstanceState, mAdapter, stories);

        initSearchView();
    }

    /**
     * Initializes the SearchView to populate the RecyclerView based on titles matching the
     * given query.
     *
     * Since there was no API search query given
     * this search is really just a filter for data which has already been pulled from the API
     *
     * While 'SearchView' is expanded (until closed via 'x') API updates are frozen
     *
     * Implementing a true 'search' with only the given query would put undue stress on the API
     * server, as countless requests would be made for every query update as the app
     * attempts to 'fill' the list... an API search query could avoid this predicament while
     * also running much faster
     *
     */
    private void initSearchView(){
        final SearchView searchView = findViewById(R.id.search_view);

        // detects searchview expansion to pause API activity
        searchView.setOnSearchClickListener(presenter);

        // detects text updates to query against the existing dataset
        searchView.setOnQueryTextListener(presenter);

        // detects 'x' press to resume API activity
        searchView.setOnCloseListener(presenter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("stories", stories);
        presenter.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }
}
