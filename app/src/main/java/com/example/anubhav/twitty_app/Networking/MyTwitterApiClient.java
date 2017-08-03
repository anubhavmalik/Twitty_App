package com.example.anubhav.twitty_app.Networking;

import com.example.anubhav.twitty_app.Utilities.TwitterUtils;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Anubhav on 30-07-2017.
 */

public class MyTwitterApiClient {
    private static ApiInterface apiInterface;
    private static Retrofit retrofit;

    public static ApiInterface getApiInterface(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.twitter.com/1.1/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(new OkHttpClient.Builder()
                            .addInterceptor(TwitterUtils.getInterceptor())
                            .build())
                    .build();

            apiInterface = retrofit.create(ApiInterface.class);
        }
        return apiInterface;
    }
}
