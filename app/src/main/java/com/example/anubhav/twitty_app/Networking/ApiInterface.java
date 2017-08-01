package com.example.anubhav.twitty_app.Networking;

import com.twitter.sdk.android.core.models.Tweet;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Anubhav on 30-07-2017.
 */

public interface ApiInterface {
    @GET("statuses/home_timeline.json")
    Call<ArrayList<Tweet>> getHomeTimeline();

    @POST("statuses/update.json")
    Call<Tweet> updateStatus(@Query("status") String status);

    @POST("statuses/destroy/{id}.json")
    Call<Tweet> deleteTweet(@Path("id") String id);
}
