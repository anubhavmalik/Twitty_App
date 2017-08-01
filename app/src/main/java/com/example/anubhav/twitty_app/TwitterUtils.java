package com.example.anubhav.twitty_app;

import android.util.Pair;

import com.twitter.sdk.android.core.OAuthSigning;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.internal.network.OAuth1aInterceptor;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Anubhav on 30-07-2017.
 */

public class TwitterUtils {


    //This class stores authconfig and session and has utility functions

    private static TwitterAuthConfig authConfig = TwitterCore.getInstance().getAuthConfig();
    private static TwitterSession twitterSession = TwitterCore.getInstance().getSessionManager().getActiveSession();
    private static TwitterAuthToken authToken = twitterSession.getAuthToken();

    public static void refresh() {
        authConfig = TwitterCore.getInstance().getAuthConfig();
        twitterSession = TwitterCore.getInstance().getSessionManager().getActiveSession();
        authToken = twitterSession.getAuthToken();
    }

    // header using oAuthSinging
    public static String getHeader(String method, String url, Pair... pairs) {

        Map<String, String> params = new HashMap<>();
        for (Pair pair : pairs) {
            params.put(pair.first.toString(), pair.second.toString());
        }

        OAuthSigning oAuthSigning = new OAuthSigning(authConfig, authToken);
        return oAuthSigning.getAuthorizationHeader(method, url, params);
    }

    public static TwitterAuthConfig getAuthConfig() {
        return authConfig;
    }

    public static TwitterSession getSession() {
        return twitterSession;
    }

    public static TwitterAuthToken getAuthToken() {
        return authToken;
    }

    public static Long getUserId() {
        return twitterSession.getUserId();
    }

    public static String getUserScreenName() {
        return twitterSession.getUserName();
    }

    // Twitter interceptor (OAuth1aInterceptor Interceptor class made by Twitter)
    public static OAuth1aInterceptor getInterceptor() {
        return new OAuth1aInterceptor(twitterSession, authConfig);
    }
}
