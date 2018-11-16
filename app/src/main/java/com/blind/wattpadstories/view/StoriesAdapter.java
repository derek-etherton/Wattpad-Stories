package com.blind.wattpadstories.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blind.wattpadstories.R;
import com.blind.wattpadstories.model.data.Story;
import com.squareup.picasso.Picasso;

import java.util.List;

public class StoriesAdapter extends RecyclerView.Adapter<StoriesAdapter.ViewHolder> {
    private List<Story> stories;

    // If 'loading', will not attempt to pull more results from the API
    private boolean loading = false;

    // # of CardViews between user view & bottom of list before querying API for more stories
    private final int LOADING_BUFFER = 5;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView cover;
        TextView title;
        TextView author;

        ViewHolder(View v) {
            super(v);
            cover = v.findViewById(R.id.story_cover);
            title = v.findViewById(R.id.story_title);
            author = v.findViewById(R.id.story_author);
        }
    }

    StoriesAdapter(List<Story> stories, RecyclerView recyclerView, final LoadMoreListener listener){
        this.stories = stories;

        final LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int numItems = lm.getItemCount();
                int lastVisibleItemPosition = lm.findLastVisibleItemPosition();

                if (!loading &&
                        numItems <= lastVisibleItemPosition + LOADING_BUFFER){
                    listener.loadMoreStories();
                }
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.story_list_item, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Story story = stories.get(i);
        initStoryCard(story, viewHolder);
    }

    private void initStoryCard(Story story, ViewHolder vh){
        Picasso.get().load(story.getCover())
                .placeholder(R.drawable.ic_launcher_background)
                .into(vh.cover);
        vh.title.setText(story.getTitle());
        vh.author.setText(story.getUser().getName());
    }

    @Override
    public int getItemCount() {
        return stories.size();
    }

    public void setLoading() {
        loading = true;
    }

    public void setLoaded(){
        loading = false;
    }

    public boolean isLoading(){
        return loading;
    }
}
