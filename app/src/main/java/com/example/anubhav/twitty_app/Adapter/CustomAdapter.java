package com.example.anubhav.twitty_app.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.Timeline;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;

/**
 * Created by Anubhav on 30-07-2017.
 */

public class CustomAdapter extends TweetTimelineListAdapter {
    Context context;
    OnTweetClickListener listener;

    public CustomAdapter(Context context, Timeline<Tweet> timeline, OnTweetClickListener listener) {
        super(context, timeline);
        this.context=context;
        this.listener= listener;


    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final int pos = position;
        View view= super.getView(pos,convertView,parent);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onTweetClicked(pos,getItem(pos));
            }
        });
        return view;
    }

    public interface OnTweetClickListener{
        void onTweetClicked(int position,Tweet tweet);
    }
}

